package app.server.net;

import app.IO;
import app.server.ServerException;
import app.server.filestorage.FileSystemService;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.*;

public class FileDownloadHandler extends Handler {
    private final FileSystemService fileSystemService;
    private final SessionService sessionService;

    public FileDownloadHandler(Transport transport, IO io, FileSystemService fileSystemService, SessionService sessionService) {
        super(transport, io);
        this.fileSystemService = fileSystemService;
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {
        var req = (FileDownloadRequest) message;
        var username = sessionService.get(Token.fromText(req.getAuthToken())).getString(Session.USERNAME);

        var filename = req.getFilename();
        if (!fileSystemService.fileExists(username, filename)) {
            throw new ServerException(STR."file '\{filename}' not found");
        }

        transport.send(new FileDownloadResponse(fileSystemService.size(username, filename)));

        try (var fileInputStream = fileSystemService.getFileInputStream(username, filename)) {
            fileInputStream.transferTo(transport.getOutputStream());
        } catch (Exception e) {
            throw new ServerException(e);
        }

        io.println(STR."file '\{filename}' uploaded by user '\{username}'");
    }
}
