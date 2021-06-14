package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class Database {
    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(MovieTitle.class);
        configuration.addAnnotatedClass(ComingSoonMovie.class);
        configuration.addAnnotatedClass(LinkMovie.class);
        configuration.addAnnotatedClass(Purchase.class);
        configuration.addAnnotatedClass(Complaint.class);

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
                "posters/Paris.jpg",
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
                "posters/Go.jpg",
                "11:15-12:55"
        );

        ComingSoonMovie comingSoonMovie = new ComingSoonMovie(mt1, "30");

        LinkMovie linkMovie = new LinkMovie(mt2, "15", "cdnmovies.com/amazingmovie2", "11:00-15:00");

        Purchase purchase1 = new Purchase("Yossi Shindler", "1111222233334444","15:12", 50, 12, mt1, null);
        Purchase purchase2 = new Purchase("Bibi Tibi", "1111222233334444","21:42", 40, 0, null, linkMovie);
        Purchase purchase3 = new Purchase("Leo Mike", "1111222233334444","11:12", 44, 7, mt2, null);

        Complaint complaint1 = new Complaint("John Wick","23:58","blablabla doesn't work",purchase2);
        Complaint complaint2 = new Complaint("Ivan Ivanov","03:03","where is my money???",purchase1);

        session.save(mt1);
        session.save(mt2);
        session.save(linkMovie);
        session.save(comingSoonMovie);
        session.save(purchase1);
        session.save(purchase2);
        session.save(purchase3);
        session.save(complaint1);
        session.save(complaint2);
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

    private static <T> T getById(Class<T> type, Serializable id) {
        T persistentInstance = session.get(type, id);
        return persistentInstance;
    }

    private static boolean deleteById(Class<?> type, Serializable id) {
        // Deleting the movie and its associated entries.
        // See the website for reference (the code uses method 2: "Deleting a persistent instance"):
        // https://www.codejava.net/frameworks/hibernate/hibernate-basics-3-ways-to-delete-an-entity-from-the-datastore
        Object persistentInstance = session.load(type, id);
        if (persistentInstance != null) {
            session.delete(persistentInstance);
            return true;
        }
        return false;
    }

    private static void printAllMovies() throws Exception {
        List<MovieTitle> movies = getAll(MovieTitle.class);
        for (MovieTitle movie : movies) {
            printMovie(movie);
        }
    }

    private static void printMovie(MovieTitle movie) {
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

    public Database() {
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

    public void showMovies(ConnectionToClient client) {
        /**
         * client: used to send the movies to the user.
         */
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

    public void changeShowTimes(int movieId, String newShowTimes) {
        /**
         * movieId: id of the movie we change
         * newShowTimes: new show times string
         */
        try {
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

    public void addMovieTitle(String hebrewName, String englishName, String genres, String producer, String actor,
                              String movieDescription, String imagePath, String showTimes) {
        /**
         * movieInfo: all of the movie information compiled into a string array, ordered as:
         *            hebrewName, englishName, genres, producer, actor, movieDescription, imagePath, showTimes
         */
        try {
            MovieTitle movie = new MovieTitle(
                    hebrewName, englishName, genres, producer, actor, movieDescription, imagePath, showTimes);

            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session

            // Add the movie to the database
            session.save(movie);
            session.flush();
            session.getTransaction().commit();
            System.out.format("Added movie to database:");
            printMovie(movie);
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

    public void addComingSoonMovie(int movieTitleId, String price) {
        /**
         * movieTitleId: id of the movie title
         * price: price string
         */
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session

            ComingSoonMovie comingSoonMovie = new ComingSoonMovie(
                    (MovieTitle) getById(MovieTitle.class, movieTitleId), price);

            // Add the coming soon movie to the database
            session.save(comingSoonMovie);
            session.flush();
            session.getTransaction().commit();
            System.out.println("Added new coming soon movie to database:");
            printMovie(comingSoonMovie.getMovieTitle());
            System.out.print("Price: ");
            System.out.print(comingSoonMovie.getPrice());
            System.out.print('\n');
        } catch (Exception e) {
            System.err.println("Could not add the coming soon movie, changes have been rolled back.");
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

    public void addLinkMovie(int movieTitleId, String price, String link, String watchHours) {
        /**
         * movieTitleId: id of the movie title
         * price: price string
         * link: link string
         * watchHours: times where the link is available to watch string
         */
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session

            LinkMovie linkMovie = new LinkMovie(
                    (MovieTitle) getById(MovieTitle.class, movieTitleId), price, link, watchHours);

            // Add the link movie to the database
            session.save(linkMovie);
            session.flush();
            session.getTransaction().commit();
            System.out.println("Added new link movie to database:");
            printMovie(linkMovie.getMovieTitle());
            System.out.print("Price: ");
            System.out.print(linkMovie.getPrice());
            System.out.print(", Link: ");
            System.out.print(linkMovie.getLink());
            System.out.print(", Watch Hours: ");
            System.out.print(linkMovie.getWatchHours());
            System.out.print('\n');
        } catch (Exception e) {
            System.err.println("Could not add the link movie, changes have been rolled back.");
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

    public void removeMovieTitle(int movieTitleId) {
        /**
         * movieTitleId: id of the movie we remove
         */
        try {
            Serializable movieId = movieTitleId;

            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();

            // Deleting the movie and its associated entries.
            try {
                session.beginTransaction();
                deleteById(ComingSoonMovie.class, movieId);
                session.flush();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
            }
            try {
                session.beginTransaction();
                deleteById(LinkMovie.class, movieId);
                session.flush();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
            }
            session.beginTransaction();
            deleteById(MovieTitle.class, movieId);
            session.flush();
            session.getTransaction().commit();

            System.out.format("Deleted movie with ID %s from the database.\n", movieTitleId);

        } catch (Exception e) {
            System.err.println("Could not delete the movie, changes have been rolled back.");
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
