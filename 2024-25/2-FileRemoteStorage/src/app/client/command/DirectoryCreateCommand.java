package app.client.command;

import app.IO;
import app.Settings;
import app.client.token.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryCreateCommand extends Command {
    public final TokenHolder tokenHolder;

    public DirectoryCreateCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    protected void performConnected() throws IOException {
        io.print(STR."enter directory name:");
        var directory = io.readln();
        if (directory.isBlank()) {
            throw new CommandException("directory name is empty");
        }

        transport.send(new DirectoryCreateRequest(tokenHolder.getToken(), directory));
        expectMessage(DirectoryCreateResponse.class);

        io.println("creating done!");
    }
}
