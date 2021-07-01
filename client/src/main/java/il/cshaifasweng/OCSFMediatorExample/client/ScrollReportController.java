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
            ReportInfoVBox.getChildren().addAll(new Label(String.valueOf(finalSum)));
        });
    }

    @Subscribe
    public void getTicketsReport(PurchasesReportEvent event) {
        List<Purchase> tickets = event.getPurchases();
        for (Purchase ticket : tickets) {
            System.out.println(ticket.getCustomerName());
        }
        System.out.println("Done!\n");
        List<Purchase> purchasesList = tickets;
        for (Purchase ticket : purchasesList) {
            if (event.getReportType().equals("ticket")) {
                if (ticket.getScreening() == null) {
                    System.out.println("removing ticket " + ticket.getPaymentInfo());
                    tickets.remove(ticket);
                }
            }
            if (event.getReportType().equals("link_subscription")) {
                if (ticket.getScreening() != null) {
                    System.out.println("removing ticket " + ticket.getPaymentInfo());
                    tickets.remove(ticket);
                }
            }
        }
        for (Purchase ticket : tickets) {
            System.out.println(ticket.getCustomerName());
        }
        System.out.println("Done!\n");
        Collections.sort(tickets, new Comparator<Purchase>() {
                @Override
                public int compare(Purchase p1, Purchase p2) {
                    return p1.getScreening().getLocation().compareTo(p2.getScreening().getLocation());
                }
            });

        Platform.runLater(() -> {
            int sum = 0;
            ReportInfoVBox.getChildren().addAll(new Label("Purchase time\t\tPrice\t\t\tSeats"));
            for (Purchase purchase : tickets) {
                Label label = new Label(purchase.getPurchaseTime() + "\t\t" + purchase.getPrice() + "\t\t" + purchase.getSeats());
                ReportInfoVBox.getChildren().addAll(label);
                sum += purchase.getPrice();//todo multiply by number of seats
            }
            Integer finalSum = sum;
            ReportInfoVBox.getChildren().addAll(new Label("amount: " + String.valueOf(finalSum)));
        });
    }

    @FXML
    void getReport(ActionEvent event) throws ParseException {
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
