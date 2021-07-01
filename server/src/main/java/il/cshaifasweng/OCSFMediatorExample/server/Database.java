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
import java.time.LocalTime;
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
        configuration.addAnnotatedClass(CancelledPurchases.class);
        configuration.addAnnotatedClass(SystemUser.class);

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

        MovieTitle mt3 = new MovieTitle(
                "חלל מת 2",
                "Dead Space 2",
                "Sci-fi, Horror",
                "David Woldman",
                "Gunner Wright, Tanya Clarke",
                "Three years after the Necromorph infestation aboard the USS Ishimura, Isaac Clarke awakens from a coma, confused, disoriented, and on a space station called The Sprawl.",
                "posters/dead_space2.png",
                "2011"
        );

        MovieTitle mt4 = new MovieTitle(
                "סרט אפס",
                "Seret Efes",
                "Comedy",
                "אופיר כגן, עדי מורג, דניאל בוקס",
                "Or Paz, Tom Trager, Hadar Zusman",
                "A rude loud-mouthed Israeli tricks Comedy Central into giving him a movie budget which in return ruins the lives of everyone around him and descends into chaos.",
                "posters/seret_efes.png",
                "2015"
        );

        MovieTitle mt5 = new MovieTitle(
                "ממזרים חסרי כבוד",
                "Inglourious Basterds",
                "Action, Thriller",
                "Lawrence Bender",
                "Brad Pitt, Christoph Waltz, Michael Fassbender",
                "In Nazi-occupied France during World War II, a plan to assassinate Nazi leaders by a group of Jewish U.S. soldiers coincides with a theatre owner's vengeful plans for the same.",
                "posters/inglorious_bastards.png",
                "2009"
        );

        MovieTitle mt6 = new MovieTitle(
                "מכבי חיפה: מאה שנים ראשונות",
                "Maccabi Haifa: 100 first years",
                "Documentary",
                "Li-Mor Zucker",
                "Ya'akov Shahar, Uzi Mor, Yaniv Katan",
                "The history, the game, the colour. Maccabi Haifa is the greatest and most beloved football club in Israel.",
                "posters/Maccabi.png",
                "2018"
        );

        MovieTitle mt7 = new MovieTitle(
                "לה לה לנד",
                "La La Land",
                "Comedy, Drama, Musical, Romance",
                "Fred Berger, Jordan Horowitz, Gary Gilbert, Marc Platt",
                "Ryan Gosling, Emma Stone, Rosemarie DeWitt",
                "While navigating their careers in Los Angeles, a pianist and an actress fall in love while attempting to reconcile their aspirations for the future.",
                "posters/la_la_land.png",
                "2016"
        );

        Screening s1 = new Screening(mt1, "10", "10:00-12:30", "Haifa", 10, 10);
        Screening s2 = new Screening(mt1, "12", "10:00-12:30", "Haifa", 10, 8);

        session.save(s1);
        session.save(s2);

        SystemUser admin= new SystemUser("admin","111","Admin");
        SystemUser cnm= new SystemUser("michael","mmm","ContentManager");
        SystemUser ceo= new SystemUser("tomi","ttt","CEO");
        SystemUser csc= new SystemUser("liraz","lll","CustomerServiceClerk");
        SystemUser tm= new SystemUser("adi","aaa","TheaterManager", "Haifa");
        SystemUser tm1= new SystemUser("ADI","AAA","TheaterManager","Netanya");

        session.save(admin);
        session.save(cnm);
        session.save(ceo);
        session.save(csc);
        session.save(tm);
        session.save(tm1);

        ComingSoonMovie comingSoonMovie = new ComingSoonMovie(mt1, "30");

        LinkMovie linkMovie = new LinkMovie(mt2, "15", "cdnmovies.com/amazingmovie2", "11:00-15:00");

        session.save(mt1);
        session.save(mt2);
        session.save(mt3);
        session.save(mt4);
        session.save(mt5);
        session.save(mt6);
        session.save(mt7);
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
        int counter = 0;
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
                        counter++;
                        System.out.format("Sent subscription %d to client %s", sub.getSubscriptionId(), client.getInetAddress().getHostAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (counter == 0) {
                Warning warning = new Warning("Subscription doesn't exist or has no entries left.");
                client.sendToClient(warning);
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

    public void getSystemUser(String username, String password, ConnectionToClient client) {
        int counter = 0;
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<SystemUser> systemUsers = getAll(SystemUser.class);
            session.getTransaction().commit();

            for (SystemUser systemUser : systemUsers) {
                if ((systemUser.getSystemUsername().equals(username)) && (systemUser.getSystemPassword().equals(password))) {
                    client.sendToClient(systemUser);
                    counter++;
                    System.out.println("Sent system user " + systemUser.getSystemUsername() + " to client " + client.getInetAddress().getHostAddress());
                }
            }
            if (counter == 0) {
                Warning warning = new Warning("Administrative login doesn't exist, check the username or the password.");
                client.sendToClient(warning);
            }
        } catch (Exception e) {
            System.err.println("Could not get system user, changes have been rolled back.");
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

    public void subscriptionPayment(String full_name, int number) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<Subscription> subs = getAll(Subscription.class);

            for (Subscription sub : subs) {
                if (sub.getFull_name().equals(full_name)) {
                    sub.setEntries_left(sub.getEntries_left() - number);
                    System.out.format("Successfully decremented subscription entries");
                    if (sub.getEntries_left() != 0) {
                        session.update(sub);
                    } else {
                        session.delete(sub);
                    }
                    session.flush();
                    session.getTransaction().commit();
                }
            }
        } catch (Exception e) {
            System.err.println("Could not decrement subscription entries, changes have been rolled back.");
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

            System.out.println("Taken seat");
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

    public void purpleOutlineRemove(int Y) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<Screening> screenings = getAll(Screening.class);
            session.flush();
            session.getTransaction().commit();

            for (Screening screening : screenings) {
                int X = screening.getColumns() * screening.getRows();
                int purchasableTickets = -(X - screening.getAvailableSeats()); // Taken seats

                // calculate purchasable tickets
                if (X > 1.2 * Y) {
                    purchasableTickets += Y;
                } else if (X > 0.8 * Y) {
                    purchasableTickets += Math.floor(0.8 * Y);
                } else {
                    purchasableTickets += Math.floor(0.5 * X);
                }

                // if sold more than available, cancel screening.
                if (purchasableTickets < 0) {
                    removeScreening(screening.getScreeningId());
                }
            }
        } catch (Exception e) {
            System.err.println("Could not remove screenings, changes have been rolled back.");
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
            System.out.println("Added subscription.");
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
            Purchase p = new Purchase(name, payInfo, dtf.format(now), grandTotal, takenSeats, screening.getMovieTitle(), null, movieDetail, screening);

            session.save(p);
            session.flush();
            session.getTransaction().commit();
            System.out.println("Added purchase.");
            client.sendToClient(p);
            System.out.println("Sent purchase to client " + client.getInetAddress().getHostAddress());
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
            System.out.println("Added link purchase");
            client.sendToClient(p);
            System.out.println("Sent purchase to client " + client.getInetAddress().getHostAddress());
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
            System.out.println("Added subscription purchase.");
            client.sendToClient(p);
            System.out.println("Sent purchase to client " + client.getInetAddress().getHostAddress());
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


            List<Purchase> purchases = getAll(Purchase.class);
            for (Purchase p : purchases) {
                if (p.getMovie_link().getMovieId() == linkMovieId) {


                    CancelledPurchases cp = new CancelledPurchases(p.getPurchaseId(), p.getPrice(), "cancelled", p.getMovieDetail(), p.getPayment_info(), p.getCustomer_name(), p.getPurchase_time());

                    session.delete(p);
                    session.save(cp);
                    session.flush();

                }
            }


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

            List<Purchase> purchases = getAll(Purchase.class);
            for (Purchase p : purchases) {
                if (p.getScreening().getScreeningId() == screeningId) {

                    CancelledPurchases cp = new CancelledPurchases(p.getPurchaseId(), p.getPrice(), "cancelled", p.getMovieDetail(), p.getPayment_info(), p.getCustomer_name(), p.getPurchase_time());
                    session.delete(p);
                    session.save(cp);
                    session.flush();

                }
            }


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
//            int testindex = 0;
//            for (Complaint cmpl : complaints) {
//                if (cmpl.getComplaintId() == id) {
//                    testindex = complaints.indexOf(cmpl);
//                }
//            }

            Complaint complaint = session.get(Complaint.class, id);

//            Complaint complaint = complaints.get(testindex);
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
//            List<Complaint> complaints = getAll(Complaint.class);
//            int testindex = 0;
//            for (Complaint cmpl : complaints) {
//                if (cmpl.getComplaintId() == id) {
//                    testindex = complaints.indexOf(cmpl);
//                }
//            }
//            Complaint complaint = complaints.get(testindex);
            Complaint complaint = session.get(Complaint.class, id);
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

    public void cancelLinkPurchase(String name, String payment, int id, ConnectionToClient client) {

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            String msg = "";

            Purchase purchase = (Purchase) session.get(Purchase.class, id);
            if (purchase == null) {
                msg = "#cancelorder\t" + "There is no purchase with such ID!";

            } else if (!name.equals(purchase.getCustomer_name())) {
                msg = "#cancelorder\t" + "invalid name ";

            } else if (!payment.equals(purchase.getPayment_info().substring(purchase.getPayment_info().length() - 4))) {
                msg = "#cancelorder\t" + "invalid payment details";

            } else if (purchase.getMovie_link() == null) {
                msg = "#cancelorder\t" + "no link was purchased";

            } else {
                String linktime = purchase.getMovie_link().getWatchHours().split("-")[0];
//                        String linktime = purchase.getMovieDetail();
//                        msg = "#cancelorder\t" + linktime;
                int link_hours = Integer.parseInt(linktime.split(":")[0]);
                int link_minutes = Integer.parseInt(linktime.split(":")[1]);

                String[] curtime = LocalTime.now().toString().split(":");
                int cur_hours = Integer.parseInt(curtime[0]);
                int cur_minutes = Integer.parseInt(curtime[1]);
//                    int cur_hours = 7;
//                    int cur_minutes = 34;

                int refunded = 0;
                int diff = (link_hours - cur_hours) * 60 + (link_minutes - cur_minutes);
                if (diff >= 60) {
                    refunded = purchase.getPrice() / 2;
                    msg = "#cancelorder\t" + "So pity! You're refunded for " + refunded + "shekels\n";
                } else {
                    msg = "#cancelorder\t" + "You cancelled your order, but no refund\n";


                }

//                    purchase.setStatus("returned");
                CancelledPurchases cancelledPurchases = new CancelledPurchases(purchase.getPurchaseId(), refunded, "returned", purchase.getMovieDetail(), purchase.getPayment_info(), purchase.getCustomer_name(), purchase.getPurchase_time());

//                    session.save(purchase); // to delete
                session.delete(purchase);
                session.save(cancelledPurchases);
                session.flush();
//                    msg = "#cancelorder\t" + name + "\t" + payment + "\t" + id;
            }

            session.getTransaction().commit();

            try {
                client.sendToClient(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            System.err.println("Could not get purchase details, changes have been rolled back.");
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

    public void cancelTicketPurchase(String name, String payment, int id, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            String msg = "";

            Purchase purchase = (Purchase) session.get(Purchase.class, id);
            if (purchase == null) {
                msg = "#cancelorder\t" + "There is no purchase with such ID!";

            } else if (!name.equals(purchase.getCustomer_name())) {
                msg = "#cancelorder\t" + "invalid name ";

            } else if (!payment.equals(purchase.getPayment_info().substring(purchase.getPayment_info().length() - 4))) {
                msg = "#cancelorder\t" + "invalid payment details";

            } else if (purchase.getScreening() == null) {
                msg = "#cancelorder\t" + "no ticket was purchased";

            } else {
                String tickettime = purchase.getScreening().getTime().split("-")[0];
                int link_hours = Integer.parseInt(tickettime.split(":")[0]);
                int link_minutes = Integer.parseInt(tickettime.split(":")[1]);

                String[] curtime = LocalTime.now().toString().split(":");
                int cur_hours = Integer.parseInt(curtime[0]);
                int cur_minutes = Integer.parseInt(curtime[1]);
//                int cur_hours = 4;
//                int cur_minutes = 34;

                String seats = purchase.getSeats();
                String takenseats = purchase.getScreening().getTakenSeats();
                int s = 0;
                for (String seat : seats.split("[()]")) {
//                    System.out.print(splitString + " ");
                    takenseats = takenseats.replace("(" + seat + ")", "");
                    s++;
                }


                int refunded = 0;
                int diff = (link_hours - cur_hours) * 60 + (link_minutes - cur_minutes);
                if (diff >= 180) {
                    refunded = purchase.getPrice();
                    msg = "#cancelorder\t" + "So pity! You're refunded for " + refunded + "shekels\n";
                } else if (diff >= 60 && diff < 180) {
                    refunded = purchase.getPrice() / 2;
                    msg = "#cancelorder\t" + "So pity! You're refunded for " + refunded + "shekels\n";
                } else {
                    msg = "#cancelorder\t" + "You cancelled your order, but no refund\n";


                }

                Screening screening = purchase.getScreening();
                screening.setTakenSeats(takenseats);
                screening.setAvailableSeats(screening.getAvailableSeats() + s / 2);
//                purchase.setStatus("returned");
                CancelledPurchases cancelledPurchases = new CancelledPurchases(purchase.getPurchaseId(), refunded, "returned", purchase.getMovieDetail(), purchase.getPayment_info(), purchase.getCustomer_name(), purchase.getPurchase_time());

//                session.save(purchase);
                session.delete(purchase);
                session.save(cancelledPurchases);
                session.save(screening);
                session.flush();
            }


            session.getTransaction().commit();

            try {
                client.sendToClient(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Could not get purchase details, changes have been rolled back.");
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

    public void postPurchasesReport(ConnectionToClient client, String ReportName) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<Purchase> purchases = getAll(Purchase.class);
            session.flush();
            session.getTransaction().commit();
            PurchaseReport purchasesReport = new PurchaseReport(purchases, ReportName);
            try {
                client.sendToClient(purchasesReport);
                System.out.println("Sent purchases list for the purchase related reports to client " + client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Could send ticket report, changes have been rolled back.");
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

    public void postRefundReport(ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<CancelledPurchases> refundList = getAll(CancelledPurchases.class);
            session.flush();
            session.getTransaction().commit();
            RefundsReport report = new RefundsReport(refundList);
            try {
                client.sendToClient(report);
                System.out.println("Sent refunds list for the refund report to client " + client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Could send refund report, changes have been rolled back.");
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

    public void postComplaintReport(ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction(); // Begin a new DB session
            List<Complaint> complaints = getAll(Complaint.class);
            session.flush();
            session.getTransaction().commit();
            ComplaintReport report = new ComplaintReport(complaints);
            try {
                client.sendToClient(report);
                System.out.println("Sent complaint list for the complaint report to client " + client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Could send complaint report, changes have been rolled back.");
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
