package client;

import client.controllers.MainWindowCollectionController;
import client.models.ClientProviding;
import common.generatedClasses.Route;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedHashSet;

public class ClientNotifying implements Runnable {

    private DataExchangeWithServer dataExchangeWithServer;
    private String address;
    private String port;
    private volatile MainWindowCollectionController mainWindowCollectionController;
    private SocketChannel notifyingChannel;
    private Selector selector;
    private ClientProviding clientProviding;

    public ClientNotifying (SocketChannel notifyinChannel, DataExchangeWithServer dataExchangeWithServer, MainWindowCollectionController mainWindowCollectionController, ClientProviding clientProviding) {
        this.notifyingChannel = notifyinChannel;
        this.dataExchangeWithServer = dataExchangeWithServer;
        this.mainWindowCollectionController = mainWindowCollectionController;
        this.clientProviding = clientProviding;
    }


    public void setMainWindowCollectionController (MainWindowCollectionController mainWindowCollectionController) {
        this.mainWindowCollectionController = mainWindowCollectionController;
    }

    @Override
    public void run ( ) {
        try {
            selector = Selector.open( );
            notifyingChannel.configureBlocking(false);
            notifyingChannel.register(selector, SelectionKey.OP_READ);
            LinkedHashSet<Route> routes = new LinkedHashSet<>();
            while (true) {

                selector.select( );
                Object object = dataExchangeWithServer.getFromServer( );
                routes = (LinkedHashSet<Route>) object;
                clientProviding.setRoutes(routes);

                if (mainWindowCollectionController != null) {
                    mainWindowCollectionController.setColumns(routes);
                }
            }
        } catch (ClosedChannelException ex) {
            ex.printStackTrace( );
        } catch (IOException ex) {
            ex.printStackTrace( );
        } catch (ClassCastException ex) {
        } catch (IllegalStateException ex) {

        }
    }
}
