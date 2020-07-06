package client.controllers;

import client.FullRoute;
import client.models.ClientProviding;
import client.models.UniversalLocalizationModel;
import common.exceptions.NoPermissionsException;
import common.generatedClasses.Route;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class EnterScriptController {
    private MainWindowCollectionController mainWindowCollectionController;
    private ClientProviding clientProviding;
    private UniversalLocalizationModel universalLocalizationModel;

    @FXML
    private TextField nameScript;

    @FXML
    private Button doneED;

    @FXML
    private Label resultES;

    @FXML
    private TextArea commandResultES;

    @FXML
    private Button chooseFile;

    private ResourceBundle bundle;

    private String result;

    private String scriptResult;

    @FXML
    private void initialize ( ) throws IOException {
        chooseFile.setOnAction( e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt"));

            File file = fc.showOpenDialog((Stage) chooseFile.getScene().getWindow());
            nameScript.setText(file.getAbsolutePath());
        });

    }
    public Button getDone ( ) {
        return doneED;
    }

    public void onActionDone (ActionEvent actionEvent) throws IOException {
        try {
            String arg = clientProviding.getUserManager( ).contentOfFile(nameScript.getText( ));
            clientProviding.getUserManager( ).setFinalScript(arg);
            if (arg.equals("")) {
                result = "Ммм, скрипт пустой";
                resultES.setText(bundle.getString(result));
            } else {
                int commandNumber = clientProviding.getUserManager( ).checkContentOfFile(arg, 0);
                if (commandNumber == 0) {
                    result = "Бе, скрипт с ошибочками, такой скрипт мы обработать не сможем";
                    resultES.setText(bundle.getString(result));
                } else {
                    arg = clientProviding.getUserManager( ).getFinalScript( );
                    result = "Все супер";
                    resultES.setText(bundle.getString(result));
                    scriptResult = mainWindowCollectionController.doExecScript(arg);
                    commandResultES.setText(universalLocalizationModel.translateMeAText(bundle, scriptResult));
                }
            }

        } catch (NoPermissionsException e) {
            result = "Недостаточно прав для чтения скрипта";
            resultES.setText(bundle.getString(result));
        } catch (FileNotFoundException e) {
            result = "Файла со скриптом по указанному пути не существует";
            resultES.setText(bundle.getString(result));
        } catch (NullPointerException e) {
            result = "Файл пуст!";
            resultES.setText(bundle.getString(result));
        }
    }

    public void setEverything (ClientProviding clientProviding, MainWindowCollectionController mainWindowCollectionController, UniversalLocalizationModel universalLocalizationModel, ResourceBundle bundle) {
        this.clientProviding = clientProviding;
        this.mainWindowCollectionController = mainWindowCollectionController;
        this.universalLocalizationModel = universalLocalizationModel;
        this.bundle = bundle;
        Platform.runLater(( ) -> translate(bundle));

    }

    public void translate (ResourceBundle bundle) {
        this.bundle = bundle;
        universalLocalizationModel.changeLanguage(doneED.getParent( ), bundle);
        universalLocalizationModel.updateLabels(resultES, result, bundle);
        if (scriptResult != null) {
            commandResultES.setText(universalLocalizationModel.translateMeAText(bundle, scriptResult));
        }
    }
}
