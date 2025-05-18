package app.client.command;

import app.IO;
import app.transport.Transport;

public class PrintCommand extends Command {
    private final String message;

    public PrintCommand(Transport transport, IO io, String message) {
        super(transport, io);
        this.message = message;
    }

    @Override
    public void perform() {
        io.println(message);
    }
}
