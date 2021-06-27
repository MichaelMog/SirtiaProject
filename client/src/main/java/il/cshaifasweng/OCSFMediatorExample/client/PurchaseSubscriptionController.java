package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

public class PurchaseSubscriptionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane thePane;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField paymentInfoTF;

    @FXML
    private Button confirmButton;

    @FXML
    private Label grandTotalTF;

    private boolean run = false;
    int grandTotal = 499;

    public void shutdown() {
        // cleanup code here: unregister from EventBus.
        EventBus.getDefault().unregister(this);
    }

    @FXML
    void confirmPurchase(ActionEvent event) {

        // check legal name
        if (nameTF.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No name was given",
                    ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                return;
            }
        }

        // check legal payment option
        if (paymentInfoTF.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No payment information was given",
                    ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                return;
            }
        }

        // add purchase to database
        TheBooth.addSubscriptionPurchase(nameTF.getText(), paymentInfoTF.getText(), grandTotal);
        TheBooth.addSubscription(nameTF.getText());

        // go back to movie explorer
        Stage stage = (Stage) thePane.getScene().getWindow();
        stage.close();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("explore_movies.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("explore movies");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
