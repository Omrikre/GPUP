package components.app.code;

import components.app.AppController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

import static components.app.CommonResourcesPaths.APP_ICON_RESOURCE;
import static components.app.CommonResourcesPaths.MAIN_APP_RESOURCE;


public class Main extends Application {

    private AppController mainAppController;

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // load all component and controller from fxml
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(MAIN_APP_RESOURCE);
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        mainAppController = fxmlLoader.getController();
        Scene scene = new Scene(root, 1100, 820);
        primaryStage.setScene(scene);
        primaryStage.setTitle("G.P.U.P - Admin App");
        primaryStage.getIcons().add(new Image(APP_ICON_RESOURCE));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        mainAppController.close();
        Platform.exit();
    }

    //TODO - close

}
