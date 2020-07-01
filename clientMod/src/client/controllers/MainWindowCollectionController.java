package client.controllers;

import client.ClientApp;
import client.FullRoute;
import client.models.ClientProviding;
import client.models.EnterRouteModel;
import client.models.MainWindowCollectionModel;
import client.models.UniversalLocalizationModel;
import com.sun.webkit.dom.KeyboardEventImpl;
import common.generatedClasses.Route;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.nio.cs.ext.MacThai;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.*;

public class MainWindowCollectionController {

    private ClientProviding clientProviding;
    private ClientApp clientApp;
    private MainWindowCollectionModel mainWindowCollectionModel;
    private CommandResultController commandResultController;
    private EnterRouteController enterRouteController;
    private EnterDistanceController enterDistanceController;
    private EnterScriptController enterScriptController;
    private UniversalLocalizationModel universalLocalizationModel;
    private ResourceBundle bundle;
    private EnterRouteModel enterRouteModel;

    @FXML
    private Label username;

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
    private TableColumn<FullRoute, Float> Ddistance;

    @FXML
    private Canvas canvas;

    @FXML
    private Label username1;

    @FXML
    private Label user;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField forFilter;

    @FXML
    private TextField idField;

    @FXML
    private TextField ownerField;

    @FXML
    private TextField creationDateField;

    @FXML
    private TextField distanceField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField nowXField;

    @FXML
    private TextField nowYField;

    @FXML
    private TextField fromNameField;

    @FXML
    private TextField toNameField;

    @FXML
    private TextField fromXField;

    @FXML
    private TextField fromYField;

    @FXML
    private TextField toXField;

    @FXML
    private TextField toYField;

    @FXML
    private Label editionResult;


    private String command;

    private GraphicsContext gc;

    private Long scale;

    private double gran;


    @FXML
    private void initialize ( ) throws IOException {

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
        Ddistance.setCellValueFactory(new PropertyValueFactory<FullRoute, Float>("distance"));


        forFilter.textProperty( ).addListener((observable, oldValue, newValue) -> { // filter_area - текствое поле , куда ты будешь вводить текст дл€ фильтрации
            List<Route> routeList = new ArrayList<>( );
            try {
                setColumnsByList(routeList);
            } catch (IOException e) {
                e.printStackTrace( );
            }
            ObservableList<Route> tableData = FXCollections.observableArrayList( );
            tableData.addAll(clientProviding.getRoutes( )); // загружаю все объекты , которые есть в коллекции
            FilteredList<Route> routes = new FilteredList<>(tableData, p -> true);
            routes.setPredicate(route -> {
                if (newValue == null || newValue.isEmpty( )) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase( );
                try {
                    if (route.getName( ).toLowerCase( ).contains(lowerCaseFilter)) { // проверка , есть ли в введеном поле часть имени моего класса
                        return true;
                    }

                    try {
                        if (route.getId( ).equals(Long.parseLong(lowerCaseFilter))) {
                            return true;
                        }
                    } catch (NumberFormatException ex) {
                    }

                    if (route.getCreationDate( ).toString( ).contains(lowerCaseFilter)) {
                        return true;
                    }

                    try {
                        if (route.getCoordinates( ).getX( ).equals(Long.parseLong(lowerCaseFilter))) {
                            return true;
                        }
                    } catch (NumberFormatException ex) {
                    }

                    try {
                        if (route.getCoordinates( ).getY( ) == Long.parseLong(lowerCaseFilter)) {
                            return true;
                        }
                    } catch (NumberFormatException ex) {
                    }


                    if (route.getUsername( ).toLowerCase( ).contains(lowerCaseFilter)) {
                        return true;
                    }

                    if (route.getFrom( ).getName( ).toLowerCase( ).contains(lowerCaseFilter)) {
                        return true;
                    }

                    try {
                        if (route.getFrom( ).getX( ) == Long.parseLong(lowerCaseFilter)) {
                            return true;
                        }
                    } catch (NumberFormatException ex) {
                    }

                    try {
                        if (route.getFrom( ).getY( ).equals(Long.parseLong(lowerCaseFilter))) {
                            return true;
                        }
                    } catch (NumberFormatException ex) {
                    }

                    if (route.getTo( ).getName( ).toLowerCase( ).contains(lowerCaseFilter)) {
                        return true;
                    }

                    try {
                        if (route.getTo( ).getX( ) == Long.parseLong(lowerCaseFilter)) {
                            return true;
                        }
                    } catch (NumberFormatException ex) {
                    }

                    try {
                        if (route.getTo( ).getY( ).equals(Long.parseLong(lowerCaseFilter))) {
                            return true;
                        }
                    } catch (NumberFormatException ex) {
                    }

                    try {
                        if (route.getDistance( ).equals(Float.parseFloat(lowerCaseFilter))) {
                            return true;
                        }
                    } catch (NumberFormatException ex) {
                    }


                } catch (NullPointerException ex) {
                    return false;
                }

                return false;
            });
            try {
                setColumnsByList(routes);
            } catch (IOException e) {
                e.printStackTrace( );
            }
        });

        setEdit(false);

    }

    @FXML
    public void onActionMouseClicked (MouseEvent mouseEvent) {
        setEdit(true);
        editionResult.setText("");
        double x = mouseEvent.getX( );
        double y = mouseEvent.getY( );
        for (Route route : clientProviding.getRoutes( )) {
            if (Math.abs(x - (canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale)) < 15 && (((canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - y) < 50 && ((canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - y) > 0) || Math.abs((x - (canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale))) < 15 && ((canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale) - y < 50 && canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale - y > 0)) {
                idField.setText(route.getId( ).toString( ));
                ownerField.setText(route.getUsername( ));
                creationDateField.setText(route.getCreationDate( ).toString( ));
                distanceField.setText(route.getDistance( ).toString( ));
                nameField.setText(route.getName( ));
                nowXField.setText(route.getCoordinates( ).getX( ).toString( ));
                nowYField.setText(((Integer) route.getCoordinates( ).getY( )).toString( ));
                fromNameField.setText(route.getFrom( ).getName( ));
                fromXField.setText(((Long) route.getFrom( ).getX( )).toString( ));
                fromYField.setText(route.getFrom( ).getY( ).toString( ));
                toNameField.setText(route.getTo( ).getName( ));
                toXField.setText(((Long) route.getTo( ).getX( )).toString( ));
                toYField.setText(route.getTo( ).getY( ).toString( ));
                idField.setEditable(false);
                ownerField.setEditable(false);
                creationDateField.setEditable(false);
            }
        }

    }

    @FXML
    public void onActionInfo (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterDistanceController != null) {
            Stage stage = (Stage) enterDistanceController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterScriptController != null) {
            Stage stage = (Stage) enterScriptController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        String result = mainWindowCollectionModel.infoCommand( );

        if (commandResultController != null) {
            if (!commandResultController.getText( ).getScene( ).getWindow( ).isShowing( )) nextStep(result);
            else commandResultController.setResult(result);
        } else nextStep(result);
    }


    @FXML
    public void onActionSumOfDistance (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterDistanceController != null) {
            Stage stage = (Stage) enterDistanceController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterScriptController != null) {
            Stage stage = (Stage) enterScriptController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        String result = mainWindowCollectionModel.sumOfDistanceCommand( );

        if (commandResultController != null) {
            if (!commandResultController.getText( ).getScene( ).getWindow( ).isShowing( )) nextStep(result);
            else commandResultController.setResult(result);
        } else nextStep(result);
    }

    @FXML
    public void onActionHistory (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterDistanceController != null) {
            Stage stage = (Stage) enterDistanceController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterScriptController != null) {
            Stage stage = (Stage) enterScriptController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        String result = mainWindowCollectionModel.historyCommand( );

        if (commandResultController != null) {
            if (!commandResultController.getText( ).getScene( ).getWindow( ).isShowing( )) nextStep(result);
            else commandResultController.setResult(result);
        } else nextStep(result);
    }

    @FXML
    public void onActionClear (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (commandResultController != null) {
            Stage stage = (Stage) commandResultController.getText( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterDistanceController != null) {
            Stage stage = (Stage) enterDistanceController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterScriptController != null) {
            Stage stage = (Stage) enterScriptController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        mainWindowCollectionModel.clearCommand( );


    }

    @FXML
    public void onActionPrintAscending (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (commandResultController != null) {
            Stage stage = (Stage) commandResultController.getText( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterDistanceController != null) {
            Stage stage = (Stage) enterDistanceController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterScriptController != null) {
            Stage stage = (Stage) enterScriptController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        List<Route> list = mainWindowCollectionModel.printAscendingCommand( );
        if (list != null) setColumnsByList(list);
    }

    public void onActionFilterLess (ActionEvent actionEvent) {

        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (commandResultController != null) {
            Stage stage = (Stage) commandResultController.getText( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterScriptController != null) {
            Stage stage = (Stage) enterScriptController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterDistanceController == null) {
            openEnterDistance( );
        } else {
            Stage stage = (Stage) enterDistanceController.getDone( ).getScene( ).getWindow( );
            stage.close( );
            openEnterDistance( );
        }
    }

    @FXML
    public void onActionAdd (ActionEvent actionEvent) throws IOException, InterruptedException {

        command = "add";

        if (commandResultController != null) {
            Stage stage = (Stage) commandResultController.getText( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterDistanceController != null) {
            Stage stage = (Stage) enterDistanceController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterScriptController != null) {
            Stage stage = (Stage) enterScriptController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }


        if (enterRouteController != null) {
            if (!enterRouteController.getNameField( ).getScene( ).getWindow( ).isShowing( )) openEnterRoute( );
            else {
                Stage stage = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
                stage.close( );
            }
        } else openEnterRoute( );


    }

    @FXML
    public void onActionExecuteScript (ActionEvent actionEvent) throws IOException {
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (commandResultController != null) {
            Stage stage = (Stage) commandResultController.getText( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterDistanceController != null) {
            Stage stage = (Stage) enterDistanceController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterScriptController == null) {
            openEnterScript( );
        } else {
            Stage stage = (Stage) enterScriptController.getDone( ).getScene( ).getWindow( );
            stage.close( );
            openEnterScript( );
        }
    }

    public void doFilterLess (String dist) throws IOException {
        List<Route> list = mainWindowCollectionModel.filterLessThanDistance(dist);
        if (list != null) setColumnsByList(list);
    }

    public void doAdd ( ) throws IOException {
        mainWindowCollectionModel.addCommand( );

    }

    public String doExecScript (String arg) throws IOException {
        return mainWindowCollectionModel.executeScriptCommand(arg);
    }

    public void openEnterScript ( ) {
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
                        loader = clientApp.showEnterScript(bundle);
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                    enterScriptController = loader.getController( );

                });
            }
        });

        thread.start( );
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
                        loader = clientApp.showEnterRoute(bundle);
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                    enterRouteController = loader.getController( );
                });
            }
        });

        thread.start( );
    }

    public void openEnterDistance ( ) {
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
                        loader = clientApp.showEnterDistance(bundle);
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                    enterDistanceController = loader.getController( );
                });
            }
        });
        thread.start( );
    }

    @FXML
    public void onActionRemoveLower (ActionEvent actionEvent) {

        command = "remove_lower";

        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterDistanceController != null) {
            Stage stage = (Stage) enterDistanceController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterScriptController != null) {
            Stage stage = (Stage) enterScriptController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        openEnterRoute( );

    }

    public void doRemoveLower ( ) throws IOException {
        mainWindowCollectionModel.removeLowerCommand( );
    }

    @FXML
    public void onActionRemoveGreater (ActionEvent actionEvent) {

        command = "remove_greater";
        if (enterRouteController != null) {
            Stage stage = (Stage) enterRouteController.getNameField( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterDistanceController != null) {
            Stage stage = (Stage) enterDistanceController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (enterScriptController != null) {
            Stage stage = (Stage) enterScriptController.getDone( ).getScene( ).getWindow( );
            stage.close( );
        }

        openEnterRoute( );

    }

    public void doRemoveGreater ( ) throws IOException {
        mainWindowCollectionModel.removeGreaterCommand( );
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
                    loader = clientApp.showCommandResult(bundle);
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                commandResultController = loader.getController( );
                commandResultController.setResult(result);
            });
        }).start( );

    }


    @FXML
    public void onActionRussian (ActionEvent actionEvent) throws UnsupportedEncodingException {
        bundle = ResourceBundle.getBundle("languages.LanguageRU");
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ).getParent( ).getParent( ), bundle);

        if (commandResultController != null) commandResultController.translate(bundle);
        if (enterRouteController != null) enterRouteController.translate(bundle);
        if (enterDistanceController != null) enterDistanceController.translate(bundle);
        if (enterScriptController != null) enterScriptController.translate(bundle);
    }

    @FXML
    public void onActionEstlane (ActionEvent actionEvent) {
        bundle = ResourceBundle.getBundle("languages.LanguageEST", new Locale("est", "EST"));
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ).getParent( ).getParent( ), bundle);
        if (commandResultController != null) commandResultController.translate(bundle);
        if (enterRouteController != null) enterRouteController.translate(bundle);
        if (enterDistanceController != null) enterDistanceController.translate(bundle);
        if (enterScriptController != null) enterScriptController.translate(bundle);


    }

    @FXML
    public void onActionCatala (ActionEvent actionEvent) {
        bundle = ResourceBundle.getBundle("languages.LanguageCAT", new Locale("cat", "CAT"));
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ).getParent( ).getParent( ), bundle);
        if (commandResultController != null) commandResultController.translate(bundle);
        if (enterRouteController != null) enterRouteController.translate(bundle);
        if (enterDistanceController != null) enterDistanceController.translate(bundle);
        if (enterScriptController != null) enterScriptController.translate(bundle);

    }

    @FXML
    public void onActionEnglish (ActionEvent actionEvent) {
        bundle = ResourceBundle.getBundle("languages.LanguageEN", new Locale("en", "ZA"));
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ).getParent( ).getParent( ), bundle);
        if (commandResultController != null) commandResultController.translate(bundle);
        if (enterRouteController != null) enterRouteController.translate(bundle);
        if (enterDistanceController != null) enterDistanceController.translate(bundle);
        if (enterScriptController != null) enterScriptController.translate(bundle);

    }

    @FXML
    public void onActionDelete (ActionEvent actionEvent) throws IOException {
        editionResult.setText("");
        if (toNameField.isEditable()) {
            String result = mainWindowCollectionModel.removeByIdCommand(idField.getText( ));
            editionResult.setText(result);
            if (result.equals("Элемент удален!")) {
                clearFields( );
                setEdit(false);
            }
        }
    }

    @FXML
    public void onActionEdit (ActionEvent actionEvent) throws IOException {
        editionResult.setText("");
        if (toNameField.isEditable()) {
            String result = enterRouteModel.checkRoute(nameField.getText( ), nowXField.getText( ), nowYField.getText( ), fromNameField.getText( ), fromXField.getText( ), fromYField.getText( ), toNameField.getText( ), toXField.getText( ), toYField.getText( ), distanceField.getText( ));
            String result2 = "";
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                result2 = mainWindowCollectionModel.updateIdCommand(idField.getText( ));
                editionResult.setText(result2);
            } else editionResult.setText(result);
        }
    }


    public void setEverything (ClientProviding clientProviding, ClientApp clientApp, TabPane tabPane, UniversalLocalizationModel universalLocalizationModel, ResourceBundle bundle) throws IOException {
        this.clientProviding = clientProviding;
        this.clientApp = clientApp;
        this.tabPane = tabPane;
        this.universalLocalizationModel = universalLocalizationModel;
        this.bundle = bundle;
        mainWindowCollectionModel = new MainWindowCollectionModel(clientProviding);
        enterRouteModel = new EnterRouteModel(clientProviding);

        clientProviding.getClientNotifying( ).setMainWindowCollectionController(this);

        setColumns(clientProviding.getRoutes( ));
        clientProviding.clientWork( );

        username.setText(clientProviding.getUsername( ));
        username1.setText(clientProviding.getUsername( ));

        Platform.runLater(( ) -> {
            universalLocalizationModel.changeLanguage(user.getParent( ).getParent( ).getParent( ).getParent( ), bundle);
        });

    }

    public void setColumns (LinkedHashSet<Route> routes) throws IOException {
        try {
            ObservableList<FullRoute> list = FXCollections.observableArrayList( );
            for (Route route : routes) {
                list.add(new FullRoute(route));
            }
            drawRoutes(routes);
            table.setItems(list);
            clearFields();
        } catch (IllegalStateException ex) {
            ex.printStackTrace( );
        }

    }

    public void drawRoutes (LinkedHashSet<Route> routes) {
        HashMap<String, Color> colors = getColors(routes);
        if (gc != null) gc.clearRect(0, 0, canvas.getWidth( ), canvas.getHeight( ));

        scale = 0L;
        for (Route route : routes) {
            if (Math.abs(route.getFrom( ).getX( )) > scale) scale = Math.abs(route.getFrom( ).getX( ));
            if (Math.abs(route.getFrom( ).getY( )) > scale) scale = Math.abs(route.getFrom( ).getY( ));
            if (Math.abs(route.getTo( ).getX( )) > scale) scale = Math.abs(route.getTo( ).getX( ));
            if (Math.abs(route.getTo( ).getY( )) > scale) scale = Math.abs(route.getTo( ).getY( ));
        }

        gc = canvas.getGraphicsContext2D( );
        gran = 125;
        gc.strokeLine(canvas.getWidth( ) / 2.0, 0, canvas.getWidth( ) / 2.0, canvas.getHeight( ));
        gc.strokeLine(0, canvas.getHeight( ) / 2.0, canvas.getWidth( ), canvas.getHeight( ) / 2.0);
        if (!routes.isEmpty( )) {
            gc.fillText("-" + scale.toString( ), canvas.getWidth( ) / 2.0 - gran, canvas.getHeight( ) / 2.0 + 15);
            gc.fillText(scale.toString( ), canvas.getWidth( ) / 2.0 + gran, canvas.getHeight( ) / 2.0 + 15);
        }
        gc.fillOval(canvas.getWidth( ) / 2.0 + gran - 2.5, canvas.getHeight( ) / 2.0 - 2, 5, 5);
        gc.fillOval(canvas.getWidth( ) / 2.0 - gran + 2.5, canvas.getHeight( ) / 2.0 - 2, 5, 5);
        gc.fillOval(canvas.getWidth( ) / 2.0 - 2.5, canvas.getHeight( ) / 2.0 + gran - 2, 5, 5);
        gc.fillOval(canvas.getWidth( ) / 2.0 - 2.5, canvas.getHeight( ) / 2.0 - gran - 7, 5, 5);



        for (Route route : routes) {

            gc.beginPath( );
            gc.moveTo((canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale), (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale));
            gc.quadraticCurveTo((canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale), ((canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) + (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale)) / 2.0 - gran / 6, canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale, (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale));
            gc.setStroke(colors.get(route.getUsername( )));
            gc.stroke( );
            gc.closePath( );
            gc.setStroke(Color.BLACK);

            gc.setFill(colors.get(route.getUsername( )));
            gc.fillOval((canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale) - 15, (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - 50, 30, 30);
            gc.fillPolygon(new double[]{canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale - 15, canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale, canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale + 15}, new double[]{(canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - 32, (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale), (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - 32}, 3);
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.fillOval(canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale - 10, (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - 45, 20, 20);
            gc.setFill(Color.BLACK);

            gc.setFill(colors.get(route.getUsername( )));
            gc.fillOval((canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale) - 15, (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale) - 50, 30, 30);
            gc.fillPolygon(new double[]{canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale - 15, canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale, canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale + 15}, new double[]{(canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale) - 32, (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale), (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale) - 32}, 3);
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.fillOval(canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale - 10, (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale) - 45, 20, 20);
            gc.setFill(Color.BLACK);
        }
    }

    public HashMap<String, Color> getColors (LinkedHashSet<Route> routes) {
        HashMap<String, Color> map = new HashMap<>( );
        for (Route route : routes) {
            int a = route.getUsername( ).hashCode( );
            Float r = (a % 1000) * 0.001f;
            Float g = (a % 1000000) * 0.000001f;
            Float b = (a % 100000000) * 0.000000001f;
            Color color = Color.color(Math.abs(r), Math.abs(g), Math.abs(b));
            map.put(route.getUsername( ), color);
        }
        return map;
    }

    public void setColumnsByList (List<Route> listRoutes) throws IOException {
        try {
            ObservableList<FullRoute> list = FXCollections.observableArrayList( );
            for (Route route : listRoutes) {
                list.add(new FullRoute(route));
            }
            table.setItems(list);
            clearFields();

        } catch (IllegalStateException ex) {
            ex.printStackTrace( );
        }
    }

    public String getCommand ( ) {
        return command;
    }

    public void clearFields ( ) {
        idField.clear( );
        ownerField.clear( );
        creationDateField.clear( );
        distanceField.clear( );
        nameField.clear( );
        nowXField.clear( );
        nowYField.clear( );
        fromNameField.clear( );
        fromXField.clear( );
        fromYField.clear( );
        toNameField.clear( );
        toXField.clear( );
        toYField.clear( );
        setEdit(false);
    }

    public void setEdit (boolean op) {
        idField.setEditable(op);
        ownerField.setEditable(op);
        creationDateField.setEditable(op);
        distanceField.setEditable(op);
        nameField.setEditable(op);
        nowXField.setEditable(op);
        nowYField.setEditable(op);
        fromNameField.setEditable(op);
        fromXField.setEditable(op);
        fromYField.setEditable(op);
        toNameField.setEditable(op);
        toXField.setEditable(op);
        toYField.setEditable(op);
    }

}
