package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.type.TrueFalseType;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HandleComplaintsController {


    @FXML
    private VBox complaints_list;
    @FXML
    private VBox ComplaintMore;
    @FXML
    private TextField complid;
    @FXML
    private Button showmore;
    @FXML
    private Label ltime;

    @FXML
    public void initialize() {
        // Register to EventBus so we can subscribe to events when a movie is sent over by the server.
        EventBus.getDefault().register(this);
        App.getApp_stage().setHeight(605);
        App.getApp_stage().setWidth(1250);
    }

    public void shutdown() {
        // cleanup code here: unregister from EventBus.
        EventBus.getDefault().unregister(this);
    }

    @FXML // fx:id="ShowComplaints"
    private Button ShowComplaints; // Value injected by FXMLLoader

    @FXML
    void ShowAllComplaints(ActionEvent event) {
        complaints_list.getChildren().removeAll(complaints_list.getChildren());

        try {
            SimpleClient.getClient().sendToServer("#ShowAllComplaints");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void goback() throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("screen_navigation");
    }

//    @FXML // show content of some particular complaint by clicking on "show more"
    // sending msg to server
//     public void ShowComplaintMore(ActionEvent event){
//
//        if(ComplaintMore.getChildren().size()>2){
//            ComplaintMore.getChildren().removeAll(ComplaintMore.getChildren().get(2));
//        }
//
////        System.out.println(complid.getText().toString());
//
//        String str = complid.getText();
////         ComplaintMore.getChildren().removeAll(ComplaintMore.getChildren());
//
//        str = "#ShowComplaintMore\t"+str;
//         System.out.println(str);
//
//         try {
//             SimpleClient.getClient().sendToServer(str);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//
//    }

    // show content of some particular complaint by clicking on "show more"
    // sending msg to server
    public void ShowComplaintMore(String str){

        if(ComplaintMore.getChildren().size()>0){
            ComplaintMore.getChildren().removeAll(ComplaintMore.getChildren());
        }
//        System.out.println(str);
        str = "#ShowComplaintMore\t"+str;
        try {
            SimpleClient.getClient().sendToServer(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void UpdateComplaint(int id, String result, int refund){
        System.out.println("id " + id + " result " + result + " ref " + refund);
        ComplaintMore.getChildren().removeAll(ComplaintMore.getChildren());
        String str = "#UpdateComplaint\t"+id + "\t" + result + "\t" + refund;
        try {
            SimpleClient.getClient().sendToServer(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Subscribe // displaying content of complaint
    // invoked by event bus
    public void ShowComplaint(ComplaintEvent c){


        System.out.println(c.getName());
        Platform.runLater(()-> {
            Label lb = new Label();
            lb.setText("Customer Name:\t" + c.getName());
            lb.setFont(new Font("System Bold",12));
            Label cd = new Label();
            cd.setText("Complaint Details:");
            cd.setFont(new Font("System Bold",12));
//            Label p = new Label();
            TextArea p = new TextArea();

            p.setText(c.getContent());
            p.setEditable(false);
            ComplaintMore.getChildren().addAll(lb,cd, p);
            HBox opt = new HBox();
            TextField ref = new TextField();
            ref.setEditable(false);
            ref.setDisable(true);

            // string array
            String st[] = { "Resolved", "Refunded", "Rejected" };
            // create a choiceBox
            ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList(st));
            // add a listener
            choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

                // if the item of the list is changed
                public void changed(ObservableValue ov, Number value, Number new_value)
                {


                    if(st[new_value.intValue()]== "Refunded"){
                        ref.setEditable(true);
                        ref.setDisable(false);
                    }
                    else {
                        ref.setEditable(false);
                        ref.setDisable(true);
                        ref.clear();
                    }

//                    ref.setText(st[new_value.intValue()] + " selected");
                }});
            Button upd = new Button();
            upd.setText("Update");
            upd.setOnAction(event -> {

                if(choiceBox.getValue()!=null){
                    int refund = 0;
                    if(ref.getText() != "") refund = Integer.parseInt(ref.getText());
                    UpdateComplaint(c.getId(), choiceBox.getValue().toString(), refund);
                    ShowAllComplaints(null);
                }


            });
            opt.getChildren().addAll(choiceBox,ref);
            ComplaintMore.getChildren().add(opt);
            ComplaintMore.getChildren().add(upd);
            ComplaintMore.setSpacing(5);

        });
    }

    @Subscribe
    public void ShowComplaint(MessageEvent w ){
        complaints_list.setSpacing(5);

        String[] str = w.getMsg().split("###");
//        System.out.println(complaints_list.getWidth());
        Platform.runLater(() -> {
            HBox boxtitle = new HBox();
            Label l1 = new Label();
            Label l2 = new Label();
            Label l3 = new Label();
            l1.setText("Complaint ID\t");
            l2.setText("Registration time\t");
            l3.setText("Time remained");
            boxtitle.getChildren().addAll(l1,l2,l3);
            complaints_list.getChildren().add(boxtitle);
            for(String s : str){
//                System.out.println(s);

                String[] ss = s.split("\t");
                String id = ss[0];
//                String name = ss[1];
//                System.out.println(ss[1]);
                String[] time = ss[1].split("_");
//                System.out.println(time[1]);
                HBox box = new HBox();

                String[] date = time[0].split("-");

                Label idd = new Label();
//                idd.setText("Complaint ID: " + id + "\t");
                idd.setText(id + "\t\t\t");

                Label tm = new Label();
//                tm.setText("Registration time: " + date[1] + "-" + date[2] + " " + time[1] + "\t");
                tm.setText(date[1] + "-" + date[2] + " " + time[1] + "\t\t");


                //#################################################
//                String eventStr = "2016-08-16T19:45:00Z";
                int x = Integer.parseInt(date[2])+1;
                String eventStr;
                if(x<10){
                    eventStr = date[1]+"-"+"0"+x+" "+time[1];

                }
                else  eventStr = date[1]+"-"+x+" "+time[1];

                String currdate = LocalDate.now().toString();
                String currtime = LocalTime.now().toString();

                int daysdiff = x-Integer.parseInt(currdate.split("-")[2]);
                int dlhours = Integer.parseInt(currtime.split(":")[0]);
                int dlminutes = Integer.parseInt(currtime.split(":")[1]);

//                dlhours = 14;
//                dlminutes = 9;
//                daysdiff = 0;
                int hoursdiff = dlhours-Integer.parseInt(time[1].split(":")[0]);
                int minutesdiff = dlminutes-Integer.parseInt(time[1].split(":")[1]);



                int diff = daysdiff*24*60+hoursdiff*60+minutesdiff;
                int mint = diff%60;
                diff = diff/60;

                Label tp = new Label();
//                    tp.setText(""+days);
                if(diff==0 && mint>0){
                    tp.setText(mint + " minutes" +"\t");
                }
                else if(diff<=0){
                    tp.setText("expired"+"\t");
                }
                else tp.setText(diff + " hours"+"\t");

//                tp.setText(diff + "\t" + mint);
//                tp.setText(eventStr);
//                tp.setText(Long.toString(hours)+" hours" + "\t");
                Button bt = new Button();
                bt.setText("show more");
                bt.setId(id);
//                bt.setOnAction(event -> test(id));
                bt.setOnAction(event -> {
                    System.out.println("Button clicked");
                    ShowComplaintMore(id);

                });

                box.getChildren().add(idd);
//                box.getChildren().add(nm);
                box.getChildren().add(tm);
                box.getChildren().add(tp);
                box.getChildren().add(bt);
                box.setSpacing(2);
//                System.out.println(box.getWidth());

//                box.setMaxWidth(complaints_list.getWidth());
                complaints_list.getChildren().add(box);
            }


        });



    }

}