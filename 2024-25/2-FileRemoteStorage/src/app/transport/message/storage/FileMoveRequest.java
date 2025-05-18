package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileMoveRequest extends AuthorizedMessage {
    private final String filePath;
    private final String directory;

    public FileMoveRequest(String authToken, String filePath, String directory) {
        super(authToken);
        this.filePath = filePath;
        this.directory = directory;
    }

    public String getFilePath() {
        return filePath;
    }
    public String getDirectory() {
        return directory;
    }
}
