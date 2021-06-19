package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
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
        configuration.addAnnotatedClass(Screening.class);
        configuration.addAnnotatedClass(Subscription.class);
        configuration.addAnnotatedClass(Purchase.class);


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
                "2015"
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
                "1995"
        );

        Screening s1 = new Screening(mt1,"10", "10:00-12:30", "Haifa", 10, 10);
        Screening s2 = new Screening(mt1, "12", "10:00-12:30", "Haifa", 10, 8);

        session.save(s1);
        session.save(s2);

        ComingSoonMovie comingSoonMovie = new ComingSoonMovie(mt1, "30");

        LinkMovie linkMovie = new LinkMovie(mt2, "15", "cdnmovies.com/amazingmovie2", "11:00-15:00");

        Purchase purchase1 = new Purchase("Yossi Shindler", "1111222233334444","15:12", 50, 12, mt1, null);
        Purchase purchase2 = new Purchase("Bibi Tibi", "1111222233334444","21:42", 40, 0, null, linkMovie);

        session.save(mt1);
        session.save(mt2);
        session.save(linkMovie);
        session.save(comingSoonMovie);
        session.save(purchase1);
        session.save(purchase2);
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

    private static int issueDeleteQuery(String table, String field, int id) {
        // Issuing a delete query.
        String hql = String.format("delete from %s where %s=%s", table, field, id);
        Query query = session.createQuery(hql);
        return query.executeUpdate();
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
        System.out.print(", Year: ");
        System.out.print(movie.getYear());
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

    public void getSubscription(String full_name, ConnectionToClient client){
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<Subscription> subs = getAll(Subscription.class);
            session.getTransaction().commit();

            for (Subscription sub : subs) {
                if(sub.getFull_name().equals(full_name)){
                    try {
                        client.sendToClient(sub);
                        System.out.format("Sent subscription %d to client %s", sub.getSubscriptionId(), full_name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            System.err.println("Could not get subscription, changes have been rolled back.");
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

    public void showMovies(ConnectionToClient client) {
        /**
         * client: used to send the movies to the user.
         */
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<MovieTitle> movies = getAll(MovieTitle.class);
            List<ComingSoonMovie> comingSoonMovies = getAll(ComingSoonMovie.class);
            List<LinkMovie> linkMovies = getAll(LinkMovie.class);
            List<Screening> screenings = getAll(Screening.class);
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

            for (ComingSoonMovie comingSoonMovie : comingSoonMovies) { // Send each movie to the client.
                try {
                    client.sendToClient(comingSoonMovie);
                    System.out.format("Sent movie %s to client %s\n", comingSoonMovie.getMovieTitle().getEnglishName(),
                            client.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (LinkMovie linkMovie : linkMovies) { // Send each movie to the client.
                try {
                    client.sendToClient(linkMovie);
                    System.out.format("Sent movie %s to client %s\n", linkMovie.getMovieTitle().getEnglishName(),
                            client.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (Screening screening : screenings) { // Send each movie to the client.
                try {
                    client.sendToClient(screening);
                    System.out.format("Sent movie %s to client %s\n", screening.getMovieTitle().getEnglishName(),
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

    public void addTakenSeat(int screeningId, String seat) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            Screening screening = session.get(Screening.class, screeningId);

            System.out.format("successfully taken seat");
            screening.addTakenSeat(seat);
            session.update(screening);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Could not take the seat, changes have been rolled back.");
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

    public void addSubscription(String full_name){
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            Subscription subs = new Subscription(full_name);

            session.save(subs);
            session.flush();
            session.getTransaction().commit();
            System.out.format("successfully added subscription");
        } catch (Exception e) {
            System.err.println("Could not add subscription, changes have been rolled back.");
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

    /*public void changeShowTimes(int movieId, String newShowTimes) {
        /**
         * movieId: id of the movie we change
         * newShowTimes: new show times string
         *
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
    }*/

    public void addMovieTitle(String hebrewName, String englishName, String genres, String producer, String actor,
                              String movieDescription, String imagePath, String year) {
        /**
         * movieInfo: all of the movie information compiled into a string array, ordered as:
         *            hebrewName, englishName, genres, producer, actor, movieDescription, imagePath, year
         */
        try {
            MovieTitle movie = new MovieTitle( hebrewName, englishName, genres, producer, actor, movieDescription, imagePath, year);

            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session

            // Add the movie to the database
            session.save(movie);
            session.flush();
            session.getTransaction().commit();
            System.out.format("Added movie to database: ");
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
                issueDeleteQuery("Purchase", "movie_id", movieTitleId);
                session.flush();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
            }
            try {
                session.beginTransaction();
                issueDeleteQuery("ComingSoonMovie", "movieTitleId", movieTitleId);
                session.flush();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
            }
            try {
                session.beginTransaction();
                issueDeleteQuery("LinkMovie", "movieTitleId", movieTitleId);
                session.flush();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
            }
            try {
                session.beginTransaction();
                issueDeleteQuery("Screening", "movieTitleId", movieTitleId);
                session.flush();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
            }
            session.beginTransaction();
            issueDeleteQuery("MovieTitle", "movieId", movieTitleId);
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

    public void removeComingSoonMovie(int ComingSoonMovieId) {
        /**
         * ComingSoonMovieId: remove matching ComingSoonMovie ID
         */
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            Object persistentInstance = session.load(ComingSoonMovie.class, ComingSoonMovieId);
            session.delete(persistentInstance);
            session.flush();
            session.getTransaction().commit();

            System.out.format("Deleted coming soon movie with ID %s from the database.\n", ComingSoonMovieId);
        } catch (Exception e) {
            System.err.println("Could not delete the coming soon movie, changes have been rolled back.");
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

    public void removeLinkMovie(int linkMovieId) {
        /**
         * linkMovieId: remove matching LinkMovie ID
         */
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            Object persistentInstance = session.load(LinkMovie.class, linkMovieId);
            session.delete(persistentInstance);
            session.flush();
            session.getTransaction().commit();

            System.out.format("Deleted link movie with ID %s from the database.\n", linkMovieId);
        } catch (Exception e) {
            System.err.println("Could not delete the link movie, changes have been rolled back.");
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

    public void removeScreening(int screeningId) {
        /**
         * screeningId: remove matching screening ID
         */
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            Object persistentInstance = session.load(Screening.class, screeningId);
            session.delete(persistentInstance);
            session.flush();
            session.getTransaction().commit();

            System.out.format("Deleted screening with ID %s from the database.\n", screeningId);
        } catch (Exception e) {
            System.err.println("Could not delete the screening, changes have been rolled back.");
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

    public void addScreening(int movieTitleId, String price, String time, String location,  String rows, String columns) {
        /**
         * movieTitleId: id of the movie title
         */
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session

            Screening screening = new Screening(
                    getById(MovieTitle.class, movieTitleId), price, time,
                    location, Integer.parseInt(rows), Integer.parseInt(columns));

            // Add the link movie to the database
            session.save(screening);
            session.flush();
            session.getTransaction().commit();
            System.out.println("Added a new screening to database:");
            printMovie(screening.getMovieTitle());
            System.out.format("Price: %s\nTime: %s\nLocation: %s\nRows: %s\nColumns: %s\nAvailable Seats: %s\n",
                    screening.getPrice(),
                    screening.getTime(),
                    screening.getLocation(),
                    screening.getRows(),
                    screening.getColumns(),
                    screening.getAvailableSeats());
        } catch (Exception e) {
            System.err.println("Could not add the screening, changes have been rolled back.");
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
