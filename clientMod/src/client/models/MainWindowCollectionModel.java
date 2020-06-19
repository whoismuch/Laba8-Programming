package client.models;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import common.generatedClasses.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
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

    public void removeLowerCommand() {
        clientProviding.clientWork();
        clientProviding.sendCommand("remove_lower");
    }

    public void removeGreaterCommand() {
        clientProviding.clientWork();
        clientProviding.sendCommand("remove_greater");
    }

}
