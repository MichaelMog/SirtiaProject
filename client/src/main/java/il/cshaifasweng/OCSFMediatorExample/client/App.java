package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.SystemUser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage appStage;
    private SimpleClient client;
    private SystemUser sysUser;
    private static int Y = 0;
    private static boolean purpleOutline = false;

    @Override
    public void start(Stage stage) throws IOException {
        appStage = stage;

        EventBus.getDefault().register(this);

        clientSetup(stage); // Create a connection to server

        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            controller.shutdown();
            Platform.exit();
        });

    }

    private void clientSetup(Stage stage) {
        // Connect to server
        // Shows the stage if successful otherwise exits the App.
        client = SimpleClient.getClient(); // Try to connect the default way
        try {
            client.openConnection();
            stage.show();
        } catch (IOException ioException) {
            // Couldn't connect the default way so offering to try another IP + port address.
            Platform.runLater(() -> {
                // Show a new alert that allows the user to pick server IP and port.
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Server Connection Setup");

                // Set the button types.
                ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(20, 150, 10, 10));

                TextField ipTF = new TextField();
                ipTF.setPromptText("Server IP");
                ipTF.setText("127.0.0.1");

                TextField portTF = new TextField();
                portTF.setPromptText("Server Port");
                portTF.setText("3000");

                gridPane.add(new Label("Server IP:"), 0, 0);
                gridPane.add(ipTF, 1, 0);
                gridPane.add(new Label("Server Port:"), 0, 1);
                gridPane.add(portTF, 1, 1);

                dialog.getDialogPane().setContent(gridPane);

                // Convert the result to a ip-port-pair when the OK button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == okButtonType) {
                        return new Pair<>(ipTF.getText(), portTF.getText());
                    }
                    return null;
                });

                dialog.showAndWait();

                if (dialog.getResult() == null || dialog.getResult().getKey().equals("") || dialog.getResult().getValue().equals("")) {
                    System.out.println("Client wasn't given a server to connect to.");
                    System.exit(0);
                } else {
                    try {
                        client = SimpleClient.getClient(dialog.getResult().getKey(), Integer.parseInt(dialog.getResult().getValue()));
                        client.openConnection();
                    } catch (Exception exception) {
                        System.err.println("Couldn't connect to given server.");
                        exception.printStackTrace();
                        System.exit(1);
                    }
                    stage.show();
                }
            });
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        super.stop();
    }

    @Subscribe
    public void onWarningEvent(WarningEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING,
                    String.format("Message: %s\nTimestamp: %s\n",
                            event.getWarning().getMessage(),
                            event.getWarning().getTime().toString())
            );
            alert.show();
        });
    }

    @Subscribe
    public void showConfirmation(PurchaseEvent event) {
        /* Show purchase confirmation */
        // set text to be displayed
        String text;
        if (event.getMovieDetail() != null) {
            text = "Customer name: " + event.getCustomer_name() + "\nPurchase ID: " + event.getPurchase_id() + "\nPayment information: " + event.getPayment_info() + "\nPurchase time: " + event.getPurchase_time() + "\nSubtotal: " + event.getPrice() + "\nMovie details: " + event.getMovieDetail();
        } else {
            text = "Customer name: " + event.getCustomer_name() + "\nPurchase ID: " + event.getPurchase_id() + "\nPayment information: " + event.getPayment_info() + "\nPurchase time: " + event.getPurchase_time() + "\nSubtotal: " + event.getPrice();
        }
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text,
                    ButtonType.OK);
            alert.showAndWait();
        });
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getAppStage() {
        return appStage;
    }

    public static void main(String[] args) {
        launch();
    }

    public static int getY() {
        return Y;
    }

    public static void setY(int y) {
        Y = y;
    }

    public static boolean isPurpleOutline() {
        return purpleOutline;
    }

    public static void setPurpleOutline(boolean purpleOutline) {
        App.purpleOutline = purpleOutline;
    }
}
