package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileListRequest extends AuthorizedMessage {
    public FileListRequest(String authToken) {
        super(authToken);
    }
}
