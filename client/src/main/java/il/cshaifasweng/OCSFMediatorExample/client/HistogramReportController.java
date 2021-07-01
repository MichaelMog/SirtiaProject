package il.cshaifasweng.OCSFMediatorExample.client;
/**
 * Sample Skeleton for 'histogram_report.fxml' Controller Class
 */

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Date;

public class HistogramReportController {

    private String titleString = "";
    private String privilege = "all";
    private String reportName = "";
    private int[] days = new int[31];

    @FXML // fx:id="Title"
    private Label Title; // Value injected by FXMLLoader

    @FXML // fx:id="BackButton"
    private Button BackButton; // Value injected by FXMLLoader

    @FXML // fx:id="ReportInfoVBox"
    private VBox ReportInfoVBox; // Value injected by FXMLLoader

    @FXML // fx:id="DateText"
    private Text DateText; // Value injected by FXMLLoader

    @FXML // fx:id="DateField"
    private TextField dateField; // Value injected by FXMLLoader

    @FXML // fx:id="Submit"
    private Button submitButton; // Value injected by FXMLLoader

    @FXML // fx:id="complaintsChart"
    private BarChart<String, Number> complaintsChart; // Value injected by FXMLLoader

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
        dateField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    GetReport(null);
                    submitButton.requestFocus();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static int daysInMonth = 0;
    private static Date date;

    @Subscribe
    public void getReportDetails(ReportParametersEvent event) {
        reportName = event.getReportName();
        titleString = event.getReportTitle();
        privilege = event.getPrivilege();
        Title.setText(titleString);
    }

    @Subscribe
    public void getReport(ComplaintReportEvent event) throws ParseException {
        Complaint complaint = event.getComplaint();
        Date receivedDate = new SimpleDateFormat("yyyy-MM-DD_HH:mm").parse(complaint.getTime_registration());
        if (!isDateRange(complaint.getTime_registration()))
            return;
        days[receivedDate.getDate() - 1]++;
        Platform.runLater(() -> {
            complaintsChart.setTitle("Complaint Histogram");
            complaintsChart.getXAxis().setLabel("Date");
            complaintsChart.getYAxis().setLabel("Number of Complaints");
            complaintsChart.getData().clear();
            XYChart.Series data = new XYChart.Series();
            for (int i = 0; i < daysInMonth; i++) {
                data.getData().add(new XYChart.Data((i + 1) + "/" + (date.getMonth()+1), days[i]));
            }
            complaintsChart.getData().addAll(data);
        });
    }


    @FXML
    void GetReport(ActionEvent event) throws ParseException {
        if (dateField.getText().length() == 7 && isValidDate(dateField.getText())) {
            date = new SimpleDateFormat("MM/yyyy").parse(dateField.getText());
            complaintsChart.getData().clear();
            Arrays.fill(days, 0);
            YearMonth yearMonthObject = YearMonth.of(date.getYear(), date.getMonth());
            daysInMonth = yearMonthObject.lengthOfMonth();
            sendCommand("#getReports" + reportName);
        } else {
            popup("Invalid date!");
        }
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("show_reports");
    }

    void popup(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    String.format(message));
            alert.show();
        });
    }

    public static void sendCommand(String command) {
        // Send a command to the server.
        try {
            SimpleClient.getClient().sendToServer(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            dateFromReceived = new SimpleDateFormat("yyyy-MM-dd_HH:mm").parse(receivedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        if (dateFromReceived.getMonth() == date.getMonth() && dateFromReceived.getYear() == date.getYear())
            return true;
        return false;
    }

}