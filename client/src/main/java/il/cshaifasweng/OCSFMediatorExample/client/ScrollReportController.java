package il.cshaifasweng.OCSFMediatorExample.client;
/**
 * Sample Skeleton for 'scroll_report.fxml' Controller Class
 */

import il.cshaifasweng.OCSFMediatorExample.entities.CancelledPurchases;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private String privilege = "all";//can get values: all-see all reports, none:cant see reports, TheaterName:can see the reports of this theater
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
    public void initialize() {
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
    public void getRefundReport(RefundsReportEvent event) {
        Integer sum = 0;
        List<CancelledPurchases> refunds = event.getRefunds();
        Iterator<CancelledPurchases> RefundsIterator = refunds.iterator();
        while (RefundsIterator.hasNext()) {
            //todo print line to vbox
            sum += RefundsIterator.next().getRefund();//sums the total amount of refunds
        }
        Integer finalSum = sum;
        Platform.runLater(() -> {
            ReportInfoVBox.getChildren().add(new Label(String.valueOf(finalSum)));
        });
    }

    @Subscribe
    public void getTicketsReport(PurchasesReportEvent event) {
        List<Purchase> tickets = event.getPurchases();
        Collections.sort(tickets, new Comparator<Purchase>() {
                @Override
                public int compare(Purchase p1, Purchase p2) {
                    return p1.getScreening().getLocation().compareTo(p2.getScreening().getLocation());
                }
            });
        Iterator<Purchase> TicketIterator = tickets.iterator();
        Platform.runLater(() -> {
            int sum = 0;
            ReportInfoVBox.getChildren().add(new Label("Purchase time\t\tPrice\t\t\tSeats"));
            while (TicketIterator.hasNext()) {
                ReportInfoVBox.getChildren().add(new Label(TicketIterator.next().getPurchaseTime() + "\t\t" + TicketIterator.next().getPrice() + "\t\t" + TicketIterator.next().getSeats()));
                sum += TicketIterator.next().getPrice();//todo multiply by number of seats
            }
            Integer finalSum = sum;
            ReportInfoVBox.getChildren().add(new Label("amount: " + String.valueOf(finalSum)));
        });
    }

    //@Subscribe
    //public void getLinkSubReport( LinkAndSubscriptionReportEvent event) {
    //    Integer sum = 0;
    //    List<Purchase> Links = event.getLinkPurchases();
    //    List<Purchase> Subscriptions = event.getSubscriptionPurchases();
    //    ReportInfoVBox.getChildren().add((new Label("Links Purchases:")));
    //    ReportInfoVBox.getChildren().add(new Label("Purchase time\t\tPrice\t\t\tSeats"));
    //    Iterator<Purchase> LinksIterator= Links.iterator();
    //    while (LinksIterator.hasNext()){
    //        ReportInfoVBox.getChildren().add(new Label(LinksIterator.next().getPurchase_time()+"\t\t"+ LinksIterator.next().getPrice() + "\t\t"+ LinksIterator.next().getSeats()));
    //        sum += LinksIterator.next().getPrice();
    //    }
    //   ReportInfoVBox.getChildren().add((new Label("Subscriptions Purchases:")));
    //    ReportInfoVBox.getChildren().add(new Label("Purchase time\t\tPrice\t\t\tSeats"));
    //    Iterator<Purchase> SubscriptionsIterator= Subscriptions.iterator();
    //    while (SubscriptionsIterator.hasNext()){
    //       ReportInfoVBox.getChildren().add(new Label(SubscriptionsIterator.next().getPurchase_time()+"\t\t"+ SubscriptionsIterator.next().getPrice() + "\t\t"+ SubscriptionsIterator.next().getSeats()));
    //        sum += SubscriptionsIterator.next().getPrice();
    //    }
    //    ReportInfoVBox.getChildren().add((new Label("amount of all Links & Subscriptions purchases: " + String.valueOf(sum) + "₪")));
    //}


    @FXML
    void GetReport(ActionEvent event) throws ParseException {
        if (DateField.getText().length() == 7 && isValidDate(DateField.getText())) {
            Date date = new SimpleDateFormat("MM/yyyy").parse(DateField.getText());
            ReportInfoVBox.getChildren().removeAll(ReportInfoVBox.getChildren());
            sendCommand("#getReports" + reportName);
            if(App.getSysUser().getSystemOccupation().equals("TheaterManager")){
                privilege = App.getSysUser().getSystemLocation();
            }
            else {
                privilege = "all";
            }
        } else {
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

    void popup(String message) {
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
