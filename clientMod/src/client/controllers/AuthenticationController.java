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


    @FXML
    public void onActionSignIn (ActionEvent actionEvent) throws InterruptedException {
        try {
            result = authenticationModel.authorization(username.getText( ), password.getText( ));
            authenticationResult.setText(bundle.getString(result));

            nextStep(result);
        } catch (IOException ex) {
            System.out.println(1);
            authenticationResult.setText(bundle.getString(LanguageRU.serverIsNotAv));
        }

    }

    @FXML
    public void onActionSignUp (ActionEvent actionEvent) throws IOException, InterruptedException {
        authenticationResult.setWrapText(true);
        result = authenticationModel.registration(username.getText( ), password.getText( ));
        authenticationResult.setText(bundle.getString(result));


        nextStep(result);
    }

    @FXML
    public void onActionRussian (ActionEvent actionEvent) throws UnsupportedEncodingException {
        bundle = ResourceBundle.getBundle("languages.LanguageRU");
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(authenticationResult, result, bundle);
    }

    @FXML
    public void onActionEstlane (ActionEvent actionEvent) {
        bundle = ResourceBundle.getBundle("languages.LanguageEST", new Locale("est", "EST"));
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(authenticationResult, result, bundle);

    }

    @FXML
    public void onActionCatala (ActionEvent actionEvent) {
        bundle = ResourceBundle.getBundle("languages.LanguageCAT", new Locale("cat", "CAT"));
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(authenticationResult, result, bundle);
    }

    @FXML
    public void onActionEnglish (ActionEvent actionEvent) {
        bundle = ResourceBundle.getBundle("languages.LanguageEN", new Locale("en", "ZA"));
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(authenticationResult, result, bundle);
    }

    public void nextStep (String result) throws IOException, InterruptedException {
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
                        clientApp.showMainWindow(bundle);
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
        authenticationModel = new AuthenticationModel(clientProviding);
        this.bundle = bundle;
        universalLocalizationModel.changeLanguage(authenticationResult.getParent( ).getParent( ), bundle);

    }

}
