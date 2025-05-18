package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileMoveRequest extends AuthorizedMessage {
    private final String filename;
    private final String directory;

    public FileMoveRequest(String authToken, String filename, String directory) {
        super(authToken);
        this.filename = filename;
        this.directory = directory;
    }

    public String getFilename() {
        return filename;
    }
    public String getDirectory() {
        return directory;
    }
}
