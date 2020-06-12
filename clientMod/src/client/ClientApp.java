package client;

import client.controllers.AuthenticationController;
import client.controllers.ConnectionController;
import client.controllers.MainWindowCollectionController;
import client.models.ClientProviding;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class ClientApp extends Application {

    private Stage primaryStage;
    private static ClientProviding clientProviding;

    /**
     * @param args массив по умолчанию в основном методе. Не используется здесь.
     */
    public static void main (String[] args) {
        clientProviding = new ClientProviding();
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {


        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RouteApp");

        InputStream stream = getClass( ).getResourceAsStream("fxmls/Connection.fxml");
        FXMLLoader loader = new FXMLLoader( );
        VBox vBox = loader.load(stream);
        // Отображаем сцену, содержащую корневой макет.

        ConnectionController connectionController = loader.getController();
        ConnectionController cc = connectionController;
        cc.setEverything(clientProviding, this);
        loader.setController(cc);

        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show( );

        primaryStage.show( );
    }

    public void showAthorization() throws IOException {

        InputStream stream = getClass( ).getResourceAsStream("fxmls/Authentication.fxml");
        FXMLLoader loader = new FXMLLoader( );
        VBox vBox = loader.load(stream);
        // Отображаем сцену, содержащую корневой макет.

        AuthenticationController authenticationController = loader.getController();
        AuthenticationController ac = authenticationController;
        ac.setEverything(clientProviding, this);
        loader.setController(ac);

        Stage stage = new Stage();
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show( );
    }

    public void showMainWindow() throws IOException {
        InputStream stream = getClass( ).getResourceAsStream("fxmls/MainWindowCollection.fxml");
        FXMLLoader loader = new FXMLLoader( );
        TabPane tabPane = loader.load(stream);
        // Отображаем сцену, содержащую корневой макет.

        MainWindowCollectionController mainWindowCollectionController = loader.getController();
        MainWindowCollectionController mwcc = mainWindowCollectionController;
        mwcc.setEverything(clientProviding, this);
        loader.setController(mwcc);

        Stage stage = new Stage();
        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.show( );
    }
}

