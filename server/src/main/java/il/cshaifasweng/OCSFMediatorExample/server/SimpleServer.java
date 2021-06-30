package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

import java.util.Arrays;
import java.util.List;


public class SimpleServer extends AbstractServer {
    Database db = new Database();

    public SimpleServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        String msgString = msg.toString();

        // Display a warning with a timestamp.
        // Use this as a template for sending messages as pop-ups across! ^.^
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
            db.showMovies(client);
        }

        if (msgString.startsWith("#takeSeat")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            int screeningId = Integer.parseInt(params.get(1));
            String seat = params.get(2);
            db.addTakenSeat(screeningId, seat);
        }

        if ((msgString.startsWith("#addSubscription")) && (!(msgString.startsWith("#addSubscriptionPurchase")))) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            String full_name = params.get(1);
            db.addSubscription(full_name);
        }

        if (msgString.startsWith("#getSysetmUser")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            String username = params.get(1);
            String password = params.get(2);
            db.getSystemUser(username, password, client);
        }

        if (msgString.startsWith("#getSubscription")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            String full_name = params.get(1);
            db.getSubscription(full_name, client);
        }

        if (msgString.startsWith("#subscriptionPayment")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            String full_name = params.get(1);
            int number = Integer.parseInt(params.get(2));
            db.subscriptionPayment(full_name, number);
        }

        if (msgString.startsWith("#addPurchase")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            String name = params.get(1);
            String payInfo = params.get(2);
            String takenSeats = params.get(3);
            int grandTotal = Integer.parseInt(params.get(4));
            int screening_id = Integer.parseInt(params.get(5));
            db.addPurchase(name, payInfo, takenSeats, grandTotal, screening_id, client);
        }

        if (msgString.startsWith("#addLinkPurchase")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            String name = params.get(1);
            String payInfo = params.get(2);
            int grandTotal = Integer.parseInt(params.get(3));
            int link_id = Integer.parseInt(params.get(4));
            db.addLinkPurchase(name, payInfo, grandTotal, link_id, client);
        }

        if (msgString.startsWith("#addSubscriptionPurchase")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            String name = params.get(1);
            String payInfo = params.get(2);
            int grandTotal = Integer.parseInt(params.get(3));
            db.addSubscriptionPurchase(name, payInfo, grandTotal, client);
        }

        // Change show times of a movie.
        // Command syntax (tab-separated): #changeShowTimes movieId  newShowTimes
        /*if (msgString.startsWith("#changeShowTimes\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            int movieId = Integer.parseInt(params.get(1));
            String newShowTimes = params.get(2);
            db.changeShowTimes(movieId, newShowTimes);
        }*/

        // Request a price change.
        // Command syntax (tab-separated): #requestPriceChange  movieType   movieId price
        // Where movieType is either "LinkMovie" or "Screening",
        // price is either a number (as string) or "cancel" - to cancel any previous request.
        if (msgString.startsWith("#requestPriceChange\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.handleStagedChange(params.get(1), params.get(2), params.get(3));
            /* Optional implementation: create entity class StagedPriceChanges, if you get a request you update the
             *  corresponding type-id inside the database, if you get a cancel you delete that row. When the manager logs
             *  on and wants to decide which to accept, she may pull out the stages prices from the database in a similar
             *  fashion to how the movies are pulled out and have a decide button - that makes an accept, reject and
             *  cancel alert pop up when clicked on. If accepted we update the row, then we delete from staged prices.
             */
        }

        // Make a time change.
        // Command syntax (tab-separated): #timeChange  movieType   movieId showTimes
        // Where movieType is either "LinkMovie" or "Screening",
        // showTimes is either a string or "cancel" - to cancel any previous request.
        if (msgString.startsWith("#timeChange\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.changeTime(params.get(1), Integer.parseInt(params.get(2)), params.get(3));
            db.updateAllClientsMovieList(this);
        }

        // Make a price change.
        // Command syntax (tab-separated): #priceChange  movieType   movieId newPrice
        // Where movieType is either "LinkMovie" or "Screening",
        // newPrice is a string.
        if (msgString.startsWith("#priceChange\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.changePrice(params.get(1), Integer.parseInt(params.get(2)), params.get(3));
            db.updateAllClientsMovieList(this);
        }

        // Return list of all staged price changes.
        // Command syntax: #getStagedPriceChanges
        if (msgString.startsWith("#getStagedPriceChanges")) {
            db.postStagedPriceChanges(client);
        }

        // Add a movie title.
        // Command syntax (tab-separated): #addMovieTitle    hebrewName  englishName genres  producer    actor   movieDescription    imagePath  year
        if (msgString.startsWith("#addMovieTitle\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));

            db.addMovieTitle(params.get(1), params.get(2), params.get(3), params.get(4), params.get(5),
                    params.get(6), params.get(7), params.get(8));
            db.updateAllClientsMovieList(this);
        }

        // Add a coming soon movie.
        // Command syntax (tab-separated): #addComingSoonMovie  movieTitleId    price
        if (msgString.startsWith("#addComingSoonMovie\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.addComingSoonMovie(Integer.parseInt(params.get(1)), params.get(2));
            db.updateAllClientsMovieList(this);
        }

        // Add a link movie.
        // Command syntax (tab-separated): #addLinkMovie  movieTitleId    price link    watchHours
        if (msgString.startsWith("#addLinkMovie\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.addLinkMovie(Integer.parseInt(params.get(1)), params.get(2), params.get(3), params.get(4));
            db.updateAllClientsMovieList(this);
        }

        // Add a screening.
        // Command syntax (tab-separated): #addLinkMovie  movieTitleId    price time   location   rows   columns
        if (msgString.startsWith("#addScreening\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.addScreening(Integer.parseInt(params.get(1)), params.get(2), params.get(3), params.get(4), params.get(5),
                    params.get(6));
            db.updateAllClientsMovieList(this);
        }

        // Remove a movie title.
        // Command syntax (tab-separated): #removeMovieTitle    movieId
        if (msgString.startsWith("#removeMovieTitle\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.removeMovieTitle(Integer.parseInt(params.get(1)));
            db.updateAllClientsMovieList(this);
        }

        // Remove a coming soon movie.
        // Command syntax (tab-separated): #removeMovieTitle    movieId
        if (msgString.startsWith("#removeComingSoonMovie\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.removeComingSoonMovie(Integer.parseInt(params.get(1)));
            db.updateAllClientsMovieList(this);
        }

        // Remove a link movie.
        // Command syntax (tab-separated): #removeMovieTitle    movieId
        if (msgString.startsWith("#removeLinkMovie\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.removeLinkMovie(Integer.parseInt(params.get(1)));
            db.updateAllClientsMovieList(this);

        }

        // Remove a screening.
        // Command syntax (tab-separated): #removeMovieTitle    movieId
        if (msgString.startsWith("#removeScreening\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.removeScreening(Integer.parseInt(params.get(1)));
            db.updateAllClientsMovieList(this);
        }

        //Add a complaint to database
        if (msgString.startsWith("#addComplaint\t")) {
            String[] str = msgString.split("\t");
            db.addComplaint(str[1], str[2], str[3]);

        }

        // get list of all the complaints
        if (msgString.startsWith("#ShowAllComplaints")) {
            db.ShowAllComplaints(client);
        }

        // show the contnent of complaint
        if (msgString.startsWith("#ShowComplaintMore")) {
            String[] s = msgString.split("\t");
//            System.out.println(s[1]);
            db.ShowComplaintByID(client, s[1]);
        }


        // update complaint(resolve,reject, refund)
        if (msgString.startsWith("#UpdateComplaint")) {

            String[] s = msgString.split("\t");
            db.updateComplaint(Integer.parseInt(s[1]), s[2], Integer.parseInt(s[3]));
        }

        //cancel purchase of the movie link
        if (msgString.startsWith("#cancelLink")) {
            String[] s = msgString.split("\t");

            db.cancelLinkPurchase(s[1], s[2], Integer.parseInt(s[3]), client);
        }

        if (msgString.startsWith("#cancelTicket")) {
            String[] s = msgString.split("\t");

            db.cancelTicketPurchase(s[1], s[2], Integer.parseInt(s[3]), client);
        }

    }
}
