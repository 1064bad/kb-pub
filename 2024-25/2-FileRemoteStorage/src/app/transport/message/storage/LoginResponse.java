package app.transport.message.storage;

import app.transport.message.Message;

public class LoginResponse extends Message {
    private final String token;

    public String getToken() {
        return token;
    }

    public LoginResponse(String token) {
        this.token = token;
    }
}
