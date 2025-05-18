package app.transport.message.storage;

import app.transport.message.Message;

public class RegisterUsernameRequest extends Message {
    private final String username;

    public RegisterUsernameRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return STR."RegisterUsernameRequest{username='\{username}'}";
    }
}
