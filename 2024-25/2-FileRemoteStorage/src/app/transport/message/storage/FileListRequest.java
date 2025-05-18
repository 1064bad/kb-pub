package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileListRequest extends AuthorizedMessage {
    private final String directory;

    public FileListRequest(String authToken, String directory) {
        super(authToken);
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

}
