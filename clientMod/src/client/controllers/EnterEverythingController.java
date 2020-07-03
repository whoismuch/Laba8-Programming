package client.controllers;

import client.models.ClientProviding;
import client.models.UniversalLocalizationModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.ResourceBundle;


public class EnterEverythingController {


    @FXML
    private TextField tfEdit;

    @FXML
    private String text;

    private ClientProviding clientProviding;
    private MainWindowCollectionController mainWindowCollectionController;
    private UniversalLocalizationModel universalLocalizationModel;


    public TextField getTfEdit ( ) {
        return tfEdit;
    }

    public String getText ( ) {
        return text;
    }

    public void onKeyPressed (KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            text = tfEdit.getText();
            mainWindowCollectionController.doEdit();
        }
    }

    public void setEverything (ClientProviding clientProviding, MainWindowCollectionController mainWindowCollectionController, UniversalLocalizationModel universalLocalizationModel, ResourceBundle bundle) {
        this.clientProviding = clientProviding;
        this.mainWindowCollectionController = mainWindowCollectionController;
        this.universalLocalizationModel = universalLocalizationModel;
    }
}
