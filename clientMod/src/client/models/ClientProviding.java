package client.models;

import client.ClientNotifying;
import client.DataExchangeWithServer;
import client.UserManager;
import client.controllers.MainWindowCollectionController;
import common.command.CommandDescription;
import common.generatedClasses.Route;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientProviding {

    private DataExchangeWithServer dataExchangeWithServer;
    private UserManager userManager;
    private Selector selector;
    private String arg;
    private String username;
    private String password;
    private String choice;
    private SocketChannel outcomingchannel;
    private SocketAddress outcoming;
    private String address;
    private String port;
    private LinkedHashSet<Route> routes;
    private MainWindowCollectionController mainController;
    private ExecutorService executorService;
    private int a;
    private ClientNotifying clientNotifying;

    public void setMainController (MainWindowCollectionController mainController) {
        this.mainController = mainController;
    }

    public ClientProviding ( ) {
        Scanner scanner = new Scanner(System.in);
        userManager = new UserManager(scanner,
                new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)),
                true);
        a = 0;
    }

    public void setRoutes (LinkedHashSet<Route> routes) {
        this.routes = routes;
    }

    public ClientNotifying getClientNotifying ( ) {
        return clientNotifying;
    }

    /**
     * Устанавливает активное соединение с сервером.
     */
    public void clientWork ( ) {
        try {
            SocketChannel notifyingChannel = SocketChannel.open( );
            SocketChannel outcomingchannel = SocketChannel.open( );
            SocketAddress outcoming = new InetSocketAddress((address), Integer.parseInt(port));

            outcomingchannel.connect(outcoming);
            notifyingChannel.connect(outcoming);


            this.outcomingchannel = outcomingchannel;
            this.outcoming = outcoming;

            dataExchangeWithServer = new DataExchangeWithServer(outcomingchannel);
            DataExchangeWithServer notifyFromServer = new DataExchangeWithServer(notifyingChannel);

            if (a == 0) {
                clientNotifying = new ClientNotifying(notifyingChannel, notifyFromServer, mainController, this);
                new Thread(clientNotifying).start();
                a = 1;
            }
//            ClientNotifying clientNotifying = new ClientNotifying(notifyingChannel, notifyFromServer, mainController, this);
//            executorService = Executors.newSingleThreadExecutor( );
//            executorService.submit(clientNotifying);

            selector = Selector.open( );
            outcomingchannel.configureBlocking(false);
            outcomingchannel.register(selector, SelectionKey.OP_READ);

            selector.select( );
            userManager.setAvailable((HashMap) dataExchangeWithServer.getFromServer( ));

        } catch (IOException ex) {
//            executorService.shutdown( );
            clientWork( );
        } catch (NullPointerException ex) {
        }
    }


    public void exit ( ) {
        userManager.write("Завершение программы.");
        System.exit(0);
    }


    public String sendCommand (String commandname) {
        try {
            CommandDescription command;
            if (userManager.checkElement(commandname)) {
                Route route = userManager.getRoute( );
                route.setUsername(username);
                command = new CommandDescription(commandname, arg, route, username, password, choice);
            } else {
                command = new CommandDescription(commandname, arg, null, username, password, choice);
            }


            dataExchangeWithServer.sendToServer(command);

            selector.select( );
            String s = getResult();
            return s;
        } catch (IOException ex) {
            clientWork( );
        }
        return "hehehe";
    }

    public LinkedHashSet<Route> getRoutes ( ) {
        return routes;
    }

    public String getResult ( ) throws IOException {
        selector.select( );
        String s = dataExchangeWithServer.getFromServer( ).toString( );
        return s;
    }

    public void lostConnection ( ) {
        userManager.writeln("Нет связи с сервером. Подключиться ещё раз (введите {да} или {нет})?");
        String answer;
        while (!(answer = userManager.read( )).equals("да")) {
            switch (answer) {
                case "":
                    break;
                case "нет":
                    exit( );
                    break;
                default:
                    userManager.write("Введите корректный ответ.");
            }
        }
    }

    public void authentication ( ) {
        while (true) {
            String choice = userManager.readChoice("Вас интересует Регистрация или Авторизация? Введите корректный ответ (R или A): ", false);
            String username = userManager.readString("Введите логин: ", false);
            String password = userManager.readString("Введите пароль: ", false);
            if (username.contains(" ") || password.contains(" ")) {
                userManager.writeln("Логин и пароль не должны содержать пробелы");
                continue;
            }
            if (!checkLanguage(username) || !checkLanguage(password) || !checkLanguage(choice)) {
                userManager.writeln("Вам следует использовать только латиницу( Вините helios, не меня");
                continue;
            }
            this.username = username;
            this.password = password;
            this.choice = choice;
            break;
        }

    }

    public void setPort (String port) {
        this.port = port;
    }

    public void setAddress (String address) {
        this.address = address;
    }

    public String authorization (String username, String password) {
        try {
            this.username = username;
            this.password = password;
            this.choice = "A";
            if (username.contains(" ") || password.contains(" ")) {
                return "Логин и пароль не должны содержать пробелы";
            }
            if (!checkLanguage(username) || !checkLanguage(password) || !checkLanguage(choice)) {
                return ("Вам следует использовать только латиницу( Вините helios, не меня");
            }

            CommandDescription command = new CommandDescription(null, null, null, username, password, choice);
            dataExchangeWithServer.sendToServer(command);

            String result = getResult( );
            return result;
        } catch (IOException ex) {
            clientWork( );
        }
        return "hehehe";
    }

    public String registration (String username, String password) throws IOException {
        this.username = username;
        this.password = password;
        this.choice = "R";
        if (username.contains(" ") || password.contains(" ")) {
            return "Логин и пароль не должны содержать пробелы";
        }
        if (!checkLanguage(username) || !checkLanguage(password) || !checkLanguage(choice)) {
            return ("Вам следует использовать только латиницу( Вините helios, не меня");
        }

        CommandDescription command = new CommandDescription(null, null, null, username, password, choice);
        dataExchangeWithServer.sendToServer(command);
        String result = getResult( );
        choice = "A";
        return result;
    }

//    public void getMap ( ) throws IOException {
//        selector.select( );
//        userManager.setAvailable((HashMap) dataExchangeWithServer.getFromServer( ));
//    }

//    public void enterAddress ( ) {
//        address = userManager.readString("Введите адрес: ", false);
//        port = userManager.readString("Введите порт: ", false);
//        everythingIsAlright = true;
//    }

    public boolean checkLanguage (String string) {
        Pattern patlatletter = Pattern.compile("[a-zA-Z]{1}");
        Pattern patnumber = Pattern.compile("[0-9]{1}");
        for (int i = 0; i < string.length( ); i++) {
            Matcher matlatletter = patlatletter.matcher(string.subSequence(i, i + 1));
            Matcher matnumber = patnumber.matcher(string.subSequence(i, i + 1));
            if (!matlatletter.matches( ) && !matnumber.matches( )) {
                return false;
            }
        }
        return true;
    }

    public UserManager getUserManager ( ) {
        return userManager;
    }
}

