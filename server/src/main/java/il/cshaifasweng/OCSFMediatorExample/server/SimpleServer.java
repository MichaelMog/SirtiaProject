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

        if(msgString.startsWith("#takeSeat")){
            List<String> params = Arrays.asList(msgString.split("\t"));
            int screeningId = Integer.parseInt(params.get(1));
            String seat = params.get(2);
            db.addTakenSeat(screeningId, seat);
        }

        if(msgString.startsWith("#addSubscription")){
            List<String> params = Arrays.asList(msgString.split("\t"));
            String full_name =params.get(1);
            db.addSubscription(full_name);
        }

        if(msgString.startsWith("#getSubscription")){
            List<String> params = Arrays.asList(msgString.split("\t"));
            String full_name =params.get(1);
            db.getSubscription(full_name, client);
        }

        // Change show times of a movie.
        // Command syntax (tab-separated): #changeShowTimes movieId  newShowTimes
        /*if (msgString.startsWith("#changeShowTimes\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            int movieId = Integer.parseInt(params.get(1));
            String newShowTimes = params.get(2);
            db.changeShowTimes(movieId, newShowTimes);
        }*/

        // Add a movie title.
        // Command syntax (tab-separated): #addMovieTitle    hebrewName  englishName genres  producer    actor   movieDescription    imagePath   showTimes
        if (msgString.startsWith("#addMovieTitle\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));

            db.addMovieTitle(params.get(1), params.get(2), params.get(3), params.get(4), params.get(5),
                    params.get(6), params.get(7), params.get(8));
        }

        // Add a coming soon movie.
        // Command syntax (tab-separated): #addComingSoonMovie  movieTitleId    price
        if (msgString.startsWith("#addComingSoonMovie\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.addComingSoonMovie(Integer.parseInt(params.get(1)), params.get(2));
        }

        // Add a link movie.
        // Command syntax (tab-separated): #addLinkMovie  movieTitleId    price link    watchHours
        if (msgString.startsWith("#addLinkMovie\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.addLinkMovie(Integer.parseInt(params.get(1)), params.get(2), params.get(3), params.get(4));
        }

        // Remove a movie title.
        // Command syntax (tab-separated): #removeMovieTitle    movieId
        if (msgString.startsWith("#removeMovieTitle\t")) {
            List<String> params = Arrays.asList(msgString.split("\t"));
            db.removeMovieTitle(Integer.parseInt(params.get(1)));
        }
    }
}
