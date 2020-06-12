package client.models;

import java.io.IOException;

public class AuthenticationModel {

    private ClientProviding clientProviding;

    public AuthenticationModel (ClientProviding clientProviding) {
        this.clientProviding = clientProviding;
    }

    public String authorization(String username, String password) throws IOException {
        clientProviding.clientWork();
        String result = clientProviding.authorization(username, password);
        return result;
    }

    public String registration(String username, String password) throws IOException {
        clientProviding.clientWork();
        String result = clientProviding.registration(username, password);
        return result;
    }
}
