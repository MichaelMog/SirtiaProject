package il.cshaifasweng.OCSFMediatorExample.client;
/**
 * Sample Skeleton for 'histogram_report.fxml' Controller Class
 */

import il.cshaifasweng.OCSFMediatorExample.entities.CancelledPurchase;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Date;

import static il.cshaifasweng.OCSFMediatorExample.client.ScrollReportController.isValidDate;
import static il.cshaifasweng.OCSFMediatorExample.client.TheBooth.sendCommand;

public class HistogramReportController {

    private String titleString = "";
    private String privilege = "";
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
    private TextField DateField; // Value injected by FXMLLoader

    @FXML // fx:id="Submit"
    private Button Submit; // Value injected by FXMLLoader

    @FXML // fx:id="complaintsChart"
    private BarChart<String, Number> complaintsChart; // Value injected by FXMLLoader

    @FXML
    public void initialize(){
//        App.getApp_stage().setTitle("צפייה בדו\"חות");
        EventBus.getDefault().register(this);
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
    public void getReport( ComplaintReportEvent event) throws ParseException {
        Complaint complaint = event.getComplaint();
        if (!ScrollReportController.isDateRange(complaint.getTime_registration()))
            return;
        Date receivedDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(complaint.getTime_registration());
        days[receivedDate.getDay()-1]++;
        complaintsChart.setTitle("Complaint Histogram");
        complaintsChart.getXAxis().setLabel("Date");
        complaintsChart.getYAxis().setLabel("Number of Complaints");
        complaintsChart.getData().clear();

        XYChart.Series data = new XYChart.Series();
        for(int i=0;i<daysInMonth;i++){
            data.getData().add(new XYChart.Data(String.valueOf(i+1)+"/"+String.valueOf(date.getMonth()), days[i]));
        }
        complaintsChart.getData().addAll(data);
    }


    @FXML
    void GetReport(ActionEvent event) throws ParseException {
        if(DateField.getText().length() == 7 && isValidDate(DateField.getText())){
            date = new SimpleDateFormat("MM/yyyy").parse(DateField.getText());
            complaintsChart.getData().clear();
            Arrays.fill(days, 0);
            YearMonth yearMonthObject = YearMonth.of(date.getYear(), date.getMonth());
            daysInMonth = yearMonthObject.lengthOfMonth();
            sendCommand("#showReports" + reportName);
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

    void popup(String message){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    String.format(message));
            alert.show();
        });
    }

}
