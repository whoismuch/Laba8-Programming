package client.controllers;

import client.ClientApp;
import client.models.ClientProviding;
import client.models.ConnectionModel;
import client.models.EnterRouteModel;
import common.generatedClasses.Route;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class EnterRouteController {

    private ClientProviding clientProviding;
    private ClientApp clientApp;
    private EnterRouteModel enterRouteModel;
    private MainWindowCollectionController mainWindowCollectionController;


    @FXML
    public TextField coordinateXField;

    @FXML
    public TextField coordinateYField;

    @FXML
    public TextField fromNameField;

    @FXML
    public TextField fromXField;

    @FXML
    public TextField fromYField;

    @FXML
    public TextField toNameField;

    @FXML
    public TextField toXField;

    @FXML
    public TextField toYField;

    @FXML
    public TextField distanceField;

    @FXML
    public Label validationResult;

    @FXML
    public Button done;

    @FXML
    private TextField nameField;

    public TextField getNameField ( ) {
        return nameField;
    }


    public void onActionDone (ActionEvent actionEvent) throws IOException, InterruptedException {
        validationResult.setWrapText(true);
        String result = enterRouteModel.checkRoute (nameField.getText(), coordinateXField.getText(), coordinateYField.getText(), fromNameField.getText(), fromXField.getText(), fromYField.getText(), toNameField.getText(), toXField.getText(), toYField.getText(), distanceField.getText());

        validationResult.setText(result);

        if (result.equals("Весьма симпатичный маршрут. Так держать")) {
            if (mainWindowCollectionController.getCommand().equals("add")) mainWindowCollectionController.doAdd();
            if (mainWindowCollectionController.getCommand().equals("remove_lower")) mainWindowCollectionController.doRemoveLower();
            if (mainWindowCollectionController.getCommand().equals("remove_greater")) mainWindowCollectionController.doRemoveGreater();
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace( );
                }
                Platform.runLater(() -> {
                Stage stage = (Stage) done.getScene( ).getWindow( );
                stage.close( );
            });}).start();
        }
    }



    public void setEverything (ClientProviding clientProviding, ClientApp clientApp) {

        this.clientProviding = clientProviding;
        this.clientApp = clientApp;
        enterRouteModel = new EnterRouteModel(clientProviding);
    }

    public void setMainWindowCollectionController (MainWindowCollectionController mainWindowCollectionController) {
        this.mainWindowCollectionController = mainWindowCollectionController;
    }
}
