package app.client.command;

import app.IO;
import app.Settings;
import app.client.token.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDeleteCommand extends Command {
    public final TokenHolder tokenHolder;

    public FileDeleteCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    protected void performConnected() throws IOException {
        io.print(STR."enter path (default '\{Settings.DEFAULT_FILENAME_TO_DELETE}'):");
        var file = io.readln();
        if (file.isBlank()) {
            file = Settings.DEFAULT_FILENAME_TO_DELETE;
        }
        var path = Path.of(file);

        var filename = path.getFileName().toString();
        transport.send(new FileDeleteRequest(tokenHolder.getToken(), filename));
        var response = expectMessage(FileDeleteResponse.class);

        io.println("deleting done!");
    }
}
