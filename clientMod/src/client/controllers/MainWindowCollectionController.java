package client.controllers;

import client.ClientApp;
import client.FullRoute;
import client.models.ClientProviding;
import client.models.MainWindowCollectionModel;
import com.sun.tools.corba.se.idl.toJavaPortable.Helper;
import common.generatedClasses.Coordinates;
import common.generatedClasses.Location;
import common.generatedClasses.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;

public class MainWindowCollectionController {

    private ClientProviding clientProviding;
    private ClientApp clientApp;
    private MainWindowCollectionModel mainWindowCollectionModel;

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
    private Button help;

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


    @FXML
    private void initialize ( ) {
        sum_of_distance.setWrapText(true);
        filter_less_than_distance.setWrapText(true);
        print_ascending.setWrapText(true);
        remove_greater.setWrapText(true);
        remove_lower.setWrapText(true);
        blup.setWrapText(true);

        // устанавливаем тип и значение которое должно хранится в колонке
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
    public void onActionInfo (ActionEvent actionEvent) {

    }

    public void setEverything (ClientProviding clientProviding, ClientApp clientApp) throws IOException {
        this.clientProviding = clientProviding;
        this.clientApp = clientApp;
        mainWindowCollectionModel = new MainWindowCollectionModel(clientProviding);

        setColumns( );
    }

    public void setColumns ( ) throws IOException {
        LinkedHashSet<Route> routes = mainWindowCollectionModel.giveMeMyCollection( );
        ObservableList<FullRoute> list = FXCollections.observableArrayList( );
        for (Route route : routes) {
            list.add(new FullRoute(route));
        }
        table.setItems(list);
    }

}
