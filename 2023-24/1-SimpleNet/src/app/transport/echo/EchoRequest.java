package app.transport.echo;

import app.transport.Message;

public class EchoRequest extends Message {
    private final String message;

    public EchoRequest(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
