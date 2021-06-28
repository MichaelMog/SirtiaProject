package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Subscription;
import il.cshaifasweng.OCSFMediatorExample.entities.SystemUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private TextField subTF;

    @FXML
    private Button subButton;

    @FXML // fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader

    private SystemUser sysUser;

    @FXML
    void backToStart(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("login");
    }

    @FXML
    void initialize() {

        EventBus.getDefault().register(this);

        App.getApp_stage().setWidth(655);
        App.getApp_stage().setHeight(518);

        sysUser = (SystemUser) App.getApp_stage().getUserData();

        if(sysUser.getSystemOccupation().equals("guest")){
            updateContentButton.setVisible(false);
            viewReportsButton.setVisible(false);
        }

    }

    @FXML
    void checkSub(ActionEvent event) {
        TheBooth.getSubscription(subTF.getText());
    }

    @Subscribe
    public void payWithSubscription(SubscriptionEvent event) {

        EventBus.getDefault().unregister(this);
        subTF.setText("Entries left in subscription: "+event.getSubscription().getEntries_left());
    }

    @FXML
    void exploreMoviesScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("explore_movies");
    }

    @FXML
    void updateContentScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("update_content");
    }

    @FXML
    void purchaseSubscriptionScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.getApp_stage().setUserData(null);
        App.setRoot("purchase");
    }

    @FXML
    void viewReportsScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        //TODO: App.setRoot("your_fxml_here");
    }

    public void shutdown() {
        // Does nothing. Merely here to make sure all fxml controller classes have a shutdown method.
    }
}
