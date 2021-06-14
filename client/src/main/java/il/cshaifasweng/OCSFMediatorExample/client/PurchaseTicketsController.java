 /**
 * Sample Skeleton for 'purchase_tickets.fxml' Controller Class
 */
package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PurchaseTicketsController {
    @FXML // fx:id="selectedMovie"
    private ScrollPane selectedMovie; // Value injected by FXMLLoader

    @FXML // fx:id="movieList"
    private VBox movieList; // Value injected by FXMLLoader

    @FXML // fx:id="seatMap"
    private Pane seatMap; // Value injected by FXMLLoader

    @FXML // fx:id="nameTF"
    private TextField nameTF; // Value injected by FXMLLoader

    @FXML // fx:id="paymentInfoTF"
    private TextField paymentInfoTF; // Value injected by FXMLLoader

    @FXML // fx:id="confirmButton"
    private Button confirmButton; // Value injected by FXMLLoader

    @FXML
    void confirmPurchase(ActionEvent event) {
        //TODO: make sure all data was set and if so pass it on.
    }
}
