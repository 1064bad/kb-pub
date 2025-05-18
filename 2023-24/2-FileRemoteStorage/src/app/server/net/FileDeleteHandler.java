package app.server.net;

import app.IO;
import app.server.ServerException;
import app.server.filestorage.FileSystemService;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.SuccessResponse;
import app.transport.message.storage.FileDeleteRequest;
import app.transport.message.storage.FileDownloadResponse;

public class FileDeleteHandler extends Handler {
    private final FileSystemService fileSystemService;
    private final SessionService sessionService;

    public FileDeleteHandler(Transport transport, IO io, FileSystemService fileSystemService, SessionService sessionService) {
        super(transport, io);
        this.fileSystemService = fileSystemService;
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {
        var req = (FileDeleteRequest) message;
        var username = sessionService.get(Token.fromText(req.getAuthToken()))
                .getString(Session.USERNAME);

        var filename = req.getFilename();
        if (!fileSystemService.fileExists(username, filename)) {
            throw new ServerException(STR."file '\{filename}' not found");
        }

        fileSystemService.delete(username, filename);

        transport.send(new SuccessResponse());
    }
}
