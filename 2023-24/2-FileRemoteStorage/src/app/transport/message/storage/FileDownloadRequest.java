package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileDownloadRequest extends AuthorizedMessage {
    private final String filename;

    public FileDownloadRequest(String authToken, String filename) {
        super(authToken);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
