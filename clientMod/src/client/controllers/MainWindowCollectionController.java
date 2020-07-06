package client.controllers;

import client.ClientApp;
import client.FullRoute;
import client.models.ClientProviding;
import client.models.EnterRouteModel;
import client.models.MainWindowCollectionModel;
import client.models.UniversalLocalizationModel;
import common.generatedClasses.Route;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.IOException;
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
    private LinkedHashSet<Route> routes;

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
    private TableColumn<FullRoute, String> date;

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
    private TableColumn<FullRoute, String> Ddistance;

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

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private Label tableEditionResult;


    private String command;

    private GraphicsContext gc;

    private Long scale;

    private double gran;

    private HashMap<Long, Group> pictureFrom;
    private HashMap<Long, Group> pictureTo;
    private volatile int i = 0;
    private Timeline timeline;
    private boolean finish = true;
    private double a;
    private double b;
    private Timeline timeline1;
    private LinkedHashSet<Route> routess;
    private String edResult = "";
    private EnterEverythingController enterEverythingController;
    private String kostil;
    private String tableEdResult;
    private Locale locale;
    private volatile boolean help = false;
    private int p;
    private volatile HashMap<Long, Double> mapforXFrNow;
    private volatile HashMap<Long, Double> mapforYFrNow;
    private volatile HashMap<Long, Double> mapforXToNow;
    private volatile HashMap<Long, Double> mapforYToNow;
    private HashMap<Long, AnimationMove> animationFrHashMap;
    private HashMap<Long, AnimationMove> animationToHashMap;
    private HashMap<Long, Boolean> isAnimRunMapFr;
    private HashMap<Long, Boolean> isAnimRunMapTo;
    private HashMap<Long, Double> mapforXFr;
    private HashMap<Long, Double> mapforYfr;
    private HashMap<Long, Double> mapforXTo;
    private HashMap<Long, Double> mapforYTo;
    private HashMap<Long, Route> r;
    private Group group;


    @FXML
    private void initialize ( ) throws IOException {

        id.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("id"));
        owner.setCellValueFactory(new PropertyValueFactory<FullRoute, String>("username"));
        name.setCellValueFactory(new PropertyValueFactory<FullRoute, String>("name"));
        coordinateX.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("coordinateX"));
        coordinateY.setCellValueFactory(new PropertyValueFactory<FullRoute, Integer>("coordinateY"));
        date.setCellValueFactory(new PropertyValueFactory<FullRoute, String>("date"));
        fromName.setCellValueFactory(new PropertyValueFactory<FullRoute, String>("fromName"));
        fromX.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("fromX"));
        fromY.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("fromY"));
        toName.setCellValueFactory(new PropertyValueFactory<FullRoute, String>("toName"));
        toX.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("toX"));
        toY.setCellValueFactory(new PropertyValueFactory<FullRoute, Long>("toY"));
        Ddistance.setCellValueFactory(new PropertyValueFactory<FullRoute, String>("distance"));

        pictureFrom = new HashMap<>( );
        pictureTo = new HashMap<>( );


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
                        if (mainWindowCollectionModel.localizeDistance(locale, route.getDistance( )).contains(lowerCaseFilter)) {
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
    public void onTableClicked (MouseEvent mouseEvent) {
        if (mouseEvent.getButton( ) == MouseButton.SECONDARY) {
            contextMenu.show(table, mouseEvent.getScreenX( ), mouseEvent.getScreenY( ));
            MenuItem menuItem = contextMenu.getItems( ).get(0);
            MenuItem menuItem1 = contextMenu.getItems( ).get(1);
            menuItem.setText(universalLocalizationModel.translateMeAText(bundle, menuItem.getId( )));
            menuItem1.setText(universalLocalizationModel.translateMeAText(bundle, menuItem1.getId( )));
        }
    }

    @FXML
    public void onActionEditCM (ActionEvent actionEvent) throws IOException {
        ObservableList<TablePosition> observableList = table.getSelectionModel( ).getSelectedCells( );
        for (TablePosition tablePosition : observableList) {
            kostil = tablePosition.getTableColumn( ).getId( );
            if (!kostil.equals("id") && !kostil.equals("owner") && !kostil.equals("date")) {
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

                if (enterEverythingController != null) {
                    Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
                    stage.close( );
                }
                openEnterEverything( );
            }
        }
    }

    @FXML
    public void onActionDeleteCM (ActionEvent actionEvent) throws IOException {
        FullRoute route = (FullRoute) table.getSelectionModel( ).getSelectedItem( );
        String result = mainWindowCollectionModel.removeByIdCommand(route.getId( ).toString( ));
        tableEditionResult.setText(bundle.getString(result));
    }

    @FXML
    public void onActionMouseClicked (MouseEvent mouseEvent) {
        editionResult.setText("");
        edResult = "";
        clearFields( );
        setEdit(true);
        double x = mouseEvent.getX( );
        double y = mouseEvent.getY( );
        for (Route route : clientProviding.getRoutes( )) {
            if (Math.abs(x - (canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale)) < 15 && (((canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - y) < 50 && ((canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - y) > 0) || Math.abs((x - (canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale))) < 15 && ((canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale) - y < 50 && canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale - y > 0)) {
                idField.setText(route.getId( ).toString( ));
                ownerField.setText(route.getUsername( ));
                creationDateField.setText(mainWindowCollectionModel.localizeDate(locale, route.getCreationDate( )));
                distanceField.setText(mainWindowCollectionModel.localizeDistance(locale, route.getDistance( )));
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

        if (enterEverythingController != null) {
            Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
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

        if (enterEverythingController != null) {
            Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
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

        if (enterEverythingController != null) {
            Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
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

        if (enterEverythingController != null) {
            Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
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

        if (enterEverythingController != null) {
            Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
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

        if (enterEverythingController != null) {
            Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
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

        if (enterEverythingController != null) {
            Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
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

        if (enterEverythingController != null) {
            Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
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

    public void openEnterEverything ( ) {
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
                        loader = clientApp.showEnterEverything( );
                    } catch (IOException e) {
                        e.printStackTrace( );
                    }
                    enterEverythingController = loader.getController( );
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

        if (enterEverythingController != null) {
            Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (commandResultController != null) {
            Stage stage = (Stage) commandResultController.getText( ).getScene( ).getWindow( );
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

        if (enterEverythingController != null) {
            Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
            stage.close( );
        }

        if (commandResultController != null) {
            Stage stage = (Stage) commandResultController.getText( ).getScene( ).getWindow( );
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
    public void onActionRussian (ActionEvent actionEvent) throws IOException {
        locale = new Locale("ru", "RU");
        bundle = ResourceBundle.getBundle("languages.LanguageRU");
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ).getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(editionResult, edResult, bundle);
        universalLocalizationModel.updateLabels(tableEditionResult, tableEdResult, bundle);
        setColumns(clientProviding.getRoutes( ));
        if (commandResultController != null) commandResultController.translate(bundle);
        if (enterRouteController != null) enterRouteController.translate(bundle);
        if (enterDistanceController != null) enterDistanceController.translate(bundle);
        if (enterScriptController != null) enterScriptController.translate(bundle);
    }

    @FXML
    public void onActionEstlane (ActionEvent actionEvent) throws IOException {
        locale = new Locale("et", "EE");
        bundle = ResourceBundle.getBundle("languages.LanguageET", locale);
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ).getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(editionResult, edResult, bundle);
        universalLocalizationModel.updateLabels(tableEditionResult, tableEdResult, bundle);
        setColumns(clientProviding.getRoutes( ));
        if (commandResultController != null) commandResultController.translate(bundle);
        if (enterRouteController != null) enterRouteController.translate(bundle);
        if (enterDistanceController != null) enterDistanceController.translate(bundle);
        if (enterScriptController != null) enterScriptController.translate(bundle);


    }

    @FXML
    public void onActionCatala (ActionEvent actionEvent) throws IOException {
        locale = new Locale("ca", "ES");
        bundle = ResourceBundle.getBundle("languages.LanguageCA", locale);
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ).getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(editionResult, edResult, bundle);
        universalLocalizationModel.updateLabels(tableEditionResult, tableEdResult, bundle);
        setColumns(clientProviding.getRoutes( ));
        if (commandResultController != null) commandResultController.translate(bundle);
        if (enterRouteController != null) enterRouteController.translate(bundle);
        if (enterDistanceController != null) enterDistanceController.translate(bundle);
        if (enterScriptController != null) enterScriptController.translate(bundle);

    }

    @FXML
    public void onActionEnglish (ActionEvent actionEvent) throws IOException {
        locale = new Locale("en", "ZA");
        bundle = ResourceBundle.getBundle("languages.LanguageEN", locale);
        universalLocalizationModel.changeLanguage(username.getParent( ).getParent( ).getParent( ).getParent( ), bundle);
        universalLocalizationModel.updateLabels(editionResult, edResult, bundle);
        universalLocalizationModel.updateLabels(tableEditionResult, tableEdResult, bundle);
        setColumns(clientProviding.getRoutes( ));
        if (commandResultController != null) commandResultController.translate(bundle);
        if (enterRouteController != null) enterRouteController.translate(bundle);
        if (enterDistanceController != null) enterDistanceController.translate(bundle);
        if (enterScriptController != null) enterScriptController.translate(bundle);

    }

    @FXML
    public void onActionDelete (ActionEvent actionEvent) throws IOException {
        editionResult.setText("");
        edResult = "";
        if (toNameField.isEditable( )) {
            edResult = mainWindowCollectionModel.removeByIdCommand(idField.getText( ));
            editionResult.setText(bundle.getString(edResult));
            if (edResult.equals("Элемент удален!")) {
                clearFields( );
                setEdit(false);
            }
        }
    }

    @FXML
    public void onActionEdit (ActionEvent actionEvent) throws IOException {
        editionResult.setText("");
        edResult = "";
        if (toNameField.isEditable( )) {
            String result = enterRouteModel.checkRoute(locale, nameField.getText( ), nowXField.getText( ), nowYField.getText( ), fromNameField.getText( ), fromXField.getText( ), fromYField.getText( ), toNameField.getText( ), toXField.getText( ), toYField.getText( ), distanceField.getText( ));

            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                edResult = mainWindowCollectionModel.updateIdCommand(idField.getText( ));
                editionResult.setText(bundle.getString(edResult));
            } else {
                edResult = result;
                editionResult.setText(bundle.getString(edResult));
            }
        }
    }


    public void setEverything (ClientProviding clientProviding, ClientApp clientApp, TabPane tabPane, UniversalLocalizationModel universalLocalizationModel, ResourceBundle bundle, Locale locale) throws IOException {
        this.clientProviding = clientProviding;
        this.clientApp = clientApp;
        this.tabPane = tabPane;
        this.universalLocalizationModel = universalLocalizationModel;
        this.bundle = bundle;
        mainWindowCollectionModel = new MainWindowCollectionModel(clientProviding);
        enterRouteModel = new EnterRouteModel(clientProviding);

        mapforYFrNow = new HashMap<>( );
        mapforXFrNow = new HashMap<>( );
        mapforXToNow = new HashMap<>();
        mapforYToNow = new HashMap<>();
        animationFrHashMap = new HashMap<>( );
        animationToHashMap = new HashMap<>();
        isAnimRunMapFr = new HashMap<>( );
        isAnimRunMapTo = new HashMap<>();
        r = new HashMap<>();

        mapforXFr = new HashMap<>( );
        mapforYfr = new HashMap<>( );
        mapforXTo = new HashMap<>();
        mapforYTo = new HashMap<>();

        clientProviding.getClientNotifying( ).setMainWindowCollectionController(this);

        this.locale = locale;
        routess = clientProviding.getRoutes( );


        drawRoutes(clientProviding.getRoutes( ));
        clientProviding.clientWork( );

        username.setText(clientProviding.getUsername( ));
        username1.setText(clientProviding.getUsername( ));

        AnimationMove.setMainWindowCollectionController(this);

        Platform.runLater(( ) -> {
            universalLocalizationModel.changeLanguage(user.getParent( ).getParent( ).getParent( ).getParent( ), bundle);
        });

    }

    public void setColumns (LinkedHashSet<Route> routes) throws IOException {
        try {
            ObservableList<FullRoute> list = FXCollections.observableArrayList( );
            for (Route route : routes) {
                list.add(new FullRoute(route, locale));
            }


            if (!mainWindowCollectionModel.isEquals(routess, clientProviding.getRoutes( ))) {
//                System.out.println("antosha");
                drawRoutes(routes);
                routess = clientProviding.getRoutes( );
            }

            table.setItems(list);

            clearFields( );
        } catch (IllegalStateException ex) {
            ex.printStackTrace( );
        }

    }

    public void drawRoutes (LinkedHashSet<Route> routes) {

        HashMap<String, Color> colors = getColors(routes);
        if (gc != null) gc.clearRect(0, 0, canvas.getWidth( ), canvas.getHeight( ));

        scale = 2000L;
//        for (Route route : routes) {
//            if (Math.abs(route.getFrom( ).getX( )) > scale) scale = Math.abs(route.getFrom( ).getX( ));
//            if (Math.abs(route.getFrom( ).getY( )) > scale) scale = Math.abs(route.getFrom( ).getY( ));
//            if (Math.abs(route.getTo( ).getX( )) > scale) scale = Math.abs(route.getTo( ).getX( ));
//            if (Math.abs(route.getTo( ).getY( )) > scale) scale = Math.abs(route.getTo( ).getY( ));
//        }

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

        group = (Group) canvas.getParent( );


        for (Map.Entry<Long, Route> pair : r.entrySet( )) {
            Long id = pair.getKey();
            int p = 0;
            for (Route route : routes) {
                if (!route.getId().equals(id)) p++;
            }
            if (p == routes.size()) deleteMetki(id);
        }


        for (Route route : routes) {


            Group fromGroup = new Group( );

            Group toGroup = new Group( );

            gc.beginPath( );
            gc.moveTo((canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale), (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale));
            gc.quadraticCurveTo((canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale), ((canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) + (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale)) / 2.0 - gran / 6, canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale, (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale));
            gc.setStroke(colors.get(route.getUsername( )));
            gc.stroke( );
            gc.closePath( );
            gc.setStroke(Color.BLACK);

            Circle circle = new Circle((canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale), (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - 35, 15);
            circle.setFill(colors.get(route.getUsername( )));
            Polygon polygon = new Polygon( );
            polygon.getPoints( ).addAll(new Double[]{
                    canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale - 15, (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - 32,
                    canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale, (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale),
                    canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale + 15, (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - 32});
            polygon.setFill(colors.get(route.getUsername( )));
            Circle smallCircle = new Circle((canvas.getWidth( ) / 2.0 + (route.getFrom( ).getX( )) * (gran) / scale), (canvas.getHeight( ) / 2.0 - (route.getFrom( ).getY( )) * gran / scale) - 35, 10);
            smallCircle.setFill(Color.WHITE);


            Circle circle2 = new Circle((canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale), (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale) - 35, 15);
            circle2.setFill(colors.get(route.getUsername( )));
            Polygon polygon2 = new Polygon( );
            polygon2.getPoints( ).addAll(new Double[]{
                    canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale - 15, (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale) - 32,
                    canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale, (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale),
                    canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale + 15, (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale) - 32});
            polygon2.setFill(colors.get(route.getUsername( )));
            Circle smallCircle2 = new Circle((canvas.getWidth( ) / 2.0 + (route.getTo( ).getX( )) * (gran) / scale), (canvas.getHeight( ) / 2.0 - (route.getTo( ).getY( )) * gran / scale) - 35, 10);
            smallCircle2.setFill(Color.WHITE);


            Platform.runLater(( ) -> {

                toGroup.getChildren( ).add(polygon2);
                toGroup.getChildren( ).add(circle2);
                toGroup.getChildren( ).add(smallCircle2);

                fromGroup.getChildren( ).add(polygon);
                fromGroup.getChildren( ).add(circle);
                fromGroup.getChildren( ).add(smallCircle);

                getShrekXfrNowYfrNow(route.getId( ), fromGroup);
                getShrekXFrYFr(route.getId( ), fromGroup);
                getFionaXtoNowYtoNow(route.getId( ), toGroup);
                getFionaXToYTo(route.getId( ), toGroup);

                if (!pictureFrom.containsKey(route.getId( ))) {
                    if (!group.getChildren( ).contains(fromGroup)) group.getChildren( ).add(fromGroup);
                    pictureFrom.put(route.getId( ), fromGroup);
                    animationFrHashMap.put(route.getId( ), new AnimationMove(route.getId( )));
                    isAnimRunMapFr.put(route.getId( ), false);
                }

                if (!pictureTo.containsKey(route.getId( ))) {
                    if (!group.getChildren( ).contains(toGroup)) group.getChildren( ).add(toGroup);
                    pictureTo.put(route.getId( ), toGroup);
                    animationToHashMap.put(route.getId( ), new AnimationMove(route.getId( )));
                    isAnimRunMapTo.put(route.getId( ), false);
                }


            });

            if (r.containsKey(route.getId())) {

                if (!route.getFrom( ).equals(r.get(route.getId( )).getFrom( ))) {


                    if (pictureFrom.containsKey(route.getId( ))) {


                        while (isAnimRunMapFr.get(route.getId( ))) ;


                        isAnimRunMapFr.replace(route.getId( ), true);



                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace( );
                        }

//                gc.setFill(Color.GREEN);
//                gc.fillOval(xFrNow, yFrNow + 30, 5, 5);
//                gc.setFill(Color.BLACK);

                        animationFrHashMap.get(route.getId( )).setEverything(pictureFrom.get(route.getId( )), mapforXFrNow.get(route.getId( )), mapforYFrNow.get(route.getId( )) + 1, mapforXFr.get(route.getId( )), mapforYfr.get(route.getId( )));
                        animationFrHashMap.get(route.getId( )).playAnim( );

                        mapforXFr.replace(route.getId( ), mapforXFrNow.get(route.getId( )));
                        mapforYfr.replace(route.getId( ), mapforYFrNow.get(route.getId( )));

                    } else {
//                        if (!group.getChildren( ).contains(fromGroup)) group.getChildren( ).add(fromGroup);
                        pictureFrom.put(route.getId( ), fromGroup);
                        animationFrHashMap.put(route.getId( ), new AnimationMove(route.getId( )));
                        isAnimRunMapFr.put(route.getId( ), false);
                    }

                    r.replace(route.getId(), route);

                }

                if (!route.getTo( ).equals(r.get(route.getId( )).getTo( ))) {

                    if (pictureTo.containsKey(route.getId( ))) {

                        while (isAnimRunMapTo.get(route.getId( ))) ;

                        isAnimRunMapTo.replace(route.getId( ), true);


                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace( );
                        }
//                gc.setFill(Color.GREEN);
//                gc.fillOval(xFrNow, yFrNow + 30, 5, 5);
//                gc.setFill(Color.BLACK);

                        animationToHashMap.get(route.getId( )).setEverything(pictureTo.get(route.getId( )), mapforXToNow.get(route.getId( )), mapforYToNow.get(route.getId( )) + 1, mapforXTo.get(route.getId( )), mapforYTo.get(route.getId( )));
                        animationToHashMap.get(route.getId( )).playAnim( );

                        mapforXTo.replace(route.getId( ), mapforXToNow.get(route.getId( )));
                        mapforYTo.replace(route.getId( ), mapforYToNow.get(route.getId( )));



                    } else {
//                if (!group.getChildren( ).contains(fromGroup)) group.getChildren( ).add(fromGroup);
                        pictureTo.put(route.getId( ), toGroup);
                        animationToHashMap.put(route.getId( ), new AnimationMove(route.getId( )));
                        isAnimRunMapTo.put(route.getId( ), false);
                    }

                    r.replace(route.getId(), route);

                }
            }

            else {
                r.put(route.getId(),route);
            }
        }

    }

    private void deleteMetki ( Long id) {
        Group metka1 = pictureFrom.get(id);
        Group metka2 = pictureTo.get(id);
        Platform.runLater( () -> {
            group.getChildren( ).remove(metka1);
            group.getChildren( ).remove(metka2);
        });
    }

    private void getFionaXToYTo (Long id, Group toGroup) {
        if (!mapforXTo.containsKey(id)) {
            double xTo = 0;
            double yTo = 0;

            for (Node node : toGroup.getChildren( )) {
                if (node instanceof Circle) {
                    xTo = ((Circle) node).getCenterX( );
                    yTo = ((Circle) node).getCenterY( ) + 10;
                }
            }

            mapforXTo.put(id, xTo);
            mapforYTo.put(id, yTo);

//            gc.setFill(Color.PINK);
//            gc.fillOval(mapforXTo.put(id, xTo), mapforYTo.put(id, yTo) + 30, 5, 5);
//            gc.setFill(Color.BLACK);
        }
    }

    private void getFionaXtoNowYtoNow (Long id, Group group) {

        double xToNow = 0;
        double yToNow = 0;

        for (Node node : group.getChildren( )) {
            if (node instanceof Circle) {
                xToNow = ((Circle) node).getCenterX( );
                yToNow = ((Circle) node).getCenterY( ) + 10;
            }
        }

        mapforXToNow.put(id, xToNow);
        mapforYToNow.put(id, yToNow);

//        gc.setFill(Color.BLUE);
//        gc.fillOval(xToNow, yToNow + 60, 5, 5);
//        gc.setFill(Color.BLACK);
    }

    private void getShrekXFrYFr (Long id, Group fromGroup) {
        if (!mapforXFr.containsKey(id)) {
            double xFr = 0;
            double yFr = 0;

            for (Node node : fromGroup.getChildren( )) {
                if (node instanceof Circle) {
                    xFr = ((Circle) node).getCenterX( );
                    yFr = ((Circle) node).getCenterY( ) + 10;
                }
            }

            mapforXFr.put(id, xFr);
            mapforYfr.put(id, yFr);

//            gc.setFill(Color.PINK);
//            gc.fillOval(mapforXFr.put(id, xFr), mapforYfr.put(id, yFr) + 30, 5, 5);
//            gc.setFill(Color.BLACK);
        }

    }

    public void getShrekXfrNowYfrNow (Long id, Group group) {

        double xFrNow = 0;
        double yFrNow = 0;

        for (Node node : group.getChildren( )) {
            if (node instanceof Circle) {
                xFrNow = ((Circle) node).getCenterX( );
                yFrNow = ((Circle) node).getCenterY( ) + 10;
            }
        }

        mapforXFrNow.put(id, xFrNow);
        mapforYFrNow.put(id, yFrNow);

//        gc.setFill(Color.BLUE);
//        gc.fillOval(xFrNow, yFrNow + 60, 5, 5);
//        gc.setFill(Color.BLACK);
    }

    public void pleaseWork (Long id) {
        isAnimRunMapFr.put(id, false);
        isAnimRunMapTo.put(id, false);
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
                list.add(new FullRoute(route, locale));
            }
            table.setItems(list);
            clearFields( );

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

    public Locale getLocale ( ) {
        return locale;
    }

    public void doEdit ( ) throws IOException {
        FullRoute fullRoute = ((FullRoute) table.getSelectionModel( ).getSelectedItem( ));
        if (kostil.equals("name")) {
            String result = clientProviding.getUserManager( ).checkRoute(locale, 0, enterEverythingController.getText( ), fullRoute.getCoordinateX( ).toString( ), ((Integer) fullRoute.getCoordinateY( )).toString( ), fullRoute.getFromName( ), ((Long) fullRoute.getFromX( )).toString( ), fullRoute.getFromY( ).toString( ), fullRoute.getToName( ), ((Long) fullRoute.getToX( )).toString( ), fullRoute.getToY( ).toString( ), fullRoute.getDistance( ).toString( ));
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                tableEdResult = mainWindowCollectionModel.updateIdCommand(fullRoute.getId( ).toString( ));
                tableEditionResult.setText(bundle.getString(tableEdResult));
            } else {
                tableEdResult = result;
                tableEditionResult.setText(bundle.getString(result));
            }
        }

        if (kostil.equals("coordinateX")) {
            String result = clientProviding.getUserManager( ).checkRoute(locale, 0, fullRoute.getName( ), enterEverythingController.getText( ), ((Integer) fullRoute.getCoordinateY( )).toString( ), fullRoute.getFromName( ), ((Long) fullRoute.getFromX( )).toString( ), fullRoute.getFromY( ).toString( ), fullRoute.getToName( ), ((Long) fullRoute.getToX( )).toString( ), fullRoute.getToY( ).toString( ), fullRoute.getDistance( ).toString( ));
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                tableEdResult = mainWindowCollectionModel.updateIdCommand(fullRoute.getId( ).toString( ));
                tableEditionResult.setText(bundle.getString(tableEdResult));
            } else {
                tableEdResult = result;
                tableEditionResult.setText(bundle.getString(result));
            }
        }

        if (kostil.equals("coordinateY")) {
            String result = clientProviding.getUserManager( ).checkRoute(locale, 0, fullRoute.getName( ), fullRoute.getCoordinateX( ).toString( ), enterEverythingController.getText( ), fullRoute.getFromName( ), ((Long) fullRoute.getFromX( )).toString( ), fullRoute.getFromY( ).toString( ), fullRoute.getToName( ), ((Long) fullRoute.getToX( )).toString( ), fullRoute.getToY( ).toString( ), fullRoute.getDistance( ).toString( ));
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                tableEdResult = mainWindowCollectionModel.updateIdCommand(fullRoute.getId( ).toString( ));
                tableEditionResult.setText(bundle.getString(tableEdResult));
            } else {
                tableEdResult = result;
                tableEditionResult.setText(bundle.getString(result));
            }
        }

        if (kostil.equals("fromName")) {
            String result = clientProviding.getUserManager( ).checkRoute(locale, 0, fullRoute.getName( ), fullRoute.getCoordinateX( ).toString( ), ((Integer) fullRoute.getCoordinateY( )).toString( ), enterEverythingController.getText( ), ((Long) fullRoute.getFromX( )).toString( ), fullRoute.getFromY( ).toString( ), fullRoute.getToName( ), ((Long) fullRoute.getToX( )).toString( ), fullRoute.getToY( ).toString( ), fullRoute.getDistance( ).toString( ));
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                tableEdResult = mainWindowCollectionModel.updateIdCommand(fullRoute.getId( ).toString( ));
                tableEditionResult.setText(bundle.getString(tableEdResult));
            } else {
                tableEdResult = result;
                tableEditionResult.setText(bundle.getString(result));
            }
        }

        if (kostil.equals("fromX")) {
            String result = clientProviding.getUserManager( ).checkRoute(locale, 0, fullRoute.getName( ), fullRoute.getCoordinateX( ).toString( ), ((Integer) fullRoute.getCoordinateY( )).toString( ), fullRoute.getFromName( ), enterEverythingController.getText( ), fullRoute.getFromY( ).toString( ), fullRoute.getToName( ), ((Long) fullRoute.getToX( )).toString( ), fullRoute.getToY( ).toString( ), fullRoute.getDistance( ).toString( ));
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                tableEdResult = mainWindowCollectionModel.updateIdCommand(fullRoute.getId( ).toString( ));
                tableEditionResult.setText(bundle.getString(tableEdResult));
            } else {
                tableEdResult = result;
                tableEditionResult.setText(bundle.getString(result));
            }
        }

        if (kostil.equals("fromY")) {
            String result = clientProviding.getUserManager( ).checkRoute(locale, 0, fullRoute.getName( ), fullRoute.getCoordinateX( ).toString( ), ((Integer) fullRoute.getCoordinateY( )).toString( ), fullRoute.getFromName( ), ((Long) fullRoute.getFromX( )).toString( ), enterEverythingController.getText( ), fullRoute.getToName( ), ((Long) fullRoute.getToX( )).toString( ), fullRoute.getToY( ).toString( ), fullRoute.getDistance( ).toString( ));
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                tableEdResult = mainWindowCollectionModel.updateIdCommand(fullRoute.getId( ).toString( ));
                tableEditionResult.setText(bundle.getString(tableEdResult));
            } else {
                tableEdResult = result;
                tableEditionResult.setText(bundle.getString(result));
            }
        }

        if (kostil.equals("toName")) {
            String result = clientProviding.getUserManager( ).checkRoute(locale, 0, fullRoute.getName( ), fullRoute.getCoordinateX( ).toString( ), ((Integer) fullRoute.getCoordinateY( )).toString( ), fullRoute.getFromName( ), ((Long) fullRoute.getFromX( )).toString( ), fullRoute.getFromY( ).toString( ), enterEverythingController.getText( ), ((Long) fullRoute.getToX( )).toString( ), fullRoute.getToY( ).toString( ), fullRoute.getDistance( ).toString( ));
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                tableEdResult = mainWindowCollectionModel.updateIdCommand(fullRoute.getId( ).toString( ));
                tableEditionResult.setText(bundle.getString(tableEdResult));
            } else {
                tableEdResult = result;
                tableEditionResult.setText(bundle.getString(result));
            }
        }

        if (kostil.equals("toX")) {
            String result = clientProviding.getUserManager( ).checkRoute(locale, 0, fullRoute.getName( ), fullRoute.getCoordinateX( ).toString( ), ((Integer) fullRoute.getCoordinateY( )).toString( ), fullRoute.getFromName( ), ((Long) fullRoute.getFromX( )).toString( ), fullRoute.getFromY( ).toString( ), fullRoute.getToName( ), enterEverythingController.getText( ), fullRoute.getToY( ).toString( ), fullRoute.getDistance( ).toString( ));
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                tableEdResult = mainWindowCollectionModel.updateIdCommand(fullRoute.getId( ).toString( ));
                tableEditionResult.setText(bundle.getString(tableEdResult));
            } else {
                tableEdResult = result;
                tableEditionResult.setText(bundle.getString(result));
            }
        }

        if (kostil.equals("toY")) {
            String result = clientProviding.getUserManager( ).checkRoute(locale, 0, fullRoute.getName( ), fullRoute.getCoordinateX( ).toString( ), ((Integer) fullRoute.getCoordinateY( )).toString( ), fullRoute.getFromName( ), ((Long) fullRoute.getFromX( )).toString( ), fullRoute.getFromY( ).toString( ), fullRoute.getToName( ), ((Long) fullRoute.getToX( )).toString( ), enterEverythingController.getText( ), fullRoute.getDistance( ).toString( ));
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                tableEdResult = mainWindowCollectionModel.updateIdCommand(fullRoute.getId( ).toString( ));
                tableEditionResult.setText(bundle.getString(tableEdResult));
            } else {
                tableEdResult = result;
                tableEditionResult.setText(bundle.getString(result));
            }
        }

        if (kostil.equals("Ddistance")) {
            String result = clientProviding.getUserManager( ).checkRoute(locale, 0, fullRoute.getName( ), fullRoute.getCoordinateX( ).toString( ), ((Integer) fullRoute.getCoordinateY( )).toString( ), fullRoute.getFromName( ), ((Long) fullRoute.getFromX( )).toString( ), fullRoute.getFromY( ).toString( ), fullRoute.getToName( ), ((Long) fullRoute.getToX( )).toString( ), fullRoute.getToY( ).toString( ), enterEverythingController.getText( ));
            if (result.equals("Весьма симпатичный маршрут. Так держать")) {
                tableEdResult = mainWindowCollectionModel.updateIdCommand(fullRoute.getId( ).toString( ));
                tableEditionResult.setText(bundle.getString(tableEdResult));
            } else {
                tableEdResult = result;
                tableEditionResult.setText(bundle.getString(result));
            }
        }

        Stage stage = (Stage) enterEverythingController.getTfEdit( ).getScene( ).getWindow( );
        stage.close( );

    }
}
