package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ScreenNavigationController {
    @FXML // fx:id="viewReportsButton"
    private Button viewReportsButton; // Value injected by FXMLLoader

    @FXML // fx:id="updateContentButton"
    private Button updateContentButton; // Value injected by FXMLLoader

    @FXML // fx:id="purchaseSubscriptionButton"
    private Button purchaseSubscriptionButton; // Value injected by FXMLLoader

    @FXML // fx:id="exploreMoviesButton"
    private Button exploreMoviesButton; // Value injected by FXMLLoader


    @FXML
    void exploreMoviesScreen(ActionEvent event) throws IOException {
        App.setRoot("explore_movies");
    }

    @FXML
    void updateContentScreen(ActionEvent event) throws IOException {
        App.setRoot("update_content");
    }

    @FXML
    void purchaseSubscriptionScreen(ActionEvent event) throws IOException {
        App.setRoot("purchase_subscription");
    }

    @FXML
    void viewReportsScreen(ActionEvent event) throws IOException {
        //TODO: App.setRoot("your_fxml_here");
    }

    public void shutdown() {
        // Does nothing. Merely here to make sure all fxml controller classes have a shutdown method.
    }
}
