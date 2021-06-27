package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;

import java.io.IOException;

public class TheBooth {

    // send a command to the database to add a new subscription with the given name.
    public static void addSubscription(String full_name) {
        sendCommand("#addSubscription\t" + full_name);
    }

    // send a command to the database to send the subscription with the given name.
    public static void getSubscription(String full_name) {
        sendCommand("#getSubscription\t" + full_name);
    }

    // send a command to the database to add a new purchase
    public static void addPurchase(String name, String payInfo, String takenSeats, int GrandTotal, int screening_id) {
        sendCommand("#addPurchase\t" + name + "\t" + payInfo + "\t" + takenSeats + "\t" + GrandTotal + "\t" + screening_id + "\t");
    }

    // send a command to the database to add a new link purchase
    public static void addLinkPurchase(String name, String payInfo, int GrandTotal, int link_id) {
        sendCommand("#addLinkPurchase\t" + name + "\t" + payInfo + "\t" + GrandTotal + "\t" + link_id + "\t");
    }

    // send a command to the database to flag the given seat as taken.
    public static void takeSeat(Screening screening, int row, int column) {
        sendCommand("#takeSeat\t" + screening.getScreeningId() + "\t(" + row + "," + column + ")");
    }

    public static void sendCommand(String command) {
        // Send a command to the server.
        try {
            SimpleClient.getClient().sendToServer(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
