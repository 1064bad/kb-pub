package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileDeleteRequest extends AuthorizedMessage {
    private final String filename;

    public FileDeleteRequest(String authToken, String filename) {
        super(authToken);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
