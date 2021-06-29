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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        configuration.addAnnotatedClass(StagedPriceChange.class);
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

        Screening s1 = new Screening(mt1, "10", "10:00-12:30", "Haifa", 10, 10);
        Screening s2 = new Screening(mt1, "12", "10:00-12:30", "Haifa", 10, 8);

        session.save(s1);
        session.save(s2);

        ComingSoonMovie comingSoonMovie = new ComingSoonMovie(mt1, "30");

        LinkMovie linkMovie = new LinkMovie(mt2, "15", "cdnmovies.com/amazingmovie2", "11:00-15:00");

        session.save(mt1);
        session.save(mt2);
        session.save(linkMovie);
        session.save(comingSoonMovie);
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

    private static int deleteMovieQuery(String table, String field, int id) {
        // Issuing a delete query.
        // See https://www.tutorialspoint.com/hibernate/hibernate_query_language.htm
//        System.out.format("Issuing query: delete from %s where %s = %s\n", table, field, id);
        String hql = String.format("delete from %s where %s = :id", table, field);
        Query query = session.createQuery(hql);
        query.setParameter("id", id); // To prevent SQL injections ;)
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

    public void getSubscription(String full_name, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<Subscription> subs = getAll(Subscription.class);
            session.getTransaction().commit();

            for (Subscription sub : subs) {
                if (sub.getFull_name().equals(full_name)) {
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
            screening.setAvailableSeats(screening.getAvailableSeats() - 1);
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

    public void addSubscription(String full_name) {
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

    public void addPurchase(String name, String payInfo, String takenSeats, int grandTotal, int screening_id, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            Screening screening = session.get(Screening.class, screening_id);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String movieDetail = screening.getMovieTitle().getEnglishName() + " | " + screening.getMovieTitle().getHebrewName() + " at: " + screening.getTime();
            Purchase p = new Purchase(name, payInfo, dtf.format(now), grandTotal, takenSeats, screening.getMovieTitle(), null, movieDetail);

            session.save(p);
            session.flush();
            session.getTransaction().commit();
            System.out.println("Successfully added purchase.");
            client.sendToClient(p);
            System.out.println("Successfully sent purchase to client.");
        } catch (Exception e) {
            System.err.println("Could not add purchase, changes have been rolled back.");
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

    public void addLinkPurchase(String name, String payInfo, int grandTotal, int link_id, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            LinkMovie link = session.get(LinkMovie.class, link_id);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String movieDetail = link.getMovieTitle().getEnglishName() + " | " + link.getMovieTitle().getHebrewName() + " on: " + link.getLink() + " at: " + link.getWatchHours();

            Purchase p = new Purchase(name, payInfo, dtf.format(now), grandTotal, link.getMovieTitle(), link, movieDetail);

            session.save(p);
            session.flush();
            session.getTransaction().commit();
            System.out.format("successfully added purchase");
            client.sendToClient(p);
            System.out.println("successfully sent purchase to client");
        } catch (Exception e) {
            System.err.println("Could not add purchase, changes have been rolled back.");
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

    public void addSubscriptionPurchase(String name, String payInfo, int grandTotal, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            Purchase p = new Purchase(name, payInfo, dtf.format(now), grandTotal, null, null);

            session.save(p);
            session.flush();
            session.getTransaction().commit();
            System.out.format("successfully added purchase");
            client.sendToClient(p);
            System.out.println("successfully sent purchase to client");
        } catch (Exception e) {
            System.err.println("Could not add purchase, changes have been rolled back.");
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

    public void changeTime(String movieType, int movieId, String newTime) {
        /**
         * movieType: Either "Screening" or "LinkMovie"
         * movieId: id of the movie we change
         * newTime: new show times string
         */
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session

            switch (movieType) {
                case "LinkMovie":
                    LinkMovie linkMovie = session.get(LinkMovie.class, movieId);
                    linkMovie.setWatchHours(newTime);
                    System.out.format("Updated movie %s show times from %s to %s.\n",
                            linkMovie.getMovieTitle().getEnglishName(), linkMovie.getWatchHours(), newTime);
                    session.update(linkMovie);
                case "Screening":
                    Screening screening = session.get(Screening.class, movieId);
                    screening.setTime(newTime);
                    System.out.format("Updated movie %s show times from %s to %s.\n",
                            screening.getMovieTitle().getEnglishName(), screening.getTime(), newTime);
                    session.update(screening);
                default:
                    System.err.println("Received " + movieType + " as movieType in changeTime when expected either" +
                            "Screening or LinkMovie. No change has been made.");
            }

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

    public void changePrice(String movieType, int movieId, String newPrice) {
        /**
         * movieType: Either "Screening" or "LinkMovie"
         * movieId: id of the movie we change
         * newPrice: new price string
         */
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session

            switch (movieType) {
                case "LinkMovie":
                    LinkMovie linkMovie = session.get(LinkMovie.class, movieId);
                    linkMovie.setPrice(newPrice);
                    System.out.format("Updated movie %s show times from %s to %s.\n",
                            linkMovie.getMovieTitle().getEnglishName(), linkMovie.getPrice(), newPrice);
                    session.update(linkMovie);
                case "Screening":
                    Screening screening = session.get(Screening.class, movieId);
                    screening.setPrice(newPrice);
                    System.out.format("Updated movie %s show times from %s to %s.\n",
                            screening.getMovieTitle().getEnglishName(), screening.getPrice(), newPrice);
                    session.update(screening);
                default:
                    System.err.println("Received " + movieType + " as movieType in changePrice when expected either" +
                            "Screening or LinkMovie. No change has been made.");
            }

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

    public void postStagedPriceChanges(ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<StagedPriceChange> priceChangeList = getAll(StagedPriceChange.class);
            session.flush();
            session.getTransaction().commit();
            SendPriceChangesList sendList = new SendPriceChangesList(priceChangeList);
            try {
                client.sendToClient(sendList);
                System.out.println("Sent staged price changes list to client " + client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Could not get staged prices, changes have been rolled back.");
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

    public void addMovieTitle(String hebrewName, String englishName, String genres, String producer, String actor,
                              String movieDescription, String imagePath, String year) {
        /**
         * movieInfo: all of the movie information compiled into a string array, ordered as:
         *            hebrewName, englishName, genres, producer, actor, movieDescription, imagePath, year
         */
        try {
            MovieTitle movie = new MovieTitle(hebrewName, englishName, genres, producer, actor, movieDescription, imagePath, year);

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
            session.beginTransaction();
            // Deleting the movie and its associated entries.

            // Currently fails if there exists a purchases constraint.
            // A possible fix is to add movie title id to each purchase as a separate field and to delete by it.
            // Make sure to see if compensations are in order when canceling shows, even whereby title deletion.
            try {
                deleteMovieQuery("ComingSoonMovie", "movieTitleId", movieTitleId);
                session.flush();
            } catch (Exception e) {
            }
            try {
                deleteMovieQuery("LinkMovie", "movieTitleId", movieTitleId);
                session.flush();
            } catch (Exception e) {
            }
            try {
                deleteMovieQuery("Screening", "movieTitleId", movieTitleId);
                session.flush();
            } catch (Exception e) {
            }
            deleteMovieQuery("MovieTitle", "movieId", movieTitleId);
            session.flush();
            session.getTransaction().commit();

            System.out.format("Deleted movie with ID %s from the database.\n", movieTitleId);

        } catch (Exception e) {
            System.err.println("Could not delete the movie. Changes have been rolled back. Are there linked purchases?");
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

    public void addScreening(int movieTitleId, String price, String time, String location, String rows, String columns) {
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

    public void updateAllClientsMovieList(SimpleServer server) {
        /**
         * server: to be able to send the movies to everyone.
         */
        try {
            server.sendToAllClients(new ForceClear());
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<MovieTitle> movies = getAll(MovieTitle.class);
            List<ComingSoonMovie> comingSoonMovies = getAll(ComingSoonMovie.class);
            List<LinkMovie> linkMovies = getAll(LinkMovie.class);
            List<Screening> screenings = getAll(Screening.class);
            session.getTransaction().commit();

            for (MovieTitle movie : movies) { // Send each movie every client.
                server.sendToAllClients(movie);
            }

            for (ComingSoonMovie comingSoonMovie : comingSoonMovies) { // Send each movie to the client.
                server.sendToAllClients(comingSoonMovie);
            }

            for (LinkMovie linkMovie : linkMovies) { // Send each movie to the client.
                server.sendToAllClients(linkMovie);
            }

            for (Screening screening : screenings) { // Send each movie to the client.
                server.sendToAllClients(screening);
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
        System.out.format("Updated all client's movie list!\n");
    }

    public void handleStagedChange(String movieType, String movieId, String fieldAfterChange) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session

            // Remove a staged change if there exists one
            String hql = String.format("DELETE FROM StagedPriceChange WHERE movieType=:movieType AND movieId=:movieId");
            Query query = session.createQuery(hql);
            query.setParameter("movieType", movieType);
            query.setParameter("movieId", Integer.parseInt(movieId)); // To prevent SQL injections ;)
            query.executeUpdate();

            if (!fieldAfterChange.equals("cancel")) {
                // Add a staged change
                StagedPriceChange stagedPriceChange = new StagedPriceChange(movieType, Integer.parseInt(movieId), fieldAfterChange);
                session.save(stagedPriceChange);
                System.out.format("Added a staged change: change %s with ID %s to %s\n", movieType, movieId, fieldAfterChange);
            } else {
                // Server notifies about the removal (that was made anyway)
                System.out.format("Removed staged change where movieType=%s and movieId=%s", movieType, movieId);
            }
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (fieldAfterChange.equals("cancel")) {
                System.err.println("Could not delete staged change. Changes have been rolled back.");
            } else {
                System.err.println("Could not add a staged change. Changes have been rolled back.");
            }
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

    public void addComplaint(String name, String time, String content) {
        try {
            Complaint complaint = new Complaint(name, time, content);

            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session

            // Add the complaint to the database
            session.save(complaint);
            session.flush();
            session.getTransaction().commit();
            System.out.format("Added complaint to database: ");
        } catch (Exception e) {
            System.err.println("error");
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

    public void ShowAllComplaints(ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<Complaint> complaints = getAll(Complaint.class);
            session.getTransaction().commit();
            String msg = "#ShowAllComplaints#";
            for (Complaint com : complaints) {
                if (com.getResult() == 3) {
                    msg += com.getComplaintId();
                    msg += "\t";
                    msg += com.getTime_registration();
                    msg += "\t";
                    msg += "###";
                }
            }
            try {
                client.sendToClient(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Could not get complaints list, changes have been rolled back.");
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

    public void ShowComplaintByID(ConnectionToClient client, String str) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<Complaint> complaints = getAll(Complaint.class);
            int id = Integer.parseInt(str);
            int testindex = 0;
            for (Complaint cmpl : complaints) {
                if (cmpl.getComplaintId() == id) {
                    testindex = complaints.indexOf(cmpl);
                }
            }

            Complaint complaint = complaints.get(testindex);
            String test = "#ShowComplaint\t" + id + "\t" + complaint.getCustomer_name() + "\t" + complaint.getComplaint_details();
            session.getTransaction().commit();
            try {
                client.sendToClient(test);

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            System.err.println("Could not get complaint details, changes have been rolled back.");
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

    public void updateComplaint(int id, String result, int refund) {

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<Complaint> complaints = getAll(Complaint.class);
            int testindex = 0;
            for (Complaint cmpl : complaints) {
                if (cmpl.getComplaintId() == id) {
                    testindex = complaints.indexOf(cmpl);
                }
            }
            Complaint complaint = complaints.get(testindex);
            complaint.setRefunded(refund);
            complaint.setResult(result);
            // Add the complaint to the database
            session.save(complaint);
            session.flush();
            session.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("Could not get complaint details, changes have been rolled back.");
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
}
