package client;

import client.models.ClientProviding;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class ClientApp extends Application {

    private Stage primaryStage;

    /**
     * @param args массив по умолчанию в основном методе. Не используется здесь.
     */
    public static void main (String[] args) {
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

        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show( );

        primaryStage.show( );
    }

    public void showAthorization() throws IOException {

        InputStream stream = getClass( ).getResourceAsStream("fxmls/Authorization.fxml");
        FXMLLoader loader = new FXMLLoader( );
        VBox vBox = loader.load(stream);
        // Отображаем сцену, содержащую корневой макет.

        Stage stage = new Stage();
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show( );
    }
}

