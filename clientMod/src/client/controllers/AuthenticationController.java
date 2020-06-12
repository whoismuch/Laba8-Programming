package client.controllers;

import client.ClientApp;
import client.models.AuthenticationModel;
import client.models.ClientProviding;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class AuthenticationController {

    private ClientProviding clientProviding;
    private ClientApp clientApp;
    private AuthenticationModel authenticationModel;

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
    public void onActionSignIn (ActionEvent actionEvent) throws IOException, InterruptedException {
        String result = authenticationModel.authorization(username.getText( ), password.getText( ));
        signIn.cancelButtonProperty( );
        authenticationResult.setText(result);

        nextStep(result);
    }

    @FXML
    public void onActionSignUp (ActionEvent actionEvent) throws IOException, InterruptedException {
        authenticationResult.setWrapText(true);
        String result = authenticationModel.registration(username.getText(), password.getText());
        signUp.cancelButtonProperty();
        authenticationResult.setText(result);

        nextStep(result);
    }

    public void nextStep (String result) throws IOException, InterruptedException {
        new Thread(( ) -> {
            if (result.equals("Вы успешно зарегистрировались") || result.equals("Вы успешно авторизовались")) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace( );
                }
                Platform.runLater(( ) -> {
                    Stage stage = (Stage) authenticationResult.getScene( ).getWindow( );
                    stage.close( );
                    try {
                        clientApp.showMainWindow( );
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                });
            }
        }).start( );
    }


    public void setEverything (ClientProviding clientProviding, ClientApp clientApp) {
        this.clientProviding = clientProviding;
        this.clientApp = clientApp;
        authenticationModel = new AuthenticationModel(clientProviding);
    }

}
