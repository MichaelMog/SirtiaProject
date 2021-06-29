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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;

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
        if(loginButton.getText().equals("administrative login")){
            SystemUser sysUser= new SystemUser(null,null,"guest");
            App.getApp_stage().setUserData(sysUser);
            App.getApp_stage().setTitle("Main Menu");
            /*EventBus.getDefault().unregister(this);*/
            App.setRoot("screen_navigation");
        }else{
            System.out.println("not ready yet");
        }
    }

    public void shutdown(){
        // do nothing
    }

    @FXML
    void showTextFields(ActionEvent event) {
        if(loginButton.getText().equals("administrative login")){
            loginButton.setText("normal start screen");
            usernameTF.setVisible(true);
            passwordTF.setVisible(true);
        }else{
            loginButton.setText("administrative login");
            usernameTF.setVisible(false);
            passwordTF.setVisible(false);
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        /*EventBus.getDefault().register(this);*/
    }
}