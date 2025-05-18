package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileUploadRequest extends AuthorizedMessage {
    private final String filename;
    private final long size;

    public FileUploadRequest(String authToken, String filename, long size) {
        super(authToken);
        this.filename = filename;
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public long getSize() {
        return size;
    }
}
