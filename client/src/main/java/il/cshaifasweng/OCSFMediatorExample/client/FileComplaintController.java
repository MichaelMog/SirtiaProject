/**
 * Sample Skeleton for 'file_complaint.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class FileComplaintController {

    @FXML
    public void initialize() {
        // Register to EventBus so we can subscribe to events when a movie is sent over by the server.
        App.getAppStage().setTitle("הגשת תלונה");
        App.getAppStage().setHeight(605);
        App.getAppStage().setWidth(640);
    }

    public void shutdown() {
        // cleanup code here
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
        String[] time = LocalTime.now().toString().split(":", 3);
        String regTime = LocalDate.now() + "_" + time[0] + ":" + time[1];

        if (name.getText().isEmpty() || complaint_content.getText().isEmpty()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        String.format("One of the fields is empty!\n"));
                alert.show();
            });
        } else {
            String customerName = name.getText();
            name.clear();
            String content = complaint_content.getText();
            complaint_content.clear();

            purchase_id = null;
            String command = "#addComplaint\t" + customerName + "\t" + regTime + "\t" + content;
            // Send a command to the server.
            try {
                SimpleClient.getClient().sendToServer(command);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING,
                            String.format("Your complaint was sent! It'll be resolved within 24 hours\n"));
                    alert.show();
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void BackTo() throws IOException {
        App.setRoot("screen_navigation");
    }
}