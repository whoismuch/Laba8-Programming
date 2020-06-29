package client.controllers;

import client.models.UniversalLocalizationModel;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.TextArea;
import javafx.stage.WindowEvent;

import java.util.ResourceBundle;

public class CommandResultController {

    private MainWindowCollectionController mainWindowCollectionController;
    private UniversalLocalizationModel universalLocalizationModel;
    private ResourceBundle bundle;

    public void setMainWindowCollectionController (MainWindowCollectionController mainWindowCollectionController) {
        this.mainWindowCollectionController = mainWindowCollectionController;
    }

    @FXML
    private TextArea text;

    private String normalResult;

    public void setResult(String result) {
        setNormalResult(result);
        text.setText(universalLocalizationModel.translateMeAText(bundle));
    }

    public void setNormalResult(String result) {
        this.normalResult = result;
        universalLocalizationModel.setNormalResult(result);
    }

    public TextArea getText ( ) {
        return text;
    }

    public void setEverything(MainWindowCollectionController mainWindowCollectionController, UniversalLocalizationModel universalLocalizationModel, ResourceBundle bundle) {
        this.mainWindowCollectionController = mainWindowCollectionController;
        this.universalLocalizationModel = universalLocalizationModel;
        this.bundle = bundle;
        Platform.runLater(() -> translate(bundle));

    }

    public void translate(ResourceBundle bundle) {
        universalLocalizationModel.changeLanguage(text.getParent(), bundle);
    }
}
