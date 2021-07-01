package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class HandleComplaintsController {
    @FXML
    private VBox complaintsList;
    @FXML
    private VBox complaintMore;
    @FXML
    private TextField complaintId;
    @FXML
    private Button showMore;
    @FXML
    private Label timeLabel;

    @FXML
    public void initialize() {
        // Register to EventBus so we can subscribe to events when a movie is sent over by the server.
        EventBus.getDefault().register(this);
        App.getAppStage().setTitle("טיפול בתלונות");
        App.getAppStage().setHeight(605);
        App.getAppStage().setWidth(800);
    }

    public void shutdown() {
        // cleanup code here: unregister from EventBus.
        EventBus.getDefault().unregister(this);
    }

    @FXML // fx:id="ShowComplaints"
    private Button ShowComplaints; // Value injected by FXMLLoader

    @FXML
    void ShowAllComplaints(ActionEvent event) {
        complaintsList.getChildren().removeAll(complaintsList.getChildren());
        try {
            SimpleClient.getClient().sendToServer("#ShowAllComplaints");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // show content of some particular complaint by clicking on "show more"
    // sending msg to server
    public void ShowComplaintMore(String str) {

        if (complaintMore.getChildren().size() > 0) {
            complaintMore.getChildren().removeAll(complaintMore.getChildren());
        }
        str = "#ShowComplaintMore\t" + str;
        try {
            SimpleClient.getClient().sendToServer(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UpdateComplaint(int id, String result, int refund) {
//        System.out.println("id " + id + " result " + result + " ref " + refund);
        complaintMore.getChildren().removeAll(complaintMore.getChildren());
        String str = "#UpdateComplaint\t" + id + "\t" + result + "\t" + refund;
        try {
            SimpleClient.getClient().sendToServer(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Subscribe // displaying content of complaint
    // invoked by event bus
    public void ShowComplaint(ComplaintEvent c) {
        Platform.runLater(() -> {
            Label lb = new Label();
            lb.setText("Customer Name:\t" + c.getName());
            lb.setFont(new Font("System Bold", 12));
            Label cd = new Label();
            cd.setText("Complaint Details:");
            cd.setFont(new Font("System Bold", 12));
            TextArea p = new TextArea();

            p.setText(c.getContent());
            p.setEditable(false);
            complaintMore.getChildren().addAll(lb, cd, p);
            HBox opt = new HBox();
            TextField ref = new TextField();
            ref.setEditable(false);
            ref.setDisable(true);

            // string array
            String st[] = {"Resolved", "Refunded", "Rejected"};
            // create a choiceBox
            ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList(st));
            // add a listener
            choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                // if the item of the list is changed
                public void changed(ObservableValue ov, Number value, Number new_value) {
                    if (st[new_value.intValue()] == "Refunded") {
                        ref.setEditable(true);
                        ref.setDisable(false);
                    } else {
                        ref.setEditable(false);
                        ref.setDisable(true);
                        ref.clear();
                    }
                }
            });
            Button upd = new Button();
            upd.setText("Update");
            upd.setOnAction(event -> {

               if (choiceBox.getValue() != null && !(choiceBox.getValue().equals("Refunded") && (ref.getText().isEmpty() ||!ref.getText().matches("[0-9]+")) )) {
                    int refund = 0;
                    if (ref.getText() != "") refund = Integer.parseInt(ref.getText());
                    UpdateComplaint(c.getId(), choiceBox.getValue().toString(), refund);
                    ShowAllComplaints(null);
                }
            });
            opt.getChildren().addAll(choiceBox, ref);
            complaintMore.getChildren().add(opt);
            complaintMore.getChildren().add(upd);
            complaintMore.setSpacing(5);
        });
    }

    @Subscribe
    public void ShowComplaint(MessageEvent w) {
        complaintsList.setSpacing(5);

        String[] str = w.getMsg().split("###");
        Platform.runLater(() -> {
            HBox boxtitle = new HBox();
            Label l1 = new Label();
            Label l2 = new Label();
            Label l3 = new Label();
            l1.setText("Complaint ID\t");
            l2.setText("Registration time\t");
            l3.setText("Time remained");
            boxtitle.getChildren().addAll(l1, l2, l3);
            complaintsList.getChildren().add(boxtitle);
            for (String s : str) {

                String[] ss = s.split("\t");
                String id = ss[0];
                String[] time = ss[1].split("_");
                HBox box = new HBox();

                String[] date = time[0].split("-");

                Label idd = new Label();
                idd.setText(id + "\t\t\t");

                Label tm = new Label();
                tm.setText(date[1] + "-" + date[2] + " " + time[1] + "\t\t");

                int x = Integer.parseInt(date[2]) + 1;

                String currDate = LocalDate.now().toString();
                String currTime = LocalTime.now().toString();

                int daysDiff = x - Integer.parseInt(currDate.split("-")[2]);
                int dlHours = Integer.parseInt(currTime.split(":")[0]);
                int dlMinutes = Integer.parseInt(currTime.split(":")[1]);

                int hoursDiff = dlHours - Integer.parseInt(time[1].split(":")[0]);
                int minutesDiff = dlMinutes - Integer.parseInt(time[1].split(":")[1]);


                int diff = daysDiff * 24 * 60 + hoursDiff * 60 + minutesDiff;
                int mint = diff % 60;
                diff = diff / 60;

                Label tp = new Label();
                if (diff == 0 && mint > 0) {
                    tp.setText(mint + " minutes" + "\t");
                } else if (diff <= 0) {
                    tp.setText("expired" + "\t");
                } else tp.setText(diff + " hours" + "\t");
                Button bt = new Button();
                bt.setText("show more");
                bt.setId(id);
                bt.setOnAction(event -> {
                    ShowComplaintMore(id);

                });

                box.getChildren().add(idd);
                box.getChildren().add(tm);
                box.getChildren().add(tp);
                box.getChildren().add(bt);
                box.setSpacing(2);
                complaintsList.getChildren().add(box);
            }
        });
    }

    @FXML
    void BackTo() throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("screen_navigation");
    }
}
