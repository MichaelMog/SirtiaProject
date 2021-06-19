/**
 * Sample Skeleton for 'update_content.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.MovieTitle;
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

public class UpdateContentController {
    /**
     * IMPORTANT PLEASE READ:
     * This class is almost identical to explore movies, except that when you show the filterable movie list each
     * movie has up to 2 buttons to the side of it: change show times / change price (if it is link or screening)
     * and delete (whether it is a movie title or any other movie instance).
     * The buttons should be assigned with the appropriate on-click commands.
     */
    @FXML
    public void initialize() {
        // Register to EventBus so we can subscribe to events when a movie is sent over by the server.
        EventBus.getDefault().register(this);
        branchTimeButton.setVisible(false);
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

    @FXML // fx:id="screeningCheckbox"
    private CheckBox screeningCheckbox; // Value injected by FXMLLoader

    @FXML // fx:id="branchTimeButton"
    private Button branchTimeButton; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="addMovieButton"
    private Button addMovieButton; // Value injected by FXMLLoader

    @FXML // fx:id="addComingSoonMovieButton"
    private Button addComingSoonMovieButton; // Value injected by FXMLLoader

    @FXML // fx:id="addLinkMovieButton"
    private Button addLinkMovieButton; // Value injected by FXMLLoader

    @FXML // fx:id="addScreeningButton"
    private Button addScreeningButton; // Value injected by FXMLLoader

    @FXML // fx:id="genresGrid"
    private GridPane genresGrid; // Value injected by FXMLLoader

    @FXML // fx:id="movieTypeGrid"
    private GridPane movieTypeGrid; // Value injected by FXMLLoader

    private String branchFilter = "";

    private String timeFilter = "";

    @FXML
    void back(ActionEvent event) {
        //TODO: set root to the login screen
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
    void showAddMovieAlert(ActionEvent event) {
        // Show a new alert that allows the user to enter movie title data and add it to the database.
        // See https://stackoverflow.com/questions/31556373/javafx-dialog-with-2-input-fields
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add a Movie!");

        // Set the button types.
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField hebrewNameTF = new TextField();
        hebrewNameTF.setPromptText("Type Hebrew Name");
        TextField englishNameTF = new TextField();
        englishNameTF.setPromptText("Type English Name");
        TextField genresTF = new TextField();
        genresTF.setPromptText("Type Genres");
        TextField producerTF = new TextField();
        producerTF.setPromptText("Type Producer");
        TextField actorsTF = new TextField();
        actorsTF.setPromptText("Type Actors");
        TextField movieDescriptionTF = new TextField();
        movieDescriptionTF.setPromptText("Type Movie Description");
        TextField imagePathTF = new TextField();
        imagePathTF.setPromptText("Type Image Path");
        TextField yearTF = new TextField();
        yearTF.setPromptText("Type Year");

        gridPane.add(new Label("Hebrew Name:"), 0, 0);
        gridPane.add(hebrewNameTF, 1, 0);
        gridPane.add(new Label("English Name:"), 0, 1);
        gridPane.add(englishNameTF, 1, 1);
        gridPane.add(new Label("Genres:"), 0, 3);
        gridPane.add(genresTF, 1, 3);
        gridPane.add(new Label("Producer:"), 0, 4);
        gridPane.add(producerTF, 1, 4);
        gridPane.add(new Label("Actors:"), 0, 5);
        gridPane.add(actorsTF, 1, 5);
        gridPane.add(new Label("Movie Description:"), 0, 6);
        gridPane.add(movieDescriptionTF, 1, 6);
        gridPane.add(new Label("Image Path:"), 0, 7);
        gridPane.add(imagePathTF, 1, 7);
        gridPane.add(new Label("Year:"), 0, 8);
        gridPane.add(yearTF, 1, 8);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> hebrewNameTF.requestFocus());

        // Convert the result to an #addMovieTitle string when the OK button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return "#addMovieTitle\t" +
                        hebrewNameTF.getText() + "\t" +
                        englishNameTF.getText() + "\t" +
                        genresTF.getText() + "\t" +
                        producerTF.getText() + "\t" +
                        actorsTF.getText() + "\t" +
                        movieDescriptionTF.getText() + "\t" +
                        imagePathTF.getText() + "\t" +
                        yearTF.getText();
            }
            return null;
        });

        dialog.showAndWait();
        if (dialog.getResult() != null &&
                !hebrewNameTF.getText().equals("") &&
                !englishNameTF.getText().equals("") &&
                !genresTF.getText().equals("") &&
                !producerTF.getText().equals("") &&
                !actorsTF.getText().equals("") &&
                !movieDescriptionTF.getText().equals("") &&
                !imagePathTF.getText().equals("") &&
                !yearTF.getText().equals("")) {
            sendCommand(dialog.getResult());
        }
    }

    @FXML
    void showAddComingSoonMovieAlert(ActionEvent event) {
        // Show a new alert that allows the user to enter coming soon movie data and add it to the database.
        // See https://stackoverflow.com/questions/31556373/javafx-dialog-with-2-input-fields
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add a Coming Soon Movie!");

        // Set the button types.
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField movieTitleIdTF = new TextField();
        movieTitleIdTF.setPromptText("Type Movie Title ID");
        TextField priceTF = new TextField();
        priceTF.setPromptText("Type Price");

        gridPane.add(new Label("Movie Title ID:"), 0, 0);
        gridPane.add(movieTitleIdTF, 1, 0);
        gridPane.add(new Label("Price:"), 0, 1);
        gridPane.add(priceTF, 1, 1);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> movieTitleIdTF.requestFocus());

        // Convert the result to an #addMovieTitle string when the OK button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return "#addComingSoonMovie\t" +
                        movieTitleIdTF.getText() + "\t" +
                        priceTF.getText();
            }
            return null;
        });

        dialog.showAndWait();
        if (dialog.getResult() != null &&
                !movieTitleIdTF.getText().equals("") &&
                !priceTF.getText().equals("")) {
            sendCommand(dialog.getResult());
        }
    }

    @FXML
    void showAddLinkMovieAlert(ActionEvent event) {
        // Show a new alert that allows the user to enter link movie data and add it to the database.
        // See https://stackoverflow.com/questions/31556373/javafx-dialog-with-2-input-fields
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add a Link Movie!");

        // Set the button types.
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField movieTitleIdTF = new TextField();
        movieTitleIdTF.setPromptText("Type Movie Title ID");
        TextField priceTF = new TextField();
        priceTF.setPromptText("Type Price");
        TextField linkTF = new TextField();
        linkTF.setPromptText("Type Link");
        TextField watchHoursTF = new TextField();
        watchHoursTF.setPromptText("Type Watch Hours");

        gridPane.add(new Label("Movie Title ID:"), 0, 0);
        gridPane.add(movieTitleIdTF, 1, 0);
        gridPane.add(new Label("Price:"), 0, 1);
        gridPane.add(priceTF, 1, 1);
        gridPane.add(new Label("Link:"), 0, 2);
        gridPane.add(linkTF, 1, 2);
        gridPane.add(new Label("Watch Hours:"), 0, 3);
        gridPane.add(watchHoursTF, 1, 3);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> movieTitleIdTF.requestFocus());

        // Convert the result to an #addMovieTitle string when the OK button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return "#addLinkMovie\t" +
                        movieTitleIdTF.getText() + "\t" +
                        priceTF.getText() + "\t" +
                        linkTF.getText() + "\t" +
                        watchHoursTF.getText();
            }
            return null;
        });

        dialog.showAndWait();
        if (dialog.getResult() != null &&
                !movieTitleIdTF.getText().equals("") &&
                !priceTF.getText().equals("") &&
                !linkTF.getText().equals("") &&
                !watchHoursTF.getText().equals("")) {
            sendCommand(dialog.getResult());
        }
    }

    @FXML
    void showAddScreeningAlert(ActionEvent event) {
        // Show a new alert that allows the user to enter link movie data and add it to the database.
        // See https://stackoverflow.com/questions/31556373/javafx-dialog-with-2-input-fields
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add a Screening!");

        // Set the button types.
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField movieTitleIdTF = new TextField();
        movieTitleIdTF.setPromptText("Type Movie Title ID");
        TextField priceTF = new TextField();
        priceTF.setPromptText("Type Price");
        TextField timeTF = new TextField();
        timeTF.setPromptText("Type Time");
        TextField locationTF = new TextField();
        locationTF.setPromptText("Type Location");
        TextField rowsTF = new TextField();
        rowsTF.setPromptText("Type Seat Rows");
        TextField columnsTF = new TextField();
        columnsTF.setPromptText("Type Seat Columns");

        gridPane.add(new Label("Movie Title ID:"), 0, 0);
        gridPane.add(movieTitleIdTF, 1, 0);
        gridPane.add(new Label("Price:"), 0, 1);
        gridPane.add(priceTF, 1, 1);
        gridPane.add(new Label("Time:"), 0, 2);
        gridPane.add(timeTF, 1, 2);
        gridPane.add(new Label("Location:"), 0, 3);
        gridPane.add(locationTF, 1, 3);
        gridPane.add(new Label("Seat Rows:"), 0, 4);
        gridPane.add(rowsTF, 1, 4);
        gridPane.add(new Label("Seat Columns:"), 0, 5);
        gridPane.add(columnsTF, 1, 5);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> movieTitleIdTF.requestFocus());

        // Convert the result to an #addMovieTitle string when the OK button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return "#addScreening\t" +
                        movieTitleIdTF.getText() + "\t" +
                        priceTF.getText() + "\t" +
                        timeTF.getText() + "\t" +
                        locationTF.getText() + "\t" +
                        rowsTF.getText() + "\t" +
                        columnsTF.getText();
            }
            return null;
        });

        dialog.showAndWait();
        if (dialog.getResult() != null &&
                !movieTitleIdTF.getText().equals("") &&
                !priceTF.getText().equals("") &&
                !timeTF.getText().equals("") &&
                !locationTF.getText().equals("") &&
                !rowsTF.getText().equals("") &&
                !columnsTF.getText().equals("")) {
            sendCommand(dialog.getResult());
        }
    }

    @FXML
    void showMovies(ActionEvent event) {
        movieList.getChildren().removeAll(movieList.getChildren()); //Clear current list of movies.
        sendCommand("#showMovies"); //Sends a request to the server to send movies
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
                addToMovieData = String.format("Price: %s\nScreening Time: %s\nScreening Location: %s\nScreening" +
                                " Rows: %s\nScreening Columns: %s\nScreening Available Seats: %s\n",
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

            Button deleteButton = createDeleteButton(event);

            if (event.getMovieType().equals("LinkMovie") || event.getMovieType().equals("Screening")) {
                int id;
                if (event.getMovieType().equals("LinkMovie")) id = event.getLinkMovie().getMovieId();
                else id = event.getScreening().getScreeningId();
                Button priceButton = createChangePriceButton(event.getMovieType(), id, movie);
                Button showTimesButton = createChangeShowTimesButton(event.getMovieType(), id, movie);
                hBox.getChildren().addAll(iv, data, priceButton, showTimesButton, deleteButton); // Add into the HBox with the purchase button
                hBox.setMargin(priceButton, new Insets(10, 10, 10, 10));
                hBox.setMargin(showTimesButton, new Insets(10, 10, 10, 10));
            } else {
                hBox.getChildren().addAll(iv, data, deleteButton); // Add into the HBox
            }
            hBox.setMargin(iv, new Insets(10, 10, 10, 10));
            hBox.setMargin(data, new Insets(10, 10, 10, 10));
            hBox.setMargin(deleteButton, new Insets(10, 10, 10, 10));
            hBox.setAlignment(Pos.CENTER_LEFT);
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

    Button createChangePriceButton(String movieType, int movieId, MovieTitle movie) {
        Button button = new Button();
        button.setMaxWidth(400);
        button.setText("Change Price");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Show a new alert that allows the user to pick a price.
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Request to change " + movie.getEnglishName() + "'s admission price.");

                // Set the button types.
                ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(20, 150, 10, 10));

                TextField priceTF = new TextField();
                priceTF.setPromptText("Type New Price");

                gridPane.add(new Label("Price:"), 0, 0);
                gridPane.add(priceTF, 1, 0);

                dialog.getDialogPane().setContent(gridPane);

                // Request focus on the username field by default.
                Platform.runLater(() -> priceTF.requestFocus());

                // Convert the result to a branch-time-pair when the OK button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == okButtonType) {
                        return priceTF.getText();
                    }
                    return null;
                });

                dialog.showAndWait();
                if (dialog.getResult() == null || dialog.getResult() == "") {
                    sendCommand("#requestPriceChange\t" + movieType + "\t" + movieId + "\t" + "cancel");
                } else {
                    sendCommand("#requestPriceChange\t" + movieType + "\t" + movieId + "\t" + dialog.getResult());
                }
            }
        });

        return button;
    }

    Button createChangeShowTimesButton(String movieType, int movieId, MovieTitle movie) {
        Button button = new Button();
        button.setMaxWidth(400);
        button.setText("Change Show Times");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Show a new alert that allows the user to pick a price.
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Request to the show times of " + movie.getEnglishName());

                // Set the button types.
                ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(20, 150, 10, 10));

                TextField showtimesTF = new TextField();
                showtimesTF.setPromptText("Type New Show Times");

                gridPane.add(new Label("Show Times:"), 0, 0);
                gridPane.add(showtimesTF, 1, 0);

                dialog.getDialogPane().setContent(gridPane);

                // Request focus on the username field by default.
                Platform.runLater(() -> showtimesTF.requestFocus());

                // Convert the result to a branch-time-pair when the OK button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == okButtonType) {
                        return showtimesTF.getText();
                    }
                    return null;
                });

                dialog.showAndWait();
                if (dialog.getResult() == null || dialog.getResult() == "") {
                    sendCommand("#requestShowTimesChange\t" + movieType + "\t" + movieId + "\t" + "cancel");
                } else {
                    sendCommand("#requestShowTimesChange\t" + movieType + "\t" + movieId + "\t" + dialog.getResult());
                }
            }
        });

        return button;
    }

    Button createDeleteButton(SendMovieEvent event) {
        MovieTitle movie;
        String command;
        switch (event.getMovieType()) {
            case "MovieTitle":
                movie = event.getMovieTitle();
                command = "#removeMovieTitle\t" + event.getMovieTitle().getMovieId();
                break;
            case "ComingSoonMovie":
                movie = event.getComingSoonMovie().getMovieTitle();
                command = "#removeComingSoonMovie\t" + event.getComingSoonMovie().getMovieId();
                break;
            case "LinkMovie":
                movie = event.getLinkMovie().getMovieTitle();
                command = "#removeLinkMovie\t" + event.getLinkMovie().getMovieId();
                break;
            case "Screening":
                movie = event.getScreening().getMovieTitle();
                command = "#removeScreening\t" + event.getScreening().getScreeningId();
                break;
            default:
                movie = event.getMovieTitle();
                command = "#removeMovieTitle";
                System.err.println("Movie Type Incorrect!");
        }
        Button button = new Button();
        button.setMaxWidth(400);
        button.setText("Delete Movie");
        String movieType = event.getMovieType();
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Show a confirmation dialog to prevent mis-clicks.
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete [" + movieType + "] " +
                        movie.getEnglishName() + " from the database?",
                        ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    sendCommand(command);
                }
            }
        });

        return button;
    }
}
