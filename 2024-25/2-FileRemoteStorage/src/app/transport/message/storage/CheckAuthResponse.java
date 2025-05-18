package app.transport.message.storage;

import app.transport.message.Message;

public class CheckAuthResponse extends Message {
    private final String message;

    public CheckAuthResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
