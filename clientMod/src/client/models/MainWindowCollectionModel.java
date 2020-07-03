package client.models;

import client.FullRoute;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import common.generatedClasses.Coordinates;
import common.generatedClasses.Location;
import common.generatedClasses.Route;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;

public class MainWindowCollectionModel {

    private ClientProviding clientProviding;

    public MainWindowCollectionModel (ClientProviding clientProviding) {
        this.clientProviding = clientProviding;
    }

    public LinkedHashSet<Route> giveMeMyCollection () throws IOException {
        return clientProviding.getRoutes( );
    }

    public String infoCommand() throws IOException {
        clientProviding.clientWork();
        return clientProviding.sendCommand("info").toString();
    }

    public String sumOfDistanceCommand () throws IOException {
        clientProviding.clientWork();
        return clientProviding.sendCommand("sum_of_distance").toString();
    }

    public String historyCommand ( ) throws IOException {
        clientProviding.clientWork();
        return clientProviding.sendCommand("history").toString();
    }

    public String clearCommand ( ) throws IOException {
        clientProviding.clientWork();
        return clientProviding.sendCommand("clear").toString();
    }

    public void addCommand () throws IOException {
        clientProviding.clientWork();
        clientProviding.sendCommand("add");
    }

    public List<Route>  printAscendingCommand() throws IOException {
        clientProviding.clientWork();
        Object o = clientProviding.sendCommand("print_ascending");
        if (o.getClass().equals("".getClass())) return null;
        else return (List<Route>) o;
    }

    public List<Route> filterLessThanDistance(String dist) throws IOException {
        clientProviding.clientWork();
        clientProviding.setArg(dist);
        Object o = clientProviding.sendCommand("filter_less_than_distance");
        List<Route> list = new ArrayList<>();
        if (o.getClass().equals("".getClass())) return list;
        else return (List<Route>) o;
    }

    public void removeLowerCommand() throws IOException {
        clientProviding.clientWork();
        clientProviding.sendCommand("remove_lower");
    }

    public void removeGreaterCommand() throws IOException {
        clientProviding.clientWork();
        clientProviding.sendCommand("remove_greater");
    }

    public String executeScriptCommand (String arg) throws IOException {
        clientProviding.clientWork();
        clientProviding.setArg(arg);
        return clientProviding.sendCommand("execute_script").toString();
    }

    public String removeByIdCommand (String arg) throws IOException {
        clientProviding.clientWork();
        clientProviding.setArg(arg);
        return clientProviding.sendCommand("remove_by_id").toString();
    }

    public String updateIdCommand (String arg) throws IOException {
        clientProviding.clientWork();
        clientProviding.setArg(arg);
        return clientProviding.sendCommand("update_id").toString();
    }

    public Route convertRoute(FullRoute fullRoute) {
        Route route = new Route(fullRoute.getName(), fullRoute.getId(), new Coordinates(fullRoute.getCoordinateX(), fullRoute.getCoordinateY()), fullRoute.getCreationDate(), new Location(fullRoute.getFromName(), fullRoute.getFromX(), fullRoute.getFromY()), new Location(fullRoute.getToName(), fullRoute.getToX(), fullRoute.getToY()), fullRoute.getDistance());
        return route;
    }

    public String localizeDate(Locale locale, ZonedDateTime creationDate) {
        Date date1 = Date.from(creationDate.toInstant());
        DateFormat ffInstance = DateFormat.getDateTimeInstance(
                DateFormat.FULL, DateFormat.MEDIUM, locale);

        return  ffInstance.format(date1);
    }

}
