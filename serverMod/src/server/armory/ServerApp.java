package server.armory;

import common.generatedClasses.Route;
import server.receiver.collection.Navigator;
import server.receiver.collection.RouteBook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {

    private volatile List<Socket> clients;


    public void beginTheParty (String port, String portBD, String login, String password) throws UnknownHostException, NumberFormatException, InputMismatchException {
        int myport = Integer.parseInt(port);
        SocketAddress address = new InetSocketAddress(myport);

        DataBase db = new DataBase(portBD, login, password);
        RouteBook routeBook = new RouteBook( );
        Navigator navigator = new Navigator(routeBook, db);
        clients = new ArrayList<>( );
        Driver driver = new Driver( );
        navigator.setServerApp(this);
        navigator.loadBegin( );

        System.out.print("Сервер начал слушать клиентов" + "\nПорт " + port +
                " / Адрес " + InetAddress.getLocalHost( ) + ".\nОжидаем подключения клиента\n ");

        ExecutorService executor = Executors.newFixedThreadPool(8);
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        Runtime.getRuntime( ).addShutdownHook(new Thread(( ) -> {
            executor.shutdown( );
            executorService.shutdown( );
            db.theEnd( );
        }));

        while (true) {
            try (ServerSocketChannel ss = ServerSocketChannel.open( )) {
                ss.bind(address);

                Socket incoming = ss.accept( ).socket( );
                Socket listener = ss.accept( ).socket( );

                System.out.println(incoming + " подключился к серверу.");

                clients.add(listener);
                notifyClients(routeBook.getRoutes( ));

                SendToClient sendToClient = new SendToClient(incoming);

                sendToClient.setMessage(driver.getAvailable( ));
                sendToClient.run( );


                GetFromClient getFromClient = new GetFromClient(incoming, listener, clients, db, navigator, routeBook, driver, executorService, sendToClient);
                executor.submit(getFromClient);


            } catch (UnknownHostException | NumberFormatException ex) {
                System.out.println("Ой, неполадочки");
            } catch (NoSuchElementException ex) {
                System.out.println("Какое такое зло я вам сделала?");
            } catch (IOException e) {
                e.printStackTrace( );
            }
        }

    }

    public void notifyClients (LinkedHashSet<Route> routes) {
        Iterator<Socket> it = clients.iterator( );
        while (it.hasNext( )) {
            try {
                Socket socket = it.next( );
                ByteArrayOutputStream baos = new ByteArrayOutputStream( );
                ObjectOutputStream send = new ObjectOutputStream(baos);
                send.writeObject(routes);
                byte[] outcoming = baos.toByteArray( );
                socket.getOutputStream( ).write(outcoming);
                send.flush( );
                baos.flush( );
                send.close( );
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
