package app.transport.echo;

import app.transport.Message;

public class EchoResponse extends Message {
    private final String message;
    public EchoResponse(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
