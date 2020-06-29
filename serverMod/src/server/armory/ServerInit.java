package server.armory;

import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ServerInit {
    public static void main (String[] args) {
        try {

            args = new String[]{"1374", "5432", "khumachbayramova", "kotenok2502"};
            ServerApp serverApp = new ServerApp( );
            serverApp.beginTheParty(args[0], args[1], args[2], args[3]);

        } catch (UnknownHostException ex) {
            System.out.println("Ой, такого порта же не существует(");
            ServerInit.main(null);
        } catch (NumberFormatException | InputMismatchException ex) {
            System.out.println("Порт должен быть циферкой");
            System.out.print("Введите порт:");
            Scanner scanner = new Scanner(System.in);
            String port = scanner.nextLine( );
            String[] newArgs = new String[]{port, args[1], args[2], args[3]};
            ServerInit.main(newArgs);
        } catch (NullPointerException | ArrayIndexOutOfBoundsException ex) {
            System.out.println("Ну вы чего, все ведь просто\nПорт сервера *пробел* Порт БД *пробел* Логин *пробел* Пароль *пробел*");
        }
    }
}
