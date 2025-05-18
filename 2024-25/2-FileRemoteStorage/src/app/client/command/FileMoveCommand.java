package app.client.command;

import app.IO;
import app.client.token.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.DirectoryCreateRequest;
import app.transport.message.storage.DirectoryCreateResponse;
import app.transport.message.storage.FileMoveRequest;

import java.io.IOException;
import java.nio.file.Path;

public class FileMoveCommand extends Command {
    public final TokenHolder tokenHolder;

    public FileMoveCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    protected void performConnected() throws IOException {
        io.print(STR."enter source file name:");
        var filename = io.readln();
        if (filename.isBlank()) {
            throw new CommandException("filename name is empty");
        }
        io.print(STR."enter destination directory name:");
        var directory = io.readln();

        if(Path.of(filename).getParent().getFileName().toString().equals(directory)){
            throw new CommandException("cannot move file to same directory");
        }

        transport.send(new FileMoveRequest(tokenHolder.getToken(), filename, directory));
        expectMessage(DirectoryCreateResponse.class);

        io.println("creating done!");
    }
}
