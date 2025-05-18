package app.server.net;

import app.IO;
import app.server.filestorage.FileStorageService;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.FileListRequest;
import app.transport.message.storage.FileListResponse;

import java.util.List;

public class FileListHandler extends Handler {
    private final FileStorageService fileSystemService;
    private final SessionService sessionService;

    public FileListHandler(Transport transport, IO io, FileStorageService fileSystemService, SessionService sessionService) {
        super(transport, io);
        this.fileSystemService = fileSystemService;
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {
        var req = (FileListRequest) message;
        var username = sessionService.get(Token.fromText(req.getAuthToken())).getString(Session.USERNAME);
        var directory = req.getDirectory();

        List<String> files = req.getDirectory().isBlank()
                ? fileSystemService.listUserFiles(username)
                : fileSystemService.listUserFilesFromDirectory(username, directory);

        transport.send(new FileListResponse(files));
    }
}
