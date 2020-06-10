package client.controllers;

import client.ClientApp;
import client.models.ConnectionModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class ConnectionController {

    ConnectionModel connectionModel = new ConnectionModel( );
    ClientApp clientApp = new ClientApp();

    @FXML
    private TextField tf1;

    @FXML
    private TextField tf2;

    @FXML
    private Button buttonConnect;

    @FXML
    private Label connectionResult;

    @FXML
    public void onActionConnection (ActionEvent event) throws IOException, InterruptedException {
        String result = connectionModel.connect(tf1.getText( ), tf2.getText( ));
        buttonConnect.cancelButtonProperty( );
        connectionResult.setText(result);

        nextStep(result);
    }

    public void nextStep(String result) throws IOException, InterruptedException {
        new Thread(() -> {
            if (result.equals("Соединение установлено")) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace( );
                }
                Platform.runLater(() -> {
                    Stage stage = (Stage) buttonConnect.getScene( ).getWindow( );
                    stage.close();
                    try {
                        clientApp.showAthorization();
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                });
            }
        }).start();

    }

}
