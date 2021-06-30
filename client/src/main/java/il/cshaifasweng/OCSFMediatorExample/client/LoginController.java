/**
 * Sample Skeleton for 'login.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.SystemUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="startButton"
    private Button startButton; // Value injected by FXMLLoader

    @FXML // fx:id="loginButton"
    private Button loginButton; // Value injected by FXMLLoader

    @FXML // fx:id="usernameTF"
    private TextField usernameTF; // Value injected by FXMLLoader

    @FXML // fx:id="passwordTF"
    private TextField passwordTF; // Value injected by FXMLLoader


    @FXML
    void goToMainMenu(ActionEvent event) throws IOException {
        if(loginButton.getText().equals("Administrative Login")){
            SystemUser sysUser= new SystemUser(null,null,"guest");
            App.getAppStage().setUserData(sysUser);
            EventBus.getDefault().unregister(this);
            App.setRoot("screen_navigation");
        }else{

            // check legal username
            if (usernameTF.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No username was given",
                        ButtonType.OK);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    return;
                }
            }

            // check legal password
            if (passwordTF.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No password was given",
                        ButtonType.OK);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    return;
                }
            }

            TheBooth.getSystemUser(usernameTF.getText(), passwordTF.getText());
        }
    }

    @Subscribe
    public void handleLogin(SystemUserEvent event) throws IOException {
        SystemUser systemUser = event.getSystemUser();
        App.getAppStage().setUserData(systemUser);
        App.getAppStage().setTitle("Main Menu");
        EventBus.getDefault().unregister(this);
        App.setRoot("screen_navigation");
    }

    public void shutdown(){
        // do nothing
    }

    @FXML
    void showTextFields(ActionEvent event) {
        if(loginButton.getText().equals("Administrative Login")){
            loginButton.setText("Guest Screen");
            usernameTF.setVisible(true);
            passwordTF.setVisible(true);
        }else{
            loginButton.setText("Administrative Login");
            usernameTF.setVisible(false);
            passwordTF.setVisible(false);
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        App.getAppStage().setTitle("Welcome");

        EventBus.getDefault().register(this);
    }
}
