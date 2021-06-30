/**
 * Sample Skeleton for 'file_complaint.fxml' Controller Class
 */

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

public class FileComplaintController {

    @FXML
    public void initialize() {
        // Register to EventBus so we can subscribe to events when a movie is sent over by the server.
        EventBus.getDefault().register(this);
        App.getApp_stage().setTitle("הגשת תלונה");
    }

    public void shutdown() {
        // cleanup code here: unregister from EventBus.
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void test(WarningEvent w){

    }

    @FXML // fx:id="name"
    private TextField name; // Value injected by FXMLLoader

    @FXML // fx:id="purchase_id"
    private TextField purchase_id; // Value injected by FXMLLoader

    @FXML // fx:id="complaint_content"
    private TextArea complaint_content; // Value injected by FXMLLoader

    @FXML // fx:id="complaint_button"
    private Button complaint_button; // Value injected by FXMLLoader

    @FXML // fx:id="complaint_button"
    private Button back; // Value injected by FXMLLoader




    @FXML
    void send_complain(ActionEvent event) {
        String[] time = LocalTime.now().toString().split(":",3);
        String regtime = LocalDate.now() + "_" + time[0]+":"+time[1];

//        System.out.println(regtime);
        if(name.getText().isEmpty() || complaint_content.getText().isEmpty()){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        String.format("One of the fields is empty!\n"));
                alert.show();
            });
        }
        else {
            String namecust = name.getText();
            name.clear();
            String content = complaint_content.getText();
            complaint_content.clear();

            purchase_id = null;
            String command = "#addComplaint\t"+namecust+"\t"+regtime+"\t"+content;
            // Send a command to the server.
            try {
                SimpleClient.getClient().sendToServer(command);
                System.out.println("sent to db");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }

    @FXML
    void BackTo() throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("screen_navigation");
    }

}