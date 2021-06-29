package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ReportsController {

    @FXML // fx:id="TicketReport"
    private Button TicketReport; // Value injected by FXMLLoader

    @FXML // fx:id="SubscriptionReport"
    private Button SubscriptionReport; // Value injected by FXMLLoader

    @FXML // fx:id="RefundsReport"
    private Button RefundsReport; // Value injected by FXMLLoader

    @FXML // fx:id="ComplaintsHistogram"
    private Button ComplaintsHistogram; // Value injected by FXMLLoader

    @FXML // fx:id="BackButton"
    private Button BackButton; // Value injected by FXMLLoader

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
    }
    
    @FXML
    void GoBack(ActionEvent event) throws IOException{
    	EventBus.getDefault().unregister(this);
    	App.setRoot("screen_navigation");
    }

    @FXML
    void ShowComplaintReport(ActionEvent event) throws IOException{
    	sendCommand("#showReports");
    	EventBus.getDefault().unregister(this);
    	App.setRoot("scroll_report");
    }

    @FXML
    void ShowRefundsReport(ActionEvent event) throws IOException{
    	sendCommand("#showReports");
    	EventBus.getDefault().unregister(this);
    	App.setRoot("scroll_report");
    }

    @FXML
    void ShowTicketReport(ActionEvent event) throws IOException{
    	sendCommand("#showReports");
    	EventBus.getDefault().unregister(this);
    	App.setRoot("scroll_report");
    }

    @FXML
    void showShowLinksReport(ActionEvent event) throws IOException{
    	sendCommand("#showReports");
    	EventBus.getDefault().unregister(this);
    	App.setRoot("histogram_report");
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
