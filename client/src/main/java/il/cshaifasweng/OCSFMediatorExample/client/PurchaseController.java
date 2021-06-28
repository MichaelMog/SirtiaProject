package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.LinkMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieTitle;
import il.cshaifasweng.OCSFMediatorExample.entities.Screening;
import il.cshaifasweng.OCSFMediatorExample.entities.Subscription;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PurchaseController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="TitleLabel"
    private Label TitleLabel; // Value injected by FXMLLoader

    @FXML // fx:id="selectedMovie"
    private ScrollPane selectedMovie; // Value injected by FXMLLoader

    @FXML // fx:id="movieList"
    private VBox movieList; // Value injected by FXMLLoader

    @FXML // fx:id="SecondaryLabel"
    private Label SecondaryLabel; // Value injected by FXMLLoader

    @FXML // fx:id="nameTF"
    private TextField nameTF; // Value injected by FXMLLoader

    @FXML // fx:id="paymentInfoTF"
    private TextField paymentInfoTF; // Value injected by FXMLLoader

    @FXML // fx:id="confirmButton"
    private Button confirmButton; // Value injected by FXMLLoader

    @FXML // fx:id="grandTotalTF"
    private Label grandTotalTF; // Value injected by FXMLLoader

    @FXML // fx:id="BorderPane"
    private BorderPane BorderPane; // Value injected by FXMLLoader

    @FXML
    private Button cancelButton;

    @FXML // fx:id="subLine"
    private Line subLine; // Value injected by FXMLLoader

    @FXML // fx:id="subLabel"
    private Label subLabel; // Value injected by FXMLLoader

    @FXML // fx:id="subTF"
    private TextField subTF; // Value injected by FXMLLoader

    @FXML // fx:id="subButton"
    private Button subButton; // Value injected by FXMLLoader

    private int[][] clickCount;
    int grandTotal = 0;
    SendMovieEvent sent;
    String returnto;
    private int seatsNum;
    private String takenseats;

    @FXML
    void useSubscription(ActionEvent event) {

        seatsNum = 0;
        takenseats = "";

        // check legal subscription name
        if (subTF.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No subscription name was given",
                    ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                return;
            }
        }

        // check seats taken
        for (int i = 0; i < sent.getScreening().getRows(); i++) {
            for (int j = 0; j < sent.getScreening().getColumns(); j++) {
                if (clickCount[i][j] % 2 == 1) {
                    seatsNum++;
                    takenseats += "(" + i + "," + j + ")";
                }
            }
        }

        if (seatsNum == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No seats were selected",
                    ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                return;
            }
        }

        TheBooth.getSubscription(subTF.getText());

    }

    @Subscribe
    public void payWithSubscription(SubscriptionEvent event) {

        if(sent==null){
            return;
        }

        Subscription s = event.getSubscription();

        if (s.getEntries_left() < seatsNum) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Subscription doesn't have enough entries left.",
                        ButtonType.OK);
                alert.showAndWait();
            });
            return;
        } else {

            EventBus.getDefault().unregister(this);

            TheBooth.subscriptionPayment(s.getFull_name(), seatsNum);

            // take seats in theater
            for (int i = 0; i < sent.getScreening().getRows(); i++) {
                for (int j = 0; j < sent.getScreening().getColumns(); j++) {
                    if (takenseats.contains("(" + i + "," + j + ")")) {
                        TheBooth.takeSeat(sent.getScreening(), i, j);
                    }
                }
            }

            // add purchase to database
            TheBooth.addPurchase(subTF.getText(), "subscription", takenseats, 0, sent.getScreening().getScreeningId());

            // go back to navigation screen
            try {
                App.setRoot("screen_navigation");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void cancel(ActionEvent event) throws IOException {
        App.setRoot(returnto);
    }

    @FXML
    void confirmPurchase(ActionEvent event) {

        // check legal name
        if (nameTF.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No name was given",
                    ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                return;
            }
        }

        // check legal payment option
        if (paymentInfoTF.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No payment information was given",
                    ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                return;
            }
        }

        if ((sent != null) && (sent.getMovieType().equals("Screening"))) {

            seatsNum = 0;
            takenseats = "";

            // check seats taken
            for (int i = 0; i < sent.getScreening().getRows(); i++) {
                for (int j = 0; j < sent.getScreening().getColumns(); j++) {
                    if (clickCount[i][j] % 2 == 1) {
                        seatsNum++;
                        takenseats += "(" + i + "," + j + ")";
                    }
                }
            }

            if (seatsNum == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No seats were selected",
                        ButtonType.OK);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    return;
                }
            }

            // take seats in theater
            for (int i = 0; i < sent.getScreening().getRows(); i++) {
                for (int j = 0; j < sent.getScreening().getColumns(); j++) {
                    if (takenseats.contains("(" + i + "," + j + ")")) {
                        TheBooth.takeSeat(sent.getScreening(), i, j);
                    }
                }
            }

            // add purchase to database
            TheBooth.addPurchase(nameTF.getText(), paymentInfoTF.getText(), takenseats, grandTotal, sent.getScreening().getScreeningId());

        } else if ((sent != null) && (sent.getMovieType().equals("LinkMovie"))) {

            // add purchase to database
            TheBooth.addLinkPurchase(nameTF.getText(), paymentInfoTF.getText(), grandTotal, sent.getLinkMovie().getMovieId());

        } else {

            // add purchase to database
            TheBooth.addSubscriptionPurchase(nameTF.getText(), paymentInfoTF.getText(), grandTotal);
            TheBooth.addSubscription(nameTF.getText());

        }

        // go back to navigation screen
        try {
            App.setRoot("screen_navigation");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shutdown() {
        // cleanup code here: unregister from EventBus.
        EventBus.getDefault().unregister(this);
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        EventBus.getDefault().register(this);

        // Getting data from last screen.
        Stage stage = App.getApp_stage();
        stage.setWidth(655);
        stage.setHeight(640);
        sent = (SendMovieEvent) stage.getUserData();

        // Determining data's type and handling it.
        if (sent != null) {
            returnto = "explore_movies";
            if (sent.getMovieType().equals("Screening")) {
                subButton.setVisible(true);
                subLabel.setVisible(true);
                subLine.setVisible(true);
                subTF.setVisible(true);
                showContentForScreening(sent.getScreening());
            } else {
                BorderPane.setTop(null);
                showContentForLink(sent.getLinkMovie());
            }
        } else {
            BorderPane.setTop(null);
            returnto = "screen_navigation";
            showContentForSubscription();
        }
    }

    void showContentForSubscription() {

        TitleLabel.setText("רכישת מנוי:");
        SecondaryLabel.setText("פרטים:");
        grandTotalTF.setText("Subtotal: 499₪");
        grandTotal = 499;

        InputStream stream = getClass().getResourceAsStream("posters/subscribers.png");
        if (stream == null) {
            throw new IllegalArgumentException("Could not find image posters/subscribers.png in resources!");
        }

        Image im = new Image(stream);
        ImageView iv = new ImageView(im); // Image
        iv.setFitHeight(150);
        iv.setFitWidth(640);
        iv.setSmooth(true);

        BorderPane.setCenter(iv);
    }

    void showContentForLink(LinkMovie l) {

        TitleLabel.setText("הזמנת לינק לסרט הנבחר:");
        SecondaryLabel.setText("הלינק של הסרט הנבחר:");

        // showing it in the Vbox
        String addToMovieData;
        String movieData;

        addToMovieData = String.format("Price: %s\n", l.getPrice());

        MovieTitle movie = l.getMovieTitle();
        movieData = String.format("Movie Id: %s\nHebrew Name: %s\nEnglish Name: %s\nGenres: %s\nProducer: %s\n" +
                        "Actors: %s\nDescription: %s\nYear: %s\n",
                movie.getMovieId(),
                movie.getHebrewName(),
                movie.getEnglishName(),
                movie.getGenres(),
                movie.getProducer(),
                movie.getActors(),
                movie.getMovieDescription(),
                movie.getYear()
        );

        movieData = movieData + addToMovieData;

        InputStream stream = getClass().getResourceAsStream(movie.getImagePath());
        if (stream == null) {
            throw new IllegalArgumentException("Could not find image " + movie.getImagePath() + " in resources!");
        }

        Image im = new Image(stream);

        String finalMovieData = movieData;

        // Create the HBox (movie row):
        HBox hBox = new HBox();

        ImageView iv = new ImageView(im); // Image
        iv.setFitHeight(175);
        iv.setFitWidth(150);
        iv.setSmooth(true);

        Label data = new Label(finalMovieData); // Text label
        data.setAlignment(Pos.CENTER_LEFT);
        data.setWrapText(true);
        data.setMaxWidth(350);

        hBox.getChildren().addAll(iv, data); // Add into the HBox
        hBox.setMargin(iv, new Insets(10, 10, 10, 10));
        hBox.setMargin(data, new Insets(10, 10, 10, 10));
        movieList.getChildren().add(hBox); // Add into the VBox

        // Showing the link
        Label linkLabel = new Label();
        linkLabel.setAlignment(Pos.CENTER);

        BorderPane.setCenter(linkLabel);
        linkLabel.setText(l.getLink());
        grandTotal += Integer.parseInt(l.getPrice());
        grandTotalTF.setText("Subtotal: " + grandTotal + "₪");
    }

    void showContentForScreening(Screening s) {

        TitleLabel.setText("הזמנת כרטיסים לסרט הנבחר:");
        SecondaryLabel.setText("הכיסאות שנבחרו:");

        clickCount = new int[s.getRows()][s.getColumns()];

        // showing it in the Vbox
        String addToMovieData;
        String movieData;

        addToMovieData = String.format("Price: %s\nTime: %s\nLocation: %s\nAvailable Seats: %s\n",
                s.getPrice(),
                s.getTime(),
                s.getLocation(),
                s.getAvailableSeats()
        );

        MovieTitle movie = s.getMovieTitle();
        movieData = String.format("Movie Id: %s\nHebrew Name: %s\nEnglish Name: %s\nGenres: %s\nProducer: %s\n" +
                        "Actors: %s\nDescription: %s\nYear: %s\n",
                movie.getMovieId(),
                movie.getHebrewName(),
                movie.getEnglishName(),
                movie.getGenres(),
                movie.getProducer(),
                movie.getActors(),
                movie.getMovieDescription(),
                movie.getYear()
        );

        movieData = movieData + addToMovieData;

        InputStream stream = getClass().getResourceAsStream(movie.getImagePath());
        if (stream == null) {
            throw new IllegalArgumentException("Could not find image " + movie.getImagePath() + " in resources!");
        }

        Image im = new Image(stream);
        String finalMovieData = movieData;

        // Create the HBox (movie row):
        HBox hBox = new HBox();

        ImageView iv = new ImageView(im); // Image
        iv.setFitHeight(175);
        iv.setFitWidth(150);
        iv.setSmooth(true);

        Label data = new Label(finalMovieData); // Text label
        data.setAlignment(Pos.CENTER_LEFT);
        data.setWrapText(true);
        data.setMaxWidth(350);

        hBox.getChildren().addAll(iv, data); // Add into the HBox
        hBox.setMargin(iv, new Insets(10, 10, 10, 10));
        hBox.setMargin(data, new Insets(10, 10, 10, 10));
        movieList.getChildren().add(hBox); // Add into the VBox

        // Showing the seats
        GridPane gridpane = new GridPane();
        gridpane.setHgap(1);
        gridpane.setVgap(1);
        BorderPane.setCenter(gridpane);
        gridpane.setAlignment(Pos.CENTER);
        for (int i = 0; i < s.getRows(); i++) {
            for (int j = 0; j < s.getColumns(); j++) {
                showseat(i, j, s, gridpane);
            }
        }
    }

    public void showseat(int row, int col, Screening s, GridPane g) {
        String seat = "(" + row + "," + col + ")";
        InputStream stream2;
        Image im;
        ImageView iv;
        String path;
        if (s.getTakenSeats().contains(seat)) {
            stream2 = getClass().getResourceAsStream("seat_selection_icons/occupied.png");
            path = "seat_selection_icons/occupied.png";
        } else {
            stream2 = getClass().getResourceAsStream("seat_selection_icons/unoccupied.png");
            path = "seat_selection_icons/unoccupied.png";
        }
        im = new Image(stream2);
        iv = new ImageView(im);
        iv.setOnMouseClicked((MouseEvent e) -> {
            if (!(path.equals("seat_selection_icons/occupied.png"))) {
                clickCount[row][col]++;
                if ((clickCount[row][col] % 2 == 0)) {
                    iv.setImage(new Image(getClass().getResourceAsStream("seat_selection_icons/unoccupied.png")));
                    grandTotal -= Integer.parseInt(s.getPrice());
                    grandTotalTF.setText("Subtotal: " + grandTotal + "₪");
                } else {
                    iv.setImage(new Image(getClass().getResourceAsStream("seat_selection_icons/selected.png")));
                    grandTotal += Integer.parseInt(s.getPrice());
                    grandTotalTF.setText("Subtotal: " + grandTotal + "₪");
                }
            }
        });
        iv.setFitHeight(12);
        iv.setFitWidth(12);
        iv.setSmooth(true);
        g.add(iv, col, row);
    }
}
