package il.cshaifasweng.OCSFMediatorExample.client;
/**
 * Sample Skeleton for 'scroll_report.fxml' Controller Class
 */

import il.cshaifasweng.OCSFMediatorExample.entities.CancelledPurchase;
import il.cshaifasweng.OCSFMediatorExample.entities.Purchase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    private int maxLabelWidth = 1000;

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

    private static Date date;

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
        App.getAppStage().setWidth(1050);
        App.getAppStage().setHeight(500);
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
        String vboxTitle = "";
        Purchase purchase = event.getPurchase();
        if (!isDateRange(purchase.getPurchaseTime()))
            return;
        String dataString = "";
        if (event.getReportType().equals("ticket") && purchase.getScreening() == null) {
            return;
        }
        if (event.getReportType().equals("link_subscription") && purchase.getScreening() != null) {
            return;
        }
        if (event.getReportType().equals("ticket")) {
            dataString = String.format("Customer: %s\t\tPayment: %s\t\tAmount: %s\t\tTime: %s\t\tMovie: %s\t\tSeats: %s",
                    purchase.getCustomerName(),
                    purchase.getPaymentInfo(),
                    purchase.getPrice(),
                    purchase.getPurchaseTime(),
                    purchase.getMovieDetail(),
                    purchase.getSeats()
            );
            vboxTitle = purchase.getScreening().getLocation();
        }
        if (event.getReportType().equals("link_subscription")) {
            if (purchase.getLinkMovie() == null) { // subscription
                dataString = String.format("Customer: %s\t\tPayment: %s\t\tAmount: %s\t\tTime: %s",
                        purchase.getCustomerName(),
                        purchase.getPaymentInfo(),
                        purchase.getPrice(),
                        purchase.getPurchaseTime()
                );
            } else { // link movie
                dataString = String.format("Customer: %s\t\tPayment: %s\t\tAmount: %s\t\tTime: %s\t\tMovie: %s",
                        purchase.getCustomerName(),
                        purchase.getPaymentInfo(),
                        purchase.getPrice(),
                        purchase.getPurchaseTime(),
                        purchase.getMovieDetail()
                );
            }
        }
        String finalDataString = dataString;
        String finalVboxTitle = vboxTitle;
        Platform.runLater(() -> {
            if (event.getReportType().equals("link_subscription")) {
                Label label = new Label(finalDataString);
                label.setMaxWidth(maxLabelWidth);
                reportInfoVBox.getChildren().addAll(new Label(finalDataString));
            } else {
                if (!privilege.equals("all") && !finalVboxTitle.equals(privilege))
                    return;
                boolean foundTheater = false;
                int i = 0;
                for (Node node : reportInfoVBox.getChildren()) {
                    Label label = (Label) node;
                    if (label.getText().equals(finalVboxTitle)) {
                        foundTheater = true;
                        Label label2 = new Label(finalDataString);
                        label2.setMaxWidth(maxLabelWidth);
                        reportInfoVBox.getChildren().add(i+1, label2);
                        break;
                    }
                    i++;
                }
                if (!foundTheater) {
                    if (i > 1) reportInfoVBox.getChildren().add(new Label(""));
                    reportInfoVBox.getChildren().add(new Label(finalVboxTitle));
                    Label label = new Label(finalDataString);
                    label.setMaxWidth(maxLabelWidth);
                    reportInfoVBox.getChildren().add(label);
                }
            }
        });
    }

    @Subscribe
    public void getRefundReport(RefundReportEvent event) {
        CancelledPurchase cancelledPurchase = event.getRefund();
        if (!isDateRange(cancelledPurchase.getPurchaseTime()))
            return;
        String dataString = String.format("Customer: %s\t\tPayment: %s\t\tAmount: %s\t\tTime: %s\t\tMovie: %s",
                cancelledPurchase.getCustomerName(),
                cancelledPurchase.getPaymentInfo(),
                cancelledPurchase.getRefund(),
                cancelledPurchase.getPurchaseTime(),
                cancelledPurchase.getMovieDetail()
        );
        String finalDataString = dataString;
        Platform.runLater(() -> {
            Label label = new Label(finalDataString);
            label.setMaxWidth(maxLabelWidth);
            reportInfoVBox.getChildren().addAll(label);
        });
    }

    @FXML
    void getReport(ActionEvent event) throws ParseException {
        if (dateField.getText().length() == 7 && isValidDate(dateField.getText())) {
            date = new SimpleDateFormat("MM/yyyy").parse(dateField.getText());
            reportInfoVBox.getChildren().removeAll(reportInfoVBox.getChildren());
            sendCommand("#getReports" + reportName);
            if (App.getSysUser().getSystemOccupation().equals("TheaterManager")) {
                privilege = App.getSysUser().getSystemLocation();
            } else {
                privilege = "all";
            }
        } else {
            popup("Invalid date!");
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

    public static boolean isDateRange(String receivedDate) {
        Date dateFromReceived;
        try {
            dateFromReceived = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(receivedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        if (dateFromReceived.getMonth() == date.getMonth() && dateFromReceived.getYear() == date.getYear())
            return true;
        return false;
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
