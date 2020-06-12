package client.models;

import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;

public class ConnectionModel {

    private ClientProviding clientProviding;

    public ConnectionModel (ClientProviding clientProviding) {
        this.clientProviding = clientProviding;
    }

    public String connect(String address, String port) {
        try {
            if (address.contains(" ")) return "Ууууу, в адресе не должно быть пробелов";
            if (port.contains(" ")) return "Порт с пробелами, серьезно? Зачем?";
           clientProviding.setAddress(address);
           clientProviding.setPort(port);
           clientProviding.clientWork();
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
