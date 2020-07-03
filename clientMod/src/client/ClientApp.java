package client;

import client.controllers.*;
import client.models.ClientProviding;
import client.models.UniversalLocalizationModel;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClientApp extends Application {

    private Stage primaryStage;
    private static ClientProviding clientProviding;
    private MainWindowCollectionController mainWindowCollectionController;
    private String address;
    private String port;
    private static ResourceBundle bundle;
    private static UniversalLocalizationModel universalLocalizationModel;

    /**
     * @param args массив по умолчанию в основном методе. Не используется здесь.
     */
    public static void main (String[] args) {
        clientProviding = new ClientProviding( );
        universalLocalizationModel = new UniversalLocalizationModel( );
        bundle = ResourceBundle.getBundle("languages.LanguageRU");
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {


        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RouteApp");

        InputStream stream = getClass( ).getResourceAsStream("fxmls/Connection.fxml");
        FXMLLoader loader = new FXMLLoader( );
        BorderPane borderPane = loader.load(stream);
        // Отображаем сцену, содержащую корневой макет.

        ConnectionController connectionController = loader.getController( );
        ConnectionController cc = connectionController;
        cc.setEverything(clientProviding, this, universalLocalizationModel, bundle);
        loader.setController(cc);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show( );

        primaryStage.show( );
    }

    public void showAthorization (String address, String port, ResourceBundle bundle, Locale locale) throws IOException {

        this.address = address;
        this.port = port;
        InputStream stream = getClass( ).getResourceAsStream("fxmls/Authentication.fxml");
        FXMLLoader loader = new FXMLLoader( );
        BorderPane borderPane = loader.load(stream);
        // Отображаем сцену, содержащую корневой макет.

        AuthenticationController authenticationController = loader.getController( );
        AuthenticationController ac = authenticationController;
        ac.setEverything(clientProviding, this, universalLocalizationModel, bundle, locale);
        loader.setController(ac);

        Stage stage = new Stage( );
        stage.setTitle("RouteApp");
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show( );
    }

    public void showMainWindow (ResourceBundle bundle, Locale locale) throws IOException {
        InputStream stream = getClass( ).getResourceAsStream("fxmls/MainWindowCollection.fxml");
        FXMLLoader loader = new FXMLLoader( );
        TabPane tabPane = loader.load(stream);
        // Отображаем сцену, содержащую корневой макет.


        MainWindowCollectionController mainWindowCollectionController = loader.getController( );
        MainWindowCollectionController mwcc = mainWindowCollectionController;

        mwcc.setEverything(clientProviding, this, tabPane, universalLocalizationModel, bundle, locale);
        loader.setController(mwcc);
        this.mainWindowCollectionController = mwcc;
        clientProviding.setMainController(this.mainWindowCollectionController);

        Stage stage = new Stage( );
        stage.setTitle("RouteApp");
        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.show( );
    }

    public FXMLLoader showCommandResult (ResourceBundle bundle) throws IOException {
        InputStream stream = getClass( ).getResourceAsStream("fxmls/CommandResult.fxml");
        FXMLLoader loader = new FXMLLoader( );
        BorderPane borderPane = loader.load(stream);

        CommandResultController commandResultController = loader.getController( );
        commandResultController.setEverything(mainWindowCollectionController, universalLocalizationModel, bundle);

        Stage stage = new Stage( );
        stage.setTitle("CommandResult");
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show( );

        return loader;
    }

    public FXMLLoader showEnterRoute (ResourceBundle bundle) throws IOException {
        InputStream stream = getClass( ).getResourceAsStream("fxmls/EnterRoute.fxml");
        FXMLLoader loader = new FXMLLoader( );
        VBox vBox = loader.load(stream);

        EnterRouteController enterRouteController = loader.getController( );
        EnterRouteController erc = enterRouteController;


        erc.setEverything(clientProviding, this, universalLocalizationModel, mainWindowCollectionController, bundle);
        loader.setController(erc);

        Stage stage = new Stage( );
        stage.setTitle("EnterRoute");
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show( );

        return loader;

    }

    public FXMLLoader showEnterDistance (ResourceBundle bundle ) throws IOException {
        InputStream stream = getClass( ).getResourceAsStream("fxmls/EnterDistance.fxml");
        FXMLLoader loader = new FXMLLoader( );
        BorderPane borderPane = loader.load(stream);

        EnterDistanceController edc = loader.getController( );
        edc.setEverything(mainWindowCollectionController, universalLocalizationModel, bundle);

        Stage stage = new Stage( );
        stage.setTitle("EnterDistance");
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show( );

        return loader;
    }

    public FXMLLoader showEnterScript (ResourceBundle bundle) throws IOException {
        InputStream stream = getClass( ).getResourceAsStream("fxmls/EnterScript.fxml");
        FXMLLoader loader = new FXMLLoader( );
        BorderPane borderPane = loader.load(stream);

        EnterScriptController esc = loader.getController( );
        esc.setEverything(clientProviding, mainWindowCollectionController, universalLocalizationModel, bundle);


        Stage stage = new Stage( );
        stage.setTitle("EnterScript");
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show( );

        return loader;

    }

    public FXMLLoader showEnterEverything () throws IOException {
        InputStream stream = getClass( ).getResourceAsStream("fxmls/EnterEverything.fxml");
        FXMLLoader loader = new FXMLLoader( );
        TextField textField = loader.load(stream);

        EnterEverythingController eec = loader.getController();
        eec.setEverything(clientProviding, mainWindowCollectionController, universalLocalizationModel, bundle);


        Stage stage = new Stage( );
        Scene scene = new Scene(textField);
        stage.setScene(scene);
        stage.show( );

        return loader;
    }
}

