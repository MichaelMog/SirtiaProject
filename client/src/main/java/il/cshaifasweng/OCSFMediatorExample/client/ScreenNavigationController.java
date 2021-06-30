package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.SystemUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    @FXML // fx:id="managerScreenButton"
    private Button managerScreenButton; // Value injected by FXMLLoader

    @FXML // fx:id="covidButton"
    private Button covidButton; // Value injected by FXMLLoader

    @FXML // fx:id="complaintButton"
    private Button complaintButton; // Value injected by FXMLLoader

    @FXML // fx:id="handleComplaintsButton"
    private Button handleComplaintsButton; // Value injected by FXMLLoader

    @FXML // fx:id="cancelPurchaseButton"
    private Button cancelPurchaseButton; // Value injected by FXMLLoader

    @FXML // fx:id="YTF"
    private TextField YTF; // Value injected by FXMLLoader

    @FXML
    void EnableCovidRestrictions(ActionEvent event) {
        if (YTF.isVisible()) {
            if (YTF.getText().equals("disable")) {
                App.setPurpleOutline(false);
            } else if (!(YTF.getText().equals(""))) {
                App.setPurpleOutline(true);
                App.setY(Integer.parseInt(YTF.getText()));
                // TODO: cancel screenings that aren't available.
            }
            YTF.setVisible(false);
        } else {
            YTF.setVisible(true);
        }
    }

    @FXML
    void managerScreen(ActionEvent event) throws IOException {
        App.setRoot("manager_screen");
    }

    @FXML
    private TextField subTF;

    @FXML
    private Button subButton;

    @FXML // fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader

    private static SystemUser sysUser = null;

    @FXML
    void backToStart(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        sysUser = null;
        App.setRoot("login");
    }

    @FXML
    void initialize() {

        EventBus.getDefault().register(this);

        App.getAppStage().setWidth(655);
        App.getAppStage().setHeight(518);
        App.getAppStage().setTitle("הסרטייה");

        if (sysUser == null) {
            sysUser = (SystemUser) App.getAppStage().getUserData();
        }

        if ((sysUser.getSystemOccupation().equals("guest"))) {
            updateContentButton.setVisible(true);
            viewReportsButton.setVisible(true);
            managerScreenButton.setVisible(true);
            covidButton.setVisible(true);
        }

        // TODO: add cases of administrative occupations like "guest" case above.

    }


    @FXML
    void checkSub(ActionEvent event) {
        TheBooth.getSubscription(subTF.getText());
    }

    @Subscribe
    public void payWithSubscription(SubscriptionEvent event) {
        subTF.setText("Entries left in subscription: " + event.getSubscription().getEntries_left());
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
        App.getAppStage().setUserData(null);
        App.setRoot("purchase");
    }

    @FXML
    void viewReportsScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("show_reports");
    }

    @FXML
    void fileComplaintScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("file_complaint");
    }

    @FXML
    void handleComplaintsScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("handle_complaints");
    }

    @FXML
    void cancelPurchaseScreen(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("cancel_purchase");
    }

    public void shutdown() {
        // Does nothing. Merely here to make sure all fxml controller classes have a shutdown method.
    }
}

