package il.cshaifasweng.OCSFMediatorExample.client;
/**
 * Sample Skeleton for 'scroll_report.fxml' Controller Class
 */

import il.cshaifasweng.OCSFMediatorExample.entities.CancelledPurchase;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Label title; // Value injected by FXMLLoader

    @FXML // fx:id="BackButton"
    private Button backButton; // Value injected by FXMLLoader

    @FXML // fx:id="ReportInfoVBox"
    private VBox reportInfoVBox; // Value injected by FXMLLoader

    @FXML // fx:id="DateText"
    private Text dateText; // Value injected by FXMLLoader

    @FXML // fx:id="DateField"
    private TextField dateField; // Value injected by FXMLLoader

    @FXML // fx:id="Submit"
    private Button submitButton; // Value injected by FXMLLoader

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
        dateField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    getReport(null);
                    submitButton.requestFocus();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe
    public void getReportDetails(ReportParametersEvent event) {
        reportName = event.getReportName();
        titleString = event.getReportTitle();
        privilege = event.getPrivilege();
        title.setText(titleString);
    }

    @Subscribe
    public void getTicketOrLinkSubscriptionReport(PurchasesReportEvent event) {
        Purchase purchase = event.getPurchase();
        String dataString = "";
        if (event.getReportType().equals("ticket") && purchase.getScreening() == null) {
            return;
        }
        if (event.getReportType().equals("link_subscription") && purchase.getScreening() != null) {
            return;
        }
        if (event.getReportType().equals("ticket")) {
            dataString = String.format("Customer: %s\tPayment: %s\tAmount: %s\tTime: %s\tMovie: %s\tSeats: %s",
                    purchase.getCustomerName(),
                    purchase.getPaymentInfo(),
                    purchase.getPrice(),
                    purchase.getPurchaseTime(),
                    purchase.getMovieDetail(),
                    purchase.getSeats()
            );
        }
        if (event.getReportType().equals("link_subscription")) {
            if (purchase.getLinkMovie() == null) { // subscription
                dataString = String.format("Customer: %s\tPayment: %s\tAmount: %s\tTime: %s",
                        purchase.getCustomerName(),
                        purchase.getPaymentInfo(),
                        purchase.getPrice(),
                        purchase.getPurchaseTime()
                );
            } else { // link movie
                dataString = String.format("Customer: %s\tPayment: %s\tAmount: %s\tTime: %s\tMovie: %s",
                        purchase.getCustomerName(),
                        purchase.getPaymentInfo(),
                        purchase.getPrice(),
                        purchase.getPurchaseTime(),
                        purchase.getMovieDetail()
                );
            }
        }
        String finalDataString = dataString;
        Platform.runLater(() -> {
            reportInfoVBox.getChildren().addAll(new Label(finalDataString));
        });
    }

    @FXML
    void getReport(ActionEvent event) throws ParseException {
        if (dateField.getText().length() == 7 && isValidDate(dateField.getText())) {
            Date date = new SimpleDateFormat("MM/yyyy").parse(dateField.getText());
            reportInfoVBox.getChildren().removeAll(reportInfoVBox.getChildren());
            sendCommand("#getReports" + reportName);
            if (App.getSysUser().getSystemOccupation().equals("TheaterManager")) {
                privilege = App.getSysUser().getSystemLocation();
            } else {
                privilege = "all";
            }
        } else {
            popup("invalid date!");
        }
    }

    @Subscribe
    public void getRefundReport(RefundsReportEvent event) {
        Integer sum = 0;
        List<CancelledPurchase> refunds = event.getRefunds();
        Iterator<CancelledPurchase> RefundsIterator = refunds.iterator();
        while (RefundsIterator.hasNext()) {
            //todo print line to vbox
            sum += RefundsIterator.next().getRefund();//sums the total amount of refunds
        }
        Integer finalSum = sum;
        Platform.runLater(() -> {
            reportInfoVBox.getChildren().addAll(new Label(String.valueOf(finalSum)));
        });
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
