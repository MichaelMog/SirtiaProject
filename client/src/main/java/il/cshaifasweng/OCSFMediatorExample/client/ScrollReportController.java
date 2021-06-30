package il.cshaifasweng.OCSFMediatorExample.client;
/**
 * Sample Skeleton for 'scroll_report.fxml' Controller Class
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ScrollReportController {

    private String titleString = "";

    @FXML // fx:id="Title"
    private Label Title; // Value injected by FXMLLoader

    @FXML // fx:id="BackButton"
    private Button BackButton; // Value injected by FXMLLoader

    @FXML // fx:id="ReportInfoVBox"
    private VBox ReportInfoVBox; // Value injected by FXMLLoader

    @FXML // fx:id="DateText"
    private Text DateText; // Value injected by FXMLLoader

    @FXML // fx:id="DateField"
    private TextField DateField; // Value injected by FXMLLoader

    @FXML // fx:id="Submit"
    private Button Submit; // Value injected by FXMLLoader

    @FXML
    public void initialize(){
//        App.getApp_stage().setTitle("צפייה בדו\"חות");
    }
    @FXML
    void GetReport(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {

    }

}
