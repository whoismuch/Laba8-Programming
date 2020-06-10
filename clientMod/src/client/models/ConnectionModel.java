package client.models;

import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;

public class ConnectionModel {

    public String connect(String address, String port) {
        try {
            if (address.contains(" ")) return "Ууууу, в адресе не должно быть пробелов";
            if (port.contains(" ")) return "Порт с пробелами, серьезно? Зачем?";
            ClientProviding provide = new ClientProviding(address, port);
            provide.clientWork( );
        } catch (UnresolvedAddressException ex) {
            return "Такого адреса, к сожалению, не существует";
        } catch (NumberFormatException ex) {
            return "Порт должен быть циферкой";
        } catch (IOException ex) {
            return "Сервер на данный момент не доступен";
        }
        return "Соединение установлено";
    }

}
