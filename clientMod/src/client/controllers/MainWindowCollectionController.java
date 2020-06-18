package client.controllers;

import client.ClientApp;
import client.ClientNotifying;
import client.FullRoute;
import client.models.ClientProviding;
import client.models.MainWindowCollectionModel;
import common.generatedClasses.Route;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;

public class MainWindowCollectionController {

    private ClientProviding clientProviding;
    private ClientApp clientApp;
    private MainWindowCollectionModel mainWindowCollectionModel;
    private CommandResultController commandResultController;
    private EnterRouteController enterRouteController;

    @FXML
    private TableView table;

    @FXML
    private TableColumn<FullRoute, Long> id;

    @FXML
    private TableColumn<FullRoute, String> owner;

    @FXML
    private TableColumn<FullRoute, String> name;

    @FXML
    private TableColumn<FullRoute, Long> coordinateX;

    @FXML
    private TableColumn<FullRoute, Integer> coordinateY;

    @FXML
    private TableColumn<FullRoute, ZonedDateTime> creationDate;

    @FXML
    private TableColumn<FullRoute, String> fromName;

    @FXML
    private TableColumn<FullRoute, Long> fromX;

    @FXML
    private TableColumn<FullRoute, Long> fromY;

    @FXML
    private TableColumn<FullRoute, String> toName;

    @FXML
    private TableColumn<FullRoute, Long> toX;

    @FXML
    private TableColumn<FullRoute, Long> toY;

    @FXML
    private TableColumn<FullRoute, Float> distance;

    @FXML
    private Button info;

    @FXML
    private Button sum_of_distance;

    @FXML
    private Button history;

    @FXML
    private Button add;

    @FXML
    private Button execute_script;

    @FXML
    private Button clear;

    @FXML
    private Button filter_less_than_distance;

    @FXML
    private Button print_ascending;

    @FXML
    private Button remove_greater;

    @FXML
    private Button remove_lower;

    @FXML
    private Button blup;

    private boolean alreadyOpened;

    @FXML
    private void initialize ( ) throws IOException {
        sum_of_distance.setWrapText(true);
        filter_less_than_distance.setWrapText(true);
        print_ascending.setWrapText(true);
        remove_greater.setWrapText(true);
        remove_lower.setWrapText(true);
        blup.setWrapText(true);

        id.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("id"));
        owner.setCellValueFactory(new PropertyValueFactory<FullRoute, String>("username"));
        name.setCellValueFactory(new PropertyValueFactory<FullRoute, String>("name"));
        coordinateX.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("coordinateX"));
        coordinateY.setCellValueFactory(new PropertyValueFactory<FullRoute, Integer>("coordinateY"));
        creationDate.setCellValueFactory(new PropertyValueFactory<FullRoute, ZonedDateTime>("creationDate"));
        fromName.setCellValueFactory(new PropertyValueFactory<FullRoute, String>("fromName"));
        fromX.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("fromX"));
        fromY.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("fromY"));
        toName.setCellValueFactory(new PropertyValueFactory<FullRoute, String>("toName"));
        toX.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("toX"));
        toY.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("toY"));
        distance.setCellValueFactory(new PropertyValueFactory<FullRoute, Float>("distance"));

    }

    @FXML
    public void onActionInfo (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField().getScene( ).getWindow( );
            stage.close( );
            alreadyOpened = false;
        }

        info.cancelButtonProperty( );
        String result = mainWindowCollectionModel.infoCommand( );

        if (!alreadyOpened) nextStep(result);
        else commandResultController.setResult(result);
    }


    @FXML
    public void onActionSumOfDistance (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField().getScene( ).getWindow( );
            stage.close( );
            alreadyOpened = false;
        }
        String result = mainWindowCollectionModel.sumOfDistanceCommand( );

        if (!alreadyOpened) nextStep(result);
        else commandResultController.setResult(result);
    }

    @FXML
    public void onActionHistory (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField().getScene( ).getWindow( );
            stage.close( );
            alreadyOpened = false;
        }
        String result = mainWindowCollectionModel.historyCommand( );

        if (!alreadyOpened) nextStep(result);
        else commandResultController.setResult(result);
    }

    @FXML
    public void onActionClear (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField().getScene( ).getWindow( );
            stage.close( );
        }
        String result = mainWindowCollectionModel.clearCommand( );

//        if (!alreadyOpened) nextStep(result);
//        else commandResultController.setResult(result);
    }

    @FXML
    public void onActionPrintAscending (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField().getScene( ).getWindow( );
            stage.close( );
            alreadyOpened = false;
        }
    }

    @FXML
    public void onActionAdd (ActionEvent actionEvent) throws IOException, InterruptedException {
        if (commandResultController != null) {
            Stage stage = (Stage) commandResultController.getText( ).getScene( ).getWindow( );
            stage.close( );
            alreadyOpened = false;
        }


        if (!alreadyOpened) openEnterRoute( );
        else {
            Stage stage1 = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
            stage1.close( );

            openEnterRoute( );
        }
    }

    public void doAdd ( ) throws IOException {
        mainWindowCollectionModel.addCommand(clientProviding.getUserManager( ).getRoute( ));
    }

    public void openEnterRoute ( ) {
        Thread thread = new Thread(new Runnable( ) {
            @Override
            public void run ( ) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace( );
                }

                Platform.runLater(( ) -> {
                    FXMLLoader loader = null;
                    try {
                        loader = clientApp.showEnterRoute( );
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                    enterRouteController = loader.getController( );
                    alreadyOpened = true;
                    enterRouteController.getNameField( ).getScene( ).getWindow( ).setOnCloseRequest(new EventHandler<WindowEvent>( ) {
                        public void handle (WindowEvent we) {
                            alreadyOpened = false;
                        }
                    });


                });
            }
        });

        thread.start( );
    }

    @FXML
    public void onActionRemoveLower (ActionEvent actionEvent) {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField().getScene( ).getWindow( );
            stage.close( );
            alreadyOpened = false;
        }

    }

    @FXML
    public void onActionRemoveGreater (ActionEvent actionEvent) {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField().getScene( ).getWindow( );
            stage.close( );
            alreadyOpened = false;
        }

    }


    public void nextStep (String result) {
        new Thread(( ) -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace( );
            }
            Platform.runLater(( ) -> {
                FXMLLoader loader = null;
                try {
                    loader = clientApp.showCommandResult( );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                commandResultController = loader.getController( );
                alreadyOpened = true;
                commandResultController.setResult(result);
                commandResultController.getText( ).getScene( ).getWindow( ).setOnCloseRequest(new EventHandler<WindowEvent>( ) {
                    public void handle (WindowEvent we) {
                        alreadyOpened = false;
                    }
                });
            });
        }).start( );

    }


    public void setEverything (ClientProviding clientProviding, ClientApp clientApp, String address, String port) throws IOException {
        this.clientProviding = clientProviding;
        this.clientApp = clientApp;
        mainWindowCollectionModel = new MainWindowCollectionModel(clientProviding);
        alreadyOpened = false;
        clientProviding.getClientNotifying().setMainWindowCollectionController(this);

        setColumns(clientProviding.getRoutes());
        clientProviding.clientWork();
    }

    public void setColumns (LinkedHashSet<Route> routes) throws IOException {
        try {
            ObservableList<FullRoute> list = FXCollections.observableArrayList( );
            for (Route route : routes) {
                list.add(new FullRoute(route));
            }
            table.setItems(list);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

    }
}
