package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PurchaseTicketsController {

    private boolean run = false;
    private int[][] clickCount;
    int grandTotal = 0;
    Screening current;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ScrollPane selectedMovie;

    @FXML
    private VBox movieList;

    @FXML
    private Pane seatMap;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField paymentInfoTF;

    @FXML
    private Button confirmButton;

    @FXML
    private Pane thePane;

    @FXML
    private Label grandTotalTF;

    @FXML
    void confirmPurchase(ActionEvent event) {

        int seatsNum = 0;
        String takenseats = "";

        // check seats taken
        for (int i = 0; i < current.getRows(); i++) {
            for (int j = 0; j < current.getColumns(); j++) {
                if (clickCount[i][j] % 2 == 1) {
                    seatsNum++;
                    takenseats += "(" + i + "," + j + ")";
                }
            }
        }
        if (seatsNum == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No seats selected",
                    ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                return;
            }
        }

        // check legal name
        MovieTitle movie = current.getMovieTitle();
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

        // take seats in theater
        for (int i = 0; i < current.getRows(); i++) {
            for (int j = 0; j < current.getColumns(); j++) {
                if (takenseats.contains("(" + i + "," + j + ")")) {
                    TheBooth.takeSeat(current, i, j);
                }
            }
        }

        // add purchase to database
        TheBooth.addPurchase(nameTF.getText(), paymentInfoTF.getText(), takenseats, grandTotal, current.getScreeningId());

        // send costumer text with purchase details


        // go back to movie explorer
        Stage stage = (Stage) movieList.getScene().getWindow();
        stage.close();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("explore_movies.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("explore movies");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void contentShow() {
        if (run) {
            return;
        } else {
            run = true;
        }
        // getting screening from last scene
        Stage stage = (Stage) selectedMovie.getScene().getWindow();
        SendMovieEvent sent = (SendMovieEvent) stage.getUserData();
        Screening s = sent.getScreening();
        current = s;
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
        movieList.getChildren().add(hBox); // Add into the VBox

        // Showing the seats
        GridPane gridpane = new GridPane();
        seatMap.getChildren().add(gridpane);
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
                    grandTotalTF.setText("Grand Total: " + grandTotal);
                } else {
                    iv.setImage(new Image(getClass().getResourceAsStream("seat_selection_icons/selected.png")));
                    grandTotal += Integer.parseInt(s.getPrice());
                    grandTotalTF.setText("Grand Total: " + grandTotal);
                }
            }
        });
        iv.setFitHeight(12);
        iv.setFitWidth(12);
        iv.setSmooth(true);
        g.add(iv, col, row);
    }
}
