package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.SystemUser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage App_stage;
    private SimpleClient client;
    private SystemUser sysUser;
    private static int Y = 0;
    private static boolean purpleOutline = false;

    @Override
    public void start(Stage stage) throws IOException {
        App_stage = stage;

        EventBus.getDefault().register(this);
        client = SimpleClient.getClient();
        client.openConnection();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            controller.shutdown();
            Platform.exit();
        });
        stage.show();
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
            text = "Customer name: " + event.getCustomer_name() + "\nPayment information: " + event.getPayment_info() + "\nPurchase time: " + event.getPurchase_time() + "\nSubtotal: " + event.getPrice() + "\nMovie details: " + event.getMovieDetail();
        } else {
            text = "Customer name: " + event.getCustomer_name() + "\nPayment information: " + event.getPayment_info() + "\nPurchase time: " + event.getPurchase_time() + "\nSubtotal: " + event.getPrice();
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

    public static Stage getApp_stage() {
        return App_stage;
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