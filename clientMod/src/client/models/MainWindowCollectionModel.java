package client.models;

import common.generatedClasses.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class MainWindowCollectionModel {

    private ClientProviding clientProviding;

    public MainWindowCollectionModel (ClientProviding clientProviding) {
        this.clientProviding = clientProviding;
    }

    public LinkedHashSet<Route> giveMeMyCollection () throws IOException {
        clientProviding.clientWork( );
        clientProviding.getMap( );
        return clientProviding.getRoutes( );
    }
}
