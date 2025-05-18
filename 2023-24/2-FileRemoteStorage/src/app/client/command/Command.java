package app.client.command;

import app.IO;
import app.transport.Transport;
import app.transport.message.ErrorResponse;
import app.transport.message.Message;

abstract public class Command {
    protected final Transport transport;
    protected final IO io;

    public Command(Transport transport, IO io) {
        this.transport = transport;
        this.io = io;
    }

    public void perform() {
        try {
            transport.connect();
            performConnected();
        } catch (Exception e) {
            throw new CommandException(e);
        } finally {
            transport.disconnect();
        }
    }

    protected void performConnected() throws Exception {
        /* do nothing */
    }

    public <T extends Message> T expectMessage(Class<T> type) {
        var msg = transport.receive();
        if (msg instanceof ErrorResponse error) {
            throw new CommandException(error.getMessage());
        }
        try {
            return type.cast(msg);
        } catch (ClassCastException e) {
            throw new CommandException(STR."unexpected received message type - \{msg.getClass().getSimpleName()}");
        }
    }
}
