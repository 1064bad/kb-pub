package app.client.command;

import app.IO;
import app.Settings;
import app.client.token.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.FileListRequest;
import app.transport.message.storage.FileListResponse;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileListCommand extends Command {
    private final TokenHolder tokenHolder;

    public FileListCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    public void performConnected() {
        var dir = io.readln();

        transport.send(new FileListRequest(tokenHolder.getToken(), dir));
        var response = expectMessage(FileListResponse.class);
        var files = response.getFiles();
        if (files.isEmpty()) {
            io.println("<no files>");
        } else {
            files.forEach(io::println);
        }
    }
}
