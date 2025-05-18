package app.client.command;

import app.IO;
import app.Settings;
import app.client.token.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDownloadCommand extends Command {
    public final TokenHolder tokenHolder;

    public FileDownloadCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    protected void performConnected() throws IOException {
        io.print(STR."enter filename (default '\{Settings.DEFAULT_FILENAME_TO_DOWNLOAD}'): ");
        var filename = io.readln();
        if (filename.isBlank()) {
            filename = Settings.DEFAULT_FILENAME_TO_DOWNLOAD;
        }

        transport.send(new FileDownloadRequest(tokenHolder.getToken(), filename));
        var response = expectMessage(FileDownloadResponse.class);

        var temp = Files.createTempFile("file-storage_", "");
        try (var tempOutputStream = Files.newOutputStream(temp)) {
            transport.getInputStream().transferTo(tempOutputStream);
        }

        var downloaded = Path.of(Settings.CLIENT_FILE_STORAGE_BASE_PATH, filename);
        Files.move(temp, downloaded);

        new ProcessBuilder("/usr/bin/open", downloaded.toAbsolutePath().toString()).start();
    }
}
