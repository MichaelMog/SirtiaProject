package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieTitle;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class SimpleServer extends AbstractServer {
    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(MovieTitle.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        return configuration.buildSessionFactory(serviceRegistry);
    }


    private static void initializeData() throws Exception {
        if (getAll(MovieTitle.class).size() > 0)
            return;

        MovieTitle mt1 = new MovieTitle(
                "אהבה פריזאית",
                "Love in Paris",
                "Comedy, Romance",
                "Francois Gilbert",
                "Jean Gaber, Julienne Monet",
                "When a bully from school doesn't recognize her and seeks a relationship after 20 " +
                        "years, Marie leverages the situation to get her revenge.",
                "C:/Users/Liraz/Downloads/MoviePosters/Paris.jpg",
                "10:00-12:30"
        );

        MovieTitle mt2 = new MovieTitle(
                "מבצע גו",
                "Operation Go",
                "Comedy, Adventure",
                "Yuri Pavlovsky",
                "Giorgi Gudashvili, Roman Bierborov",
                "Two charlatan Georgian spies attempt to kidnap the Japanese heir in an effort to " +
                        "impress their superiors.",
                "C:/Users/Liraz/Downloads/MoviePosters/Go.jpg",
                "11:15-12:55"
        );

        session.save(mt1);
        session.save(mt2);
        session.flush();
    }


    private static <T> List<T> getAll(Class<T> object) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(object);
        Root<T> rootEntry = criteriaQuery.from(object);
        CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);

        TypedQuery<T> allQuery = session.createQuery(allCriteriaQuery);
        return allQuery.getResultList();
    }

    private static void printAllMovies() throws Exception {
        List<MovieTitle> movies = getAll(MovieTitle.class);
        for (MovieTitle movie : movies) {
            System.out.print("Movie Id: ");
            System.out.print(movie.getMovieId());
            System.out.print(", Hebrew Name: ");
            System.out.print(movie.getHebrewName());
            System.out.print(", English Name: ");
            System.out.print(movie.getEnglishName());
            System.out.print(", Genres: ");
            System.out.print(movie.getGenres());
            System.out.print(", Producer: ");
            System.out.print(movie.getProducer());
            System.out.print(", Actors: ");
            System.out.print(movie.getActors());
            System.out.print(", Description: ");
            System.out.print(movie.getMovieDescription());
            System.out.print(", Image Path: ");
            System.out.print(movie.getImagePath());
            System.out.print(", Show Times: ");
            System.out.print(movie.getShowTimes());
            System.out.print('\n');
        }
    }

    public SimpleServer(int port) {
        super(port);

        try {
            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();
            session.beginTransaction();

            initializeData();
            printAllMovies();

            session.getTransaction().commit(); // Save everything.

        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
                session.getSessionFactory().close();
            }
        }


    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        String msgString = msg.toString();

        // Display a warning with a timestamp.
        // Command syntax: #warning
        if (msgString.startsWith("#warning")) {
            Warning warning = new Warning("Warning from server!");
            try {
                client.sendToClient(warning);
                System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Send movies over to the client simultaneously but separately.
        // Command syntax: #showMovies
        if (msgString.startsWith("#showMovies")) {
            try {
                SessionFactory sessionFactory = getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction(); // Begin a new DB session
                List<MovieTitle> movies = getAll(MovieTitle.class);
                session.getTransaction().commit();

                for (MovieTitle movie : movies) { // Send each movie to the client.
                    try {
                        client.sendToClient(movie);
                        System.out.format("Sent movie %s to client %s\n", movie.getEnglishName(),
                                client.getInetAddress().getHostAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.err.println("Could not get movie list, changes have been rolled back.");
                e.printStackTrace();
                if (session != null) {
                    session.getTransaction().rollback();
                }
            } finally {
                if (session != null) {
                    session.close(); // Close the session.
                    session.getSessionFactory().close();
                }
            }
        }

        // Change show times of a movie.
        // Command syntax (tab-separated): #changeShowTimes movieId  newShowTimes
        if (msgString.startsWith("#changeShowTimes\t")) {
            try {
                List<String> params = Arrays.asList(msgString.split("\t"));

                int movieId = Integer.parseInt(params.get(1));
                String newShowTimes = params.get(2);

                SessionFactory sessionFactory = getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction(); // Begin a new DB session
                MovieTitle movie = session.get(MovieTitle.class, movieId);

                // Update the show times of the movie.
                System.out.format("Updated movie %s show times from %s to %s.\n",
                        movie.getEnglishName(), movie.getShowTimes(), newShowTimes);
                movie.setShowTimes(newShowTimes);
                session.update(movie);
                session.flush();
                session.getTransaction().commit();
            } catch (Exception e) {
                System.err.println("Could not update the movie, changes have been rolled back.");
                e.printStackTrace();
                if (session != null) {
                    session.getTransaction().rollback();
                }
            } finally {
                if (session != null) {
                    session.close();
                    session.getSessionFactory().close();
                }
            }
        }
    }
}
