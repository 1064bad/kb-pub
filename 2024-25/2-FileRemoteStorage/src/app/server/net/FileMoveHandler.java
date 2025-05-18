package app.server.net;

import app.IO;
import app.server.ServerException;
import app.server.filestorage.FileStorageService;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.*;

public class FileMoveHandler extends Handler {
    private final FileStorageService fileSystemService;
    private final SessionService sessionService;

    public FileMoveHandler(Transport transport, IO io, FileStorageService fileStorageService, SessionService sessionService) {
        super(transport, io);
        this.fileSystemService = fileStorageService;
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {
        var req = (FileMoveRequest) message;
        var username = sessionService.get(Token.fromText(req.getAuthToken())).getString(Session.USERNAME);

        var filename = req.getFilename();
        var directory = req.getDirectory();
        var fileExists = fileSystemService.fileExists(username, filename);
        transport.send(new FileMoveResponse(fileExists));
        if (fileExists) {
            transport.receive(FileUploadRewriteConfirmation.class);
        }

        try (var fileOutputStream = fileSystemService.getFileOutputStream(username, filename)) {
            var transpotInputStream = transport.getInputStream();
            var transferred = transpotInputStream.transferTo(fileOutputStream);
            fileOutputStream.flush();
            assert transferred == req.getSize();
        } catch (Exception e) {
            throw new ServerException(e);
        }

        io.println(STR."file '\{filename}' moved to \{directory} by user '\{username}'");
    }
}
