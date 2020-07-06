package client.controllers;

import client.ClientApp;
import languages.*;
import client.models.AuthenticationModel;
import client.models.ClientProviding;
import client.models.UniversalLocalizationModel;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthenticationController {

    private ClientProviding clientProviding;
    private ClientApp clientApp;
    private AuthenticationModel authenticationModel;
    private UniversalLocalizationModel universalLocalizationModel;
    private ResourceBundle bundle;
    private String result;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button signUp;

    @FXML
    private Button signIn;

    @FXML
    private Label authenticationResult;
    private Locale locale;


    @FXML
    public void onActionSignIn (ActionEvent actionEvent) throws InterruptedException {
        try {
            result = authenticationModel.authorization(username.getText( ), password.getText( ));
            authenticationResult.setText(bundle.getString(result));

            nextStep(result);
        } catch (IOException ex) {
            clientProviding.setResetConnection(true);
            System.out.println("contr" + clientProviding.isResetConnection());
            authenticationResult.setText(bundle.getString(LanguageRU.serverIsNotAv));
        }

    }

    @FXML
    public void onActionSignUp (ActionEvent actionEvent)  throws InterruptedException {
        try {
            authenticationResult.setWrapText(true);
            result = authenticationModel.registration(username.getText( ), password.getText( ));
            authenticationResult.setText(bundle.getString(result));


            nextStep(result);
        } catch (IOException ex ) {
            clientProviding.setResetConnection(true);
            authenticationResult.setText(bundle.getString(LanguageRU.serverIsNotAv));
        }
    }

    @FXML
    public void onActionRussian (ActionEvent actionEvent) throws UnsupportedEncodingException {
        locale = new Locale("ru", "RU");
        bundle = ResourceBundle.getBundle("languages.LanguageRU");
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(authenticationResult, result, bundle);
    }

    @FXML
    public void onActionEstlane (ActionEvent actionEvent) {
        locale = new Locale("et", "EE");
        bundle = ResourceBundle.getBundle("languages.LanguageET", locale);
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(authenticationResult, result, bundle);

    }

    @FXML
    public void onActionCatala (ActionEvent actionEvent) {
        locale = new Locale("ca", "ES");
        bundle = ResourceBundle.getBundle("languages.LanguageCA", locale);
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(authenticationResult, result, bundle);
    }

    @FXML
    public void onActionEnglish (ActionEvent actionEvent) {
        locale = new Locale("en", "ZA");
        bundle = ResourceBundle.getBundle("languages.LanguageEN", locale);
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(authenticationResult, result, bundle);
    }

    public void nextStep (String result) {
        new Thread(( ) -> {
            if (bundle.getString(result).equals(bundle.getString("Вы успешно зарегистрировались")) || bundle.getString(result).equals(bundle.getString("Вы успешно авторизовались"))) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace( );
                }
                Platform.runLater(( ) -> {
                    Stage stage = (Stage) authenticationResult.getScene( ).getWindow( );
                    stage.close( );
                    try {
                        clientApp.showMainWindow(bundle, locale);
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                });
            }
        }).start( );
    }


    public void setEverything (ClientProviding clientProviding, ClientApp clientApp, UniversalLocalizationModel universalLocalizationModel, ResourceBundle bundle, Locale locale) {
        this.clientProviding = clientProviding;
        this.clientApp = clientApp;
        this.universalLocalizationModel = universalLocalizationModel;
        authenticationModel = new AuthenticationModel(clientProviding);
        this.bundle = bundle;
        universalLocalizationModel.changeLanguage(authenticationResult.getParent( ).getParent( ), bundle);
        this.locale = locale;

    }

}
