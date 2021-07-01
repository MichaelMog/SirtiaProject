package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class ReportsController {

    @FXML // fx:id="TicketReport"
    private Button TicketReport; // Value injected by FXMLLoader

    @FXML // fx:id="SubscriptionReport"
    private Button SubscriptionReport; // Value injected by FXMLLoader

    @FXML // fx:id="RefundsReport"
    private Button RefundsReport; // Value injected by FXMLLoader

    @FXML // fx:id="ComplaintsHistogram"
    private Button ComplaintsHistogram; // Value injected by FXMLLoader

    @FXML // fx:id="BackButton"
    private Button BackButton; // Value injected by FXMLLoader

    private String privilege = "all";//
    private String title = "";

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    @FXML
    public void initialize() {
        App.getAppStage().setTitle("צפייה בדו\"חות");
    }
    
    @FXML
    void GoBack(ActionEvent event) throws IOException{
    	App.setRoot("screen_navigation");
    }

    @FXML
    void ShowComplaintReport(ActionEvent event) throws IOException{
        title = "Complaint Histogram";
        ReportParametersEvent ReportEvent = new ReportParametersEvent("\tcomplaint", title, privilege);
        App.setRoot("histogram_report");
        EventBus.getDefault().post(ReportEvent);
    }

    @FXML
    void ShowRefundsReport(ActionEvent event) throws IOException{
        title = "Refund Report";
        ReportParametersEvent ReportEvent = new ReportParametersEvent("\trefund", title, privilege);
        App.setRoot("scroll_report");
        EventBus.getDefault().post(ReportEvent);
    }

    @FXML
    void ShowTicketReport(ActionEvent event) throws IOException{
        title = "Tickets Report";
        ReportParametersEvent ReportEvent = new ReportParametersEvent("\tticket",title, privilege);
        App.setRoot("scroll_report");
        EventBus.getDefault().post(ReportEvent);
    }

    @FXML
    void ShowLinksReport(ActionEvent event) throws IOException{
        title = "Links & Subscriptions Report";
        ReportParametersEvent ReportEvent = new ReportParametersEvent("\tlink_subscription",title, privilege);
        App.setRoot("scroll_report");
        EventBus.getDefault().post(ReportEvent);
    }


}
