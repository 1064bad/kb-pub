package app.client.command;

import app.IO;
import app.Settings;
import app.client.token.TokenHolder;
import app.transport.Transport;
import app.transport.message.SuccessResponse;
import app.transport.message.storage.FileDownloadRequest;
import app.transport.message.storage.FileDownloadResponse;

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
        io.print(STR."enter filename to delete (default '\{Settings.DEFAULT_FILENAME_TO_DOWNLOAD}'): ");
        var filename = io.readln();
        if (filename.isBlank()) {
            filename = Settings.DEFAULT_FILENAME_TO_DOWNLOAD;
        }

        transport.send(new FileDownloadRequest(tokenHolder.getToken(), filename));
        expectMessage(SuccessResponse.class);
    }
}
