package app.server.net;

import app.IO;
import app.transport.Transport;
import app.transport.message.Message;

abstract public class Handler {
    protected final Transport transport;
    protected final IO io;

    public Handler(Transport transport, IO io) {
        this.transport = transport;
        this.io = io;
    }

    abstract public void handle(Message message);
}