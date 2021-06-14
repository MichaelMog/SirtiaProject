package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;

import java.io.IOException;

public class TheBooth {

    public static void addSubscription(String full_name){
        sendCommand("#addSubscription\t"+ full_name);
    }

    public static void getSubscription(String full_name){
        sendCommand("#getSubscription\t"+ full_name);
    }

    public static void takeSeat(Screening screening, int row, int column){
        sendCommand("#takeSeat\t"+ screening.getScreeningId()+"\t("+row+","+column+")");
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
