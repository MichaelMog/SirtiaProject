package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieTitle;
import il.cshaifasweng.OCSFMediatorExample.entities.StagedPriceChange;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ManagerScreenController {
    @FXML
    public void initialize() {
        // Register to EventBus so we can subscribe to events when a movie is sent over by the server.
        EventBus.getDefault().register(this);
        branchTimeButton.setVisible(false);
        App.getApp_stage().setHeight(605);
        App.getApp_stage().setWidth(1000);
        App.getApp_stage().setTitle("מסך ניהול");
    }

    public void shutdown() {
        // cleanup code here: unregister from EventBus.
        EventBus.getDefault().unregister(this);
    }

    private List<StagedPriceChange> priceChangeList; // On every search this gets updated.

    @FXML // fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader

    @FXML // fx:id="movieList"
    private VBox movieList; // Value injected by FXMLLoader

    @FXML // fx:id="searchButton"
    private Button searchButton; // Value injected by FXMLLoader

    @FXML // fx:id="screeningCheckbox"
    private CheckBox screeningCheckbox; // Value injected by FXMLLoader

    @FXML // fx:id="branchTimeButton"
    private Button branchTimeButton; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="addMovieButton"
    private Button addMovieButton; // Value injected by FXMLLoader

    @FXML // fx:id="genresGrid"
    private GridPane genresGrid; // Value injected by FXMLLoader

    @FXML // fx:id="movieTypeGrid"
    private GridPane movieTypeGrid; // Value injected by FXMLLoader

    @FXML // fx:id="showPriceChangesOnly"
    private CheckBox showPriceChangesOnly; // Value injected by FXMLLoader

    private String branchFilter = "";

    private String timeFilter = "";

    @FXML
    void back(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        App.setRoot("screen_navigation");
    }

    @FXML
    void clearSelection(ActionEvent event) {
        for (Node child : genresGrid.getChildren()) {
            CheckBox cb = (CheckBox) child;
            if (cb.isSelected())
                cb.setSelected(false);
        }
        Boolean typeSelected = false;
        for (Node child : movieTypeGrid.getChildren()) {
            CheckBox cb = (CheckBox) child;
            if (cb.isSelected())
                cb.setSelected(false);
        }
        branchTimeButton.setVisible(false);
    }

    @FXML
    void flipBranchDateButtonVisibility(ActionEvent event) {
        if (screeningCheckbox.isSelected()) {
            branchTimeButton.setVisible(true);
        } else {
            branchTimeButton.setVisible(false);
        }
    }

    @FXML
    void showBranchTimeAlert(ActionEvent event) {
        // Show a new alert that allows the user to pick a branch and date to filter theater movies by.
        // See https://stackoverflow.com/questions/31556373/javafx-dialog-with-2-input-fields
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Branch & Date Selection");

        // Set the button types.
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField branch = new TextField();
        branch.setPromptText("Type Branch");
        TextField time = new TextField();
        time.setPromptText("Type Time");

        gridPane.add(new Label("Branch:"), 0, 0);
        gridPane.add(branch, 1, 0);
        gridPane.add(new Label("Time:"), 2, 0);
        gridPane.add(time, 3, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> branch.requestFocus());

        // Convert the result to a branch-time-pair when the OK button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(branch.getText(), time.getText());
            }
            return null;
        });

        dialog.showAndWait();
        if (dialog.getResult() != null) {
            branchFilter = dialog.getResult().getKey().toString();
            timeFilter = dialog.getResult().getValue().toString();
        } else {
            branchFilter = "";
            timeFilter = "";
        }
    }

    @FXML
    void showMovies(ActionEvent event) {
        movieList.getChildren().removeAll(movieList.getChildren()); // Clear current list of movies.
        // Send to the server a request to update the price changes list. Once updated, proceeds to show the movie list.
        sendCommand("#getStagedPriceChanges");
    }

    private boolean justUpdatePriceList = false; // Used below. Always set to true before calling #getStagedPriceChanges just updating.

    @Subscribe
    public void continueShowMovies(SendPriceChangesListEvent event) {
        priceChangeList = event.getPriceChangeList();
        if (!justUpdatePriceList)
            sendCommand("#showMovies");
        justUpdatePriceList = false;
    }

    @Subscribe
    public void serverForceMovieListClear(ForceClearEvent event) {
        Platform.runLater(() -> {
            movieList.getChildren().removeAll(movieList.getChildren()); // Clear current list of movies.
        });
    }

    @Subscribe
    public void showMovie(SendMovieEvent event) {
        /*
         * Handle receiving a movie from the server. (Write here later)
         */

        // Set the describing text:
        String movieData;
        String addToMovieData = "";
        MovieTitle movie;
        switch (event.getMovieType()) {
            case "MovieTitle":
                movie = event.getMovieTitle();
                addToMovieData = "";
                break;
            case "ComingSoonMovie":
                movie = event.getComingSoonMovie().getMovieTitle();
                addToMovieData = String.format("Price: %s\n", event.getComingSoonMovie().getPrice());
                break;
            case "LinkMovie":
                movie = event.getLinkMovie().getMovieTitle();
                addToMovieData = String.format("Price: %s\nLink: %s\nWatch Hours: %s\n",
                        event.getLinkMovie().getPrice(),
                        event.getLinkMovie().getLink(),
                        event.getLinkMovie().getWatchHours()
                );
                break;
            case "Screening":
                movie = event.getScreening().getMovieTitle();
                addToMovieData = String.format("Price: %s\nTime: %s\nLocation: %s\nRows: %s\nColumns: %s\nAvailable Seats: %s\n",
                        event.getScreening().getPrice(),
                        event.getScreening().getTime(),
                        event.getScreening().getLocation(),
                        event.getScreening().getRows(),
                        event.getScreening().getColumns(),
                        event.getScreening().getAvailableSeats()
                );
                break;
            default:
                movie = event.getMovieTitle();
                System.err.println("Movie Type Incorrect!");
        }
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

        // Check for filter matching:
        // If no check box is ticked, treated as if all of them are.
        Boolean genreSelected = false;
        for (Node child : genresGrid.getChildren()) {
            CheckBox cb = (CheckBox) child;
            if (cb.isSelected())
                genreSelected = true;
        }
        Boolean typeSelected = false;
        for (Node child : movieTypeGrid.getChildren()) {
            CheckBox cb = (CheckBox) child;
            if (cb.isSelected())
                typeSelected = true;
        }

        if (genreSelected) {
            for (Node child : genresGrid.getChildren()) {
                CheckBox cb = (CheckBox) child;
                if (cb.isSelected()) {
                    for (String data : movieData.split("\n")) {
                        if (data.startsWith("Genres: ") && !data.contains(cb.getText())) {
                            return;
                        }
                    }
                }
            }
        }

        if (typeSelected) {
            switch (event.getMovieType()) {
                case "MovieTitle":
                    for (Node child : movieTypeGrid.getChildren()) {
                        CheckBox cb = (CheckBox) child;
                        if (cb.getText().equals("Movie Title") && !cb.isSelected())
                            return;
                    }
                    break;
                case "ComingSoonMovie":
                    for (Node child : movieTypeGrid.getChildren()) {
                        CheckBox cb = (CheckBox) child;
                        if (cb.getText().equals("Coming Soon") && !cb.isSelected()) {
                            return;
                        }
                    }
                    break;
                case "LinkMovie":
                    for (Node child : movieTypeGrid.getChildren()) {
                        CheckBox cb = (CheckBox) child;
                        if (cb.getText().equals("Watch at Home") && !cb.isSelected()) {
                            return;
                        }
                    }
                    break;
                case "Screening":
                    for (Node child : movieTypeGrid.getChildren()) {
                        CheckBox cb = (CheckBox) child;
                        if (cb.getText().equals("Screening") && !cb.isSelected()) {
                            return;
                        } else {
                            for (String data : movieData.split("\n")) {
                                if (data.startsWith("Screening Time: ") && !data.substring(16).contains(timeFilter))
                                    return;
                                if (data.startsWith("Screening Location: ") && !data.substring(20).contains(branchFilter))
                                    return;
                            }
                        }
                    }
                    break;
            }
        }

        // Read the image from the path specified inside the movie:
        InputStream stream = getClass().getResourceAsStream(movie.getImagePath());
        if (stream == null) {
            throw new IllegalArgumentException("Could not find image " + movie.getImagePath() + " in resources!");
        }
        Image im = new Image(stream);

        // Add the image and the text label to the HBox, then add the HBox to the bottom of movieList (the VBox).
        String finalMovieData = movieData; // label complaining about the string being changed inside lambda
        Platform.runLater(() -> {
            // Create the button to change show times:
            // Based on https://www.geeksforgeeks.org/javafx-textinputdialog/:


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

            boolean isPriceChange = false;

            if (event.getMovieType().equals("LinkMovie") || event.getMovieType().equals("Screening")) {
                for (StagedPriceChange priceChange : priceChangeList) {
                    if (priceChange.getMovieType().equals(event.getMovieType()) && event.getMovieType().equals("LinkMovie")) {
                        if (priceChange.getMovieId() == event.getLinkMovie().getMovieId()) {
                            isPriceChange = true;
                            Button button = createActionButton(event, priceChange.getNewPrice());
                            hBox.getChildren().addAll(iv, data, button); // Add into the HBox with the purchase button
                            hBox.setMargin(button, new Insets(10, 10, 10, 10));
                        }
                    } else if (priceChange.getMovieType().equals(event.getMovieType()) && event.getMovieType().equals("Screening")) {
                        if (priceChange.getMovieId() == event.getScreening().getScreeningId()) {
                            isPriceChange = true;
                            Button button = createActionButton(event, priceChange.getNewPrice());
                            hBox.getChildren().addAll(iv, data, button); // Add into the HBox with the purchase button
                            hBox.setMargin(button, new Insets(10, 10, 10, 10));
                        }
                    }
                }
                if (!isPriceChange)
                    hBox.getChildren().addAll(iv, data); // Add into the HBox because no button was added earlier
            } else {
                hBox.getChildren().addAll(iv, data); // Add into the HBox
            }
            hBox.setFillHeight(false);
            hBox.setMargin(iv, new Insets(10, 10, 10, 10));
            hBox.setMargin(data, new Insets(10, 10, 10, 10));
            hBox.setAlignment(Pos.CENTER_LEFT);
            if (!showPriceChangesOnly.isSelected() || isPriceChange)
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

    Button createActionButton(SendMovieEvent event, String newPrice) {
        MovieTitle movie;
        String acceptCommand, rejectCommand;
        String currPrice;
        switch (event.getMovieType()) {
            case "LinkMovie":
                movie = event.getLinkMovie().getMovieTitle();
                acceptCommand = String.format("#priceChange\t%s\t%s\t%s", event.getMovieType(),
                        event.getLinkMovie().getMovieId(), newPrice);
                rejectCommand = String.format("#requestPriceChange\t%s\t%s\t%s", event.getMovieType(),
                        event.getLinkMovie().getMovieId(), "cancel");
                currPrice = event.getLinkMovie().getPrice();
                break;
            case "Screening":
                movie = event.getScreening().getMovieTitle();
                acceptCommand = String.format("#priceChange\t%s\t%s\t%s", event.getMovieType(),
                        event.getScreening().getScreeningId(), newPrice);
                rejectCommand = String.format("#requestPriceChange\t%s\t%s\t%s", event.getMovieType(),
                        event.getScreening().getScreeningId(), "cancel");
                currPrice = event.getScreening().getPrice();
                break;
            default:
                System.err.println("Movie Type Incorrect! Expected LinkMovie or Screening when defining the price" +
                        "action (accept/reject) button!");
                acceptCommand = rejectCommand = currPrice = "";
                movie = event.getMovieTitle(); // This shouldn't be executed.
        }
        Button button = new Button();
        button.setMaxWidth(400);
        button.setText("Change Price:\n" + currPrice + " --> " + newPrice);
        button.setAlignment(Pos.CENTER);
        String movieType = event.getMovieType();
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Show a confirmation dialog to prevent mis-clicks.
                // Set the button types.
                ButtonType acceptButtonType = new ButtonType("Accept", ButtonBar.ButtonData.YES);
                ButtonType rejectButtonType = new ButtonType("Reject", ButtonBar.ButtonData.NO);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Accept price changes to [" + movieType + "] " +
                        movie.getEnglishName() + "?",
                        acceptButtonType, rejectButtonType, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == acceptButtonType) {
                    sendCommand(rejectCommand); // Deletes the staged price.
                    justUpdatePriceList = true; // Update staged price list to avoid useless price change buttons
                    sendCommand("#getStagedPriceChanges");
                    sendCommand(acceptCommand); // Will update the price and thus every client's movie list.
                }
                if (alert.getResult() == rejectButtonType) {
                    sendCommand(rejectCommand);
                    showMovies(null); // Update movie list to remove the rejected price change offer.
                }
            }
        });
        return button;
    }

}