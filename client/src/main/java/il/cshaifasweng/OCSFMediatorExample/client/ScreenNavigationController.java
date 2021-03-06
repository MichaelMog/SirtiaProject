package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.SystemUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    @FXML // fx:id="disableCovidButton"
    private Button disableCovidButton; // Value injected by FXMLLoader

    @FXML // fx:id="enableCovidButton"
    private Button enableCovidButton; // Value injected by FXMLLoader

    @FXML // fx:id="YTextField"
    private TextField YTextField; // Value injected by FXMLLoader

    @FXML // fx:id="setYButton"
    private Button setYButton; // Value injected by FXMLLoader

    @FXML
    void EnableCovidRestrictions(ActionEvent event) {
        // Swap visibility of both buttons
        disableCovidButton.setVisible(!disableCovidButton.isVisible());
        enableCovidButton.setVisible(disableCovidButton.isVisible());
        if (enableCovidButton.isVisible() && App.isPurpleOutline()) {
            setYButton.setVisible(true);
            YTextField.setVisible(true);
        } else {
            setYButton.setVisible(false);
            YTextField.setVisible(false);
        }
    }


    @FXML
    void disableCovidRestrictions(ActionEvent event) {
        if (App.isPurpleOutline()) {
            // If covid restrictions were enabled - disable them.
            disableCovidButton.setStyle("-fx-background-color: #aadff2; "); // Set disable background color to blue.
            enableCovidButton.setStyle("-fx-background-color: #cccccc; "); // Set enable background color to gray.
            App.setPurpleOutline(false);
            setYButton.setVisible(false);
            YTextField.setVisible(false);
        }
    }

    @FXML
    void enableCovidRestrictions(ActionEvent event) {
        if (!App.isPurpleOutline()) {
            // If covid restrictions were disabled - enable them.
            disableCovidButton.setStyle("-fx-background-color: #cccccc; "); // Set disable background color to gray.
            enableCovidButton.setStyle("-fx-background-color: #aadff2; "); // Set enable background color to blue.
            App.setPurpleOutline(true);
            setYButton.setVisible(true);
            YTextField.setVisible(true);
        }
    }

    @FXML
    void setY(ActionEvent event) {
        try {
            App.setY(Integer.parseInt(YTextField.getText()));
            YTextField.clear();
            YTextField.setPromptText("Set to " + App.getY());
            TheBooth.purpleOutlineRemove(App.getY());
        } catch (NumberFormatException e) {
            System.err.println("Expecting to receive an integer Y value!");
            e.printStackTrace();
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
        sendCommand("#logUser\t"+sysUser.getSystemUserId()+"\toff");
        sysUser = null;
        App.setRoot("login");
    }

    @FXML
    void initialize() {

        EventBus.getDefault().register(this);

        App.getAppStage().setWidth(655);
        App.getAppStage().setHeight(518);
        App.getAppStage().setTitle("??????????????");
        
        if (App.isPurpleOutline()) {
            // If you exit from the screen with covid restrictions enabled then return the button colors reset.
            disableCovidButton.setStyle("-fx-background-color: #cccccc; "); // Set disable background color to gray.
            enableCovidButton.setStyle("-fx-background-color: #aadff2; "); // Set enable background color to blue.
        }

        if (sysUser == null) {
            sysUser = (SystemUser) App.getAppStage().getUserData();
            App.setSysUser(sysUser);
        }

        if ((sysUser.getSystemOccupation().equals("guest"))) {
            updateContentButton.setVisible(false);
            viewReportsButton.setVisible(false);
            managerScreenButton.setVisible(false);
            covidButton.setVisible(false);
            handleComplaintsButton.setVisible(false);
        } else {
            sendCommand("#logUser\t"+sysUser.getSystemUserId()+"\ton");
        }
        if ((sysUser.getSystemOccupation().equals("ContentManager"))) {
            updateContentButton.setVisible(true);
            viewReportsButton.setVisible(false);
            managerScreenButton.setVisible(false);
            covidButton.setVisible(false);
            handleComplaintsButton.setVisible(false);
        }
        if ((sysUser.getSystemOccupation().equals("CEO"))) {
            updateContentButton.setVisible(false);
            viewReportsButton.setVisible(false);
            managerScreenButton.setVisible(true);
            covidButton.setVisible(false);
            handleComplaintsButton.setVisible(false);
        }
        if ((sysUser.getSystemOccupation().equals("TheaterManager"))) {
            updateContentButton.setVisible(false);
            viewReportsButton.setVisible(true);
            managerScreenButton.setVisible(false);
            covidButton.setVisible(false);
            handleComplaintsButton.setVisible(false);
        }
        if ((sysUser.getSystemOccupation().equals("CustomerServiceClerk"))) {
            updateContentButton.setVisible(false);
            viewReportsButton.setVisible(false);
            managerScreenButton.setVisible(false);
            covidButton.setVisible(true);
            handleComplaintsButton.setVisible(true);
        }
        if ((sysUser.getSystemOccupation().equals("Admin"))) {
            updateContentButton.setVisible(true);
            viewReportsButton.setVisible(true);
            managerScreenButton.setVisible(true);
            covidButton.setVisible(true);
            handleComplaintsButton.setVisible(true);
        }

        subTF.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                checkSub(null);
                subButton.requestFocus();
            }
        });

        YTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                setY(null);
                setYButton.requestFocus();
            }
        });

    }


    @FXML
    void checkSub(ActionEvent event) {
        TheBooth.getSubscription(subTF.getText());
    }

    @Subscribe
    public void payWithSubscription(SubscriptionEvent event) {
        subTF.clear();
        subTF.setPromptText("Entries left in subscription: " + event.getSubscription().getEntries_left());
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

    void sendCommand(String command) {
        // Send a command to the server.
        try {
            SimpleClient.getClient().sendToServer(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

