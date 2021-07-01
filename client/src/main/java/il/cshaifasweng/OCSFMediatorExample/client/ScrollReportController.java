package il.cshaifasweng.OCSFMediatorExample.client;
/**
 * Sample Skeleton for 'scroll_report.fxml' Controller Class
 */

import il.cshaifasweng.OCSFMediatorExample.entities.CancelledPurchases;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

public class ScrollReportController {

    private String titleString = "";
    private String privilege = "";
    private String reportName = "";

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
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void getReportDetails(ReportParametersEvent event) {
        reportName = event.getReportName();
        titleString = event.getReportTitle();
        privilege = event.getPrivilege();
        Title.setText(titleString);
    }

    @Subscribe
    public void getRefundReport( RefundsReportEvent event) {
       Integer sum = 0;
       List<CancelledPurchases> refunds = event.getRefunds();
        Iterator<CancelledPurchases> RefundsIterator= refunds.iterator();
        while (RefundsIterator.hasNext()){
            //todo print line to vbox
            sum += RefundsIterator.next().getRefund();//sums the total amount of refunds
        }
        ReportInfoVBox.getChildren().add(new Label(String.valueOf(sum)));
    }

    @Subscribe
    public void getTicketsReport( TicketReportEvent event) {
        Integer sum = 0;
        System.out.println("hello");
        List<Purchase> tickets = event.getTicketPurchases();
        Collections.sort(tickets, Comparator.comparing(Purchase::getMovieDetail));
        Iterator<Purchase> TicketIterator= tickets.iterator();
        while (TicketIterator.hasNext()){
            //todo print line to vbox
            sum += TicketIterator.next().getPrice();//todo multiply by number of seats
        }
        ReportInfoVBox.getChildren().add(new Label(String.valueOf(sum)));
    }

    @Subscribe
    public void getLinkSubReport( LinkAndSubscriptionReportEvent event) {
        Integer sum = 0;
        List<Purchase> Links = event.getLinkPurchases();
        List<Purchase> Subscriptions = event.getSubscriptionPurchases();
        //todo add to vbox title Links
        Iterator<Purchase> LinksIterator= Links.iterator();
        while (LinksIterator.hasNext()){
            //todo print line to vbox
            sum += LinksIterator.next().getPrice();
        }
        //todo add to vbox title Subscription
        Iterator<Purchase> SubscriptionsIterator= Subscriptions.iterator();
        while (SubscriptionsIterator.hasNext()){
            //todo print line to vbox
            sum += SubscriptionsIterator.next().getPrice();
        }
        ReportInfoVBox.getChildren().add((new Label("amount of all Links & Subscriptions purchases: " + String.valueOf(sum) + "₪")));
    }


    @FXML
    void GetReport(ActionEvent event) throws ParseException {
        if(DateField.getText().length() == 7 && isValidDate(DateField.getText())){
            Date date = new SimpleDateFormat("MM/yyyy").parse(DateField.getText());
            ReportInfoVBox.getChildren().removeAll();
            sendCommand("#getReports" + reportName);
            System.out.println("#getReports" + reportName);
        }
        else{
            popup("invalid date!");
        }
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("show_reports");
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    void popup(String message){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    String.format(message));
            alert.show();
        });
    }

    public void sendCommand(String command) {
        // Send a command to the server.
        try {
            SimpleClient.getClient().sendToServer(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
