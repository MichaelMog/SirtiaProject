package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieTitle;
import il.cshaifasweng.OCSFMediatorExample.entities.Screening;

import java.io.IOException;

public class TheBooth {

    public void addSubscription(String full_name){
        sendCommand("#addSubscription\t"+ full_name);
    }

    public void getSubscription(String full_name){
        sendCommand("#getSubscription\t"+ full_name);
    }

    public void takeSeat(Screening screening, int row, int column){
        sendCommand("#takeSeat\t"+ screening.getScreeningId()+"\t("+row+","+column+")");
    }





    void sendCommand(String command) {
        // Send a command to the server.
        try {
            SimpleClient.getClient().sendToServer(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
