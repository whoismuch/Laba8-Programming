package client.controllers;

import client.ClientApp;
import languages.*;
import client.models.ClientProviding;
import client.models.ConnectionModel;
import client.models.UniversalLocalizationModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConnectionController {


    private ClientProviding clientProviding;
    private ClientApp clientApp;
    private ConnectionModel connectionModel;
    private String address;
    private String port;
    private ResourceBundle bundle;
    private UniversalLocalizationModel universalLocalizationModel;
    private String result;

    @FXML
    private TextField tf1;

    @FXML
    private TextField tf2;

    @FXML
    private Button buttonConnect;

    @FXML
    private Label connectionResult;

    @FXML
    private Label connect;

    @FXML
    private Label enterAddress;

    @FXML
    private Label enterPort;
    private Locale locale;

    @FXML
    public void onActionConnection (ActionEvent event) throws InterruptedException {
        try {
            address = tf1.getText( );
            port = tf2.getText( );
            result = connectionModel.connect(address, port);
            connectionResult.setText(bundle.getString(result));

            nextStep(result);
        } catch (IOException ex) {
            connectionResult.setText(bundle.getString(LanguageRU.serverIsNotAv));
        }
    }

    @FXML
    public void onActionRussian (ActionEvent actionEvent) throws UnsupportedEncodingException {
        locale = new Locale("ru", "RU");
        bundle = ResourceBundle.getBundle("languages.LanguageRU");
        universalLocalizationModel.changeLanguage(connectionResult.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(connectionResult, result, bundle);
    }

    @FXML
    public void onActionEstlane (ActionEvent actionEvent) {
        locale = new Locale("et", "EE");
        bundle = ResourceBundle.getBundle("languages.LanguageET", locale);
        universalLocalizationModel.changeLanguage(connectionResult.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(connectionResult, result, bundle);

    }

    @FXML
    public void onActionCatala (ActionEvent actionEvent) {
        locale = new Locale("ca", "ES");
        bundle = ResourceBundle.getBundle("languages.LanguageCA", locale);
        universalLocalizationModel.changeLanguage(connectionResult.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(connectionResult, result, bundle);
    }

    @FXML
    public void onActionEnglish (ActionEvent actionEvent) {
        locale = new Locale("en", "ZA");
        bundle = ResourceBundle.getBundle("languages.LanguageEN", locale);
        universalLocalizationModel.changeLanguage(connectionResult.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(connectionResult, result, bundle);
    }

    public void nextStep (String result) throws IOException, InterruptedException {
        new Thread(( ) -> {
            if (bundle.getString(result).equals(bundle.getString("Соединение установлено"))) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace( );
                }
                Platform.runLater(( ) -> {
                    Stage stage = (Stage) buttonConnect.getScene( ).getWindow( );
                    stage.close( );
                    try {
                        clientApp.showAthorization(address, port, bundle, locale);
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                });
            }
        }).start( );

    }


    public void setEverything (ClientProviding clientProviding, ClientApp clientApp, UniversalLocalizationModel universalLocalizationModel, ResourceBundle bundle) {

        this.clientProviding = clientProviding;
        this.clientApp = clientApp;
        this.universalLocalizationModel = universalLocalizationModel;
        connectionModel = new ConnectionModel(clientProviding);
        this.bundle = bundle;
        universalLocalizationModel.changeLanguage(connectionResult.getParent( ).getParent( ), bundle);

        locale = new Locale("ru", "RU");
    }


}
