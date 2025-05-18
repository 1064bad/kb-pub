package app.server.net;

import app.IO;
import app.server.ServerException;
import app.server.filestorage.FileStorageService;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.FileDeleteRequest;
import app.transport.message.storage.FileDeleteResponse;
import app.transport.message.storage.FileDownloadRequest;
import app.transport.message.storage.FileDownloadResponse;

public class FileDeletionHandler extends Handler {
    private final FileStorageService fileSystemService;
    private final SessionService sessionService;

    public FileDeletionHandler(Transport transport, IO io, FileStorageService fileStorageService, SessionService sessionService) {
        super(transport, io);
        this.fileSystemService = fileStorageService;
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {
        var req = (FileDeleteRequest) message;
        var username = sessionService.get(Token.fromText(req.getAuthToken())).getString(Session.USERNAME);

        var filename = req.getFilename();
        if (!fileSystemService.fileExists(username, filename)) {
            throw new ServerException(STR."file '\{filename}' not found");
        }

        try {
            fileSystemService.deleteFile(username, filename);
        } catch (Exception e) {
            throw new ServerException(e);
        }

        transport.send(new FileDeleteResponse());

        io.println(STR."file '\{filename}' deleted by user '\{username}'");
    }
}
