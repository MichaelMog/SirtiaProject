package il.cshaifasweng.OCSFMediatorExample.client; /**
 * Sample Skeleton for 'cancel_purchase.fxml' Controller Class
 */

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class CancelPurchaseController {


    @FXML
    public void initialize() {
        // Register to EventBus so we can subscribe to events when a movie is sent over by the server.
        App.getApp_stage().setTitle("Cancel Purchase");
        EventBus.getDefault().register(this);
    }

    public void shutdown() {
        // cleanup code here: unregister from EventBus.
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void test(MessageEvent w){

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING,
//                    String.format(w.getMsg()));
                    String.format(w.getMsg()));

            alert.show();
        });


    }


    @FXML // fx:id="cust_name"
    private TextField cust_name; // Value injected by FXMLLoader

    @FXML // fx:id="pay_details"
    private TextField pay_details; // Value injected by FXMLLoader

    @FXML // fx:id="choise_box"
    private ChoiceBox<?> choise_box; // Value injected by FXMLLoader

    @FXML // fx:id="purchase_id"
    private TextField purchase_id; // Value injected by FXMLLoader

    @FXML // fx:id="Cancel"
    private Button Cancel; // Value injected by FXMLLoader

    void popup(String message){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    String.format(message));
            alert.show();
        });
    }

    @FXML
    void CancelPurchase(ActionEvent event) {
            if(cust_name.getText().isEmpty() || pay_details.getText().isEmpty() || purchase_id.getText().isEmpty()){
                String message = "One of the fields is empty!\n";
                popup(message);
            }
            else if(pay_details.getText().length() != 4){
                String message = "4 digits! Only 4 digits!\n";
                popup(message);
            }
            else if(choise_box.getValue().equals("Cancel Subscription") ){
                String message = "Sorry, but You can not cancel your subscription!\n";
                popup(message);
            }

            else {
                String command = "";
                if(choise_box.getValue().equals("Cancel Ticket")){
                     command = "#cancelTicket\t";
                }
                else if(choise_box.getValue().equals("Cancel Link")){
                    command = "#cancelLink\t";
                }

                command += cust_name.getText()+"\t"+pay_details.getText()+"\t"+purchase_id.getText();
                // Send a command to the server.
                try {
                    SimpleClient.getClient().sendToServer(command);
                    System.out.println(command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cust_name.clear();
                pay_details.clear();
                purchase_id.clear();
            }
    }

}
