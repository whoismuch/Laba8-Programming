package client.models;

import languages.*;

import java.io.IOException;
import java.net.ConnectException;

public class AuthenticationModel {

    private ClientProviding clientProviding;

    public AuthenticationModel (ClientProviding clientProviding) {
        this.clientProviding = clientProviding;
    }

    public String authorization(String username, String password) throws IOException {
        try {
            clientProviding.clientWork( );
            String result = clientProviding.authorization(username, password).toString( );
            return result;
        } catch (ConnectException ex) {
            return (LanguageRU.serverIsNotAv);
        }
    }

    public String registration(String username, String password) throws IOException {
        try {
            clientProviding.clientWork( );
            String result = clientProviding.registration(username, password).toString( );
            return result;
        } catch (ConnectException ex) {
            return (LanguageRU.serverIsNotAv);
        }
    }
}
