package client.models;

import client.DataExchangeWithServer;
import client.UserManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientProviding {

    private DataExchangeWithServer dataExchangeWithServer;
    private UserManager userManager;
    private Selector selector;
    private String commandname = "check";
    private String arg;
    private String username;
    private String password;
    private String choice;
    private SocketChannel outcomingchannel;
    private SocketAddress outcoming;
    private boolean everythingIsAlright;
    private String address;
    private String port;
    private int alrightAuthentication = 0;
    private boolean good;
    private LinkedHashSet<Route> routes;

    public ClientProviding ( ) {
        Scanner scanner = new Scanner(System.in);
        userManager = new UserManager(scanner,
                new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)),
                true);
    }

    /**
     * Устанавливает активное соединение с сервером.
     */
    public void clientWork ( ) throws UnresolvedAddressException, NumberFormatException, IOException {
        try {
            SocketChannel outcomingchannel = SocketChannel.open( );
            SocketAddress outcoming = new InetSocketAddress((address), Integer.parseInt(port));

            everythingIsAlright = true;

            outcomingchannel.connect(outcoming);

            this.outcomingchannel = outcomingchannel;


            this.outcoming = outcoming;

            dataExchangeWithServer = new DataExchangeWithServer(outcomingchannel);

            selector = Selector.open( );
            outcomingchannel.configureBlocking(false);
            outcomingchannel.register(selector, SelectionKey.OP_READ);
        }
//        } catch (UnresolvedAddressException ex) {
//            userManager.writeln("Ойойой, такого адреса ведь не существует");
//            enterAddress( );
//            clientWork( );
//        } catch (NumberFormatException ex) {
//            userManager.writeln("Тут, видимо, должна быть циферка, попробуйте еще раз, позязя");
//            enterAddress( );
//            clientWork( );
//        }
//            catch (IOException e) {
//                lostConnection( );
//                clientWork( );
        catch (NoSuchElementException ex) {
            userManager.writeln("Ну и зачем?");
        } catch (NullPointerException ex) {
            userManager.writeln("Упссс...У нас сетевые неполадочки");
            clientWork( );
        }
    }


    public void clientLaunch ( ) {
        try {
            String line = "check";
            while (!line.equals("exit")) {

                while (alrightAuthentication == 0) {
                    selector.select( );
                    userManager.setAvailable((HashMap) dataExchangeWithServer.getFromServer( ));
                    authentication( );
                    CommandDescription command = new CommandDescription(null, null, null, username, password, choice);
                    dataExchangeWithServer.sendToServer(command);
//                        if (getResult( )) {
//                            choice = "A";
//                            alrightAuthentication = 1;
//                        }
                }


                if (everythingIsAlright) {
                    selector.select( );
                    userManager.setAvailable((HashMap) dataExchangeWithServer.getFromServer( ));
                }

                everythingIsAlright = true;


                userManager.write("Введите команду: ");
                line = userManager.read( );
                line = line.trim( );
                commandname = line;
                arg = null;
                if (line.indexOf(" ") != -1) {
                    commandname = line.substring(0, line.indexOf(" "));
                    arg = (line.substring(line.indexOf(" "))).trim( );
                }

                if (!userManager.checkCommandName(commandname)) {
                    everythingIsAlright = false;
                    continue;
                }

                if (!userManager.checkArg(commandname, arg)) {
                    everythingIsAlright = false;
                    continue;
                }

                if (userManager.checkFile(commandname)) {
                    arg = userManager.contentOfFile(arg);
                    userManager.setFinalScript(arg);
                    if (arg == null) {
                        everythingIsAlright = false;
                        continue;
                    } else {
                        int commandNumber = userManager.checkContentOfFile(arg, 0);
                        if (commandNumber == 0) {
                            userManager.writeln("Бе, скрипт с ошибочками, такой скрипт мы обработать не сможем\nПожалуй, исправьте скрипт и введите следующую команду");
                            continue;
                        }
                        arg = userManager.getFinalScript( );
                        sendCommand( commandname);
                        getResult( );
                    }
                } else {
                    sendCommand( commandname);
                    getResult( );
                }

            }

        } catch (IOException e) {
//                clientWork( );
        }
    }

    public void exit ( ) {
        userManager.write("Завершение программы.");
        System.exit(0);
    }

    public void sendCommand (String commandname) throws IOException {
        CommandDescription command;
        if (userManager.checkElement(commandname)) {
            Route route = userManager.readRoute( );
            route.setUsername(username);
            command = new CommandDescription(commandname, arg, route, username, password, choice);
        } else {
            command = new CommandDescription(commandname, arg, null, username, password, choice);
        }

        dataExchangeWithServer.sendToServer(command);
    }

    public LinkedHashSet<Route> getRoutes ( ) {
        return routes;
    }

    public String getResult ( ) throws IOException {
        selector.select();
        routes = (LinkedHashSet<Route>) dataExchangeWithServer.getFromServer();
        selector.select( );
        String s = dataExchangeWithServer.getFromServer( ).toString( );
        return s;

//            if (s.contains("\nИзвините, ваш запрос не может быть выполнен. Попробуйте еще раз")) {
//                authentication( );
//                return false;
//            }
//
//            if (s.equals("Пользователь с таким логином не зарегистрирован") || s.equals("Вы ввели неправильный пароль") || s.equals("Пользователь с таким логином уже зарегистрирован. Может, вам стоит авторизоваться?")) {
//                return false;
//            }
//
//            return true;
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

    public String authorization (String username, String password) throws IOException {
        this.username = username;
        this.password = password;
        this.choice = "A";
        if (username.contains(" ") || password.contains(" ")) {
            return "Логин и пароль не должны содержать пробелы";
        }
        if (!checkLanguage(username) || !checkLanguage(password) || !checkLanguage(choice)) {
            return ("Вам следует использовать только латиницу( Вините helios, не меня");
        }


        selector.select( );
        userManager.setAvailable((HashMap) dataExchangeWithServer.getFromServer( ));

        CommandDescription command = new CommandDescription(null, null, null, username, password, choice);
        dataExchangeWithServer.sendToServer(command);
        String result = getResult( );
        return result;
    }

    public String registration(String username, String password) throws IOException {
        this.username = username;
        this.password = password;
        this.choice = "R";
        if (username.contains(" ") || password.contains(" ")) {
            return "Логин и пароль не должны содержать пробелы";
        }
        if (!checkLanguage(username) || !checkLanguage(password) || !checkLanguage(choice)) {
            return ("Вам следует использовать только латиницу( Вините helios, не меня");
        }

        selector.select( );
        userManager.setAvailable((HashMap) dataExchangeWithServer.getFromServer( ));

        CommandDescription command = new CommandDescription(null, null, null, username, password, choice);
        dataExchangeWithServer.sendToServer(command);
        String result = getResult( );
        choice = "A";
        return result;
    }

    public void getMap() throws IOException {
        selector.select( );
        userManager.setAvailable((HashMap) dataExchangeWithServer.getFromServer( ));
    }

    public void enterAddress ( ) {
        address = userManager.readString("Введите адрес: ", false);
        port = userManager.readString("Введите порт: ", false);
        everythingIsAlright = true;
    }

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
}

