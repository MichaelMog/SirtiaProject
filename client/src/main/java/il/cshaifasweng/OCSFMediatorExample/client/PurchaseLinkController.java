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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PurchaseLinkController {

    private boolean run = false;
    int grandTotal = 0;
    LinkMovie current;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane thePane;

    @FXML
    private ScrollPane selectedMovie;

    @FXML
    private VBox movieList;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField paymentInfoTF;

    @FXML
    private Button confirmButton;

    @FXML
    private Label grandTotalTF;

    @FXML
    private Label linkLabel;

    @FXML
    void confirmPurchase(ActionEvent event) {

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

        // add purchase to database
        TheBooth.addLinkPurchase(nameTF.getText(), paymentInfoTF.getText(), grandTotal, current.getMovieId());

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
        // getting Link from last scene
        Stage stage = (Stage) selectedMovie.getScene().getWindow();
        SendMovieEvent sent = (SendMovieEvent) stage.getUserData();
        LinkMovie l = sent.getLinkMovie();
        current = l;

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
        movieList.getChildren().add(hBox); // Add into the VBox

        // Showing the link
        linkLabel.setText(l.getLink());
        grandTotal+=Integer.parseInt(l.getPrice());
        grandTotalTF.setText("Grand Total: " + grandTotal);
    }
}
