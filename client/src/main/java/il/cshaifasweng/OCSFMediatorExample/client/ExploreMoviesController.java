/**
 * Sample Skeleton for 'explore_movies.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieTitle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class ExploreMoviesController {
    @FXML
    public void initialize() {
        // Register to EventBus so we can subscribe to events when a movie is sent over by the server.
        EventBus.getDefault().register(this);
    }

    public void shutdown() {
        // cleanup code here: unregister from EventBus.
        EventBus.getDefault().unregister(this);
    }

    @FXML // fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader

    @FXML // fx:id="movieList"
    private VBox movieList; // Value injected by FXMLLoader

    @FXML // fx:id="searchButton"
    private Button searchButton; // Value injected by FXMLLoader

    @FXML // fx:id="theaterCheckbox"
    private CheckBox theaterCheckbox; // Value injected by FXMLLoader

    @FXML // fx:id="branchDateButton"
    private Button branchDateButton; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="addMovieButton"
    private Button addMovieButton; // Value injected by FXMLLoader


    @FXML
    void addMovieAlert(ActionEvent event) {
        //TODO: Show alert that has text fields for each movie data item and a confirm button that adds it to DB
    }

    @FXML
    void back(ActionEvent event) {
        //TODO: set root to the login screen
    }

    @FXML
    void clearSelection(ActionEvent event) {
        //TODO: Untick all of the checkboxes
    }

    @FXML
    void flipBranchDateButtonVisibility(ActionEvent event) {
        //TODO: Flip the branch / date selection button visibility.
    }

    @FXML
    void showBranchDateAlert(ActionEvent event) {
        //TODO: Show a new alert that allows the user to pick a branch and date to filter theater movies by.
    }

    @FXML
    void showMovies(ActionEvent event) {
        //TODO: Check which checkboxes are ticked (see the answer by Neel Sanchala on
        // https://stackoverflow.com/questions/22882791/javafx-check-if-a-checkbox-is-ticked)
        // use that information to send a filter request* to the server and let the server return only the movies that
        // fit the filter.
        // Could use the code from the prototype, but make sure NOT to show the change show times buttons.
        // *either send a long tab-separated string as done with all the commands OR send an instance of a Filter class
        // (which you'll have to create) and add a Filter.class handler inside handleMessageFromClient(). IMO 1st is
        // the better approach.

        //TODO: For link / screening movies add a button to purchase.

//        movieList.getChildren().removeAll(movieList.getChildren()); //Clear current list of movies.
//        sendCommand("#showMovies\t"+filter_string); //Sends a request to the server to send movies
    }

    @Subscribe
    public void showMovie(MovieTitleEvent event) {
        /*
         * Handle receiving a movie from the server.
         * This method adds a HBox to bottom of the Scroll-VBox of the movie list.
         * Each HBox contains an image and a describing text.
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
        InputStream stream = getClass().getResourceAsStream(event.getMovie().getImagePath());
        if (stream == null) {
            throw new IllegalArgumentException("Could not find image " + event.getMovie().getImagePath() + " in resources!");
        }
        Image im = new Image(stream);

        // Add the image and the text label to the HBox, then add the HBox to the bottom of movieList (the VBox).
        Platform.runLater(() -> {
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

            hBox.setMargin(iv, new Insets(10, 10, 10, 10));
            hBox.setMargin(data, new Insets(10, 10, 10, 10));
            hBox.setAlignment(Pos.CENTER);
            movieList.getChildren().add(hBox); // Add into the VBox
        });
    }
}
