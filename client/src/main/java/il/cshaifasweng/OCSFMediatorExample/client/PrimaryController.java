package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieTitle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class PrimaryController {
    //Useful site: https://o7planning.org/10625/javafx-hbox-vbox
    @FXML
    public void initialize() {
        // Register to EventBus so we can subscribe to events when a movie is sent over by the server.
        EventBus.getDefault().register(this);
    }

    public void shutdown() {
        // cleanup code here: unregister from EventBus.
        EventBus.getDefault().unregister(this);
    }

    @FXML // fx:id="wrnBtn"
    private Button wrnBtn; // Value injected by FXMLLoader

    @FXML // fx:id="movieBtn"
    private Button movieBtn; // Value injected by FXMLLoader

    @FXML // fx:id="movieList"
    private VBox movieList; // Value injected by FXMLLoader

    @FXML
    void sendWarning(ActionEvent event) {
        sendCommand("#warning");
    }

    @FXML
    void showMovies(ActionEvent event) {
        movieList.getChildren().removeAll(movieList.getChildren()); //Clear current list of movies.
        sendCommand("#showMovies"); //Sends a request to the server to send movies
    }

    @Subscribe
    public void showMovie(MovieTitleEvent event) {
        /*
         * Handle receiving a movie from the server.
         * Currently, this method adds a HBox to bottom of the Scroll-VBox of the movie list.
         * Each HBox contains an image and a describing text and a button to update show times.
         */

        MovieTitle movie = event.getMovie();
        // Set the describing text:
        String movieData = String.format("Movie Id: %s\nHebrew Name: %s\nEnglish Name: %s\nGenres: %s\nProducer: %s\n" +
                        "Actors: %s\nDescription: %s\nShow Times: %s\n",
                movie.getMovieId(),
                movie.getHebrewName(),
                movie.getEnglishName(),
                movie.getGenres(),
                movie.getProducer(),
                movie.getActors(),
                movie.getMovieDescription(),
                movie.getShowTimes()
        );

        // Read the image from the path specified inside the movie:
        InputStream stream = null;
        try {
            stream = new FileInputStream(event.getMovie().getImagePath());
        } catch (FileNotFoundException e) {
            System.out.println("Could not find image on path" + event.getMovie().getImagePath());
            e.printStackTrace();
        }
        assert stream != null;
        Image im = new Image(stream);

        // Add the image and the text label to the HBox, then add the HBox to the bottom of movieList (the VBox).
        Platform.runLater(() -> {
            // Create the button to change show times:
            // Based on https://www.geeksforgeeks.org/javafx-textinputdialog/:
            TextInputDialog td = new TextInputDialog();
            td.setHeaderText("Enter new show times");
            Button button = new Button();
            button.setMaxWidth(400);
            button.setText("Change Show Times");
            button.setId(Integer.toString(movie.getMovieId()));
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Show the text input dialog on click
                    Optional<String> result = td.showAndWait();
                    // When the OK button is clicked, send a request to the server to change show times of movie of
                    // this ID to be what the user typed.
                    if (result.isPresent()){
                        sendCommand("#changeShowTimes\t" + button.getId() + "\t" + result.get());
                    }
                }
            });

            // Create the HBox (movie row):
            HBox hBox = new HBox();

            ImageView iv = new ImageView(im); // Image
            iv.setFitHeight(175);
            iv.setFitWidth(150);
            iv.setSmooth(true);

            Label data = new Label(movieData); // Text label
            data.setAlignment(Pos.CENTER_LEFT);
            data.setWrapText(true);
            data.setMaxWidth(350);

            hBox.getChildren().addAll(iv, data, button); // Add into the HBox
            hBox.setMargin(iv, new Insets(10, 10, 10, 10));
            hBox.setMargin(data, new Insets(10, 10, 10, 10));
            hBox.setMargin(button, new Insets(10, 10, 10, 10));
            hBox.setAlignment(Pos.CENTER);
            HBox.setHgrow(button, Priority.ALWAYS);
            movieList.getChildren().add(hBox); // Add into the VBox
        });
    }


    void sendCommand(String command) {
        // Send a command to the server.
        try {
            SimpleClient.getClient().sendToServer(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
