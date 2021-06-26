package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    void confirmPurchase(ActionEvent event) {

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
            clickCount[col][row]++;
            if(clickCount[col][row]%2==0){
                iv.setImage(new Image(getClass().getResourceAsStream("seat_selection_icons/unoccupied.png")));
            }
            else{
                iv.setImage(new Image(getClass().getResourceAsStream("seat_selection_icons/selected.png")));
            }
        });
        iv.setFitHeight(12);
        iv.setFitWidth(12);
        iv.setSmooth(true);
        g.add(iv, col, row);
    }
}
