package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileUploadRequest extends AuthorizedMessage {
    private final String filename;
    private final long size;
    private final String directory;

    public FileUploadRequest(String authToken, String filename, long size, String directory) {
        super(authToken);
        this.filename = filename;
        this.size = size;
        this.directory = directory;
    }

    public String getFilename() {
        return filename;
    }

    public long getSize() {
        return size;
    }

    public String getDirectory(){ return directory;}
}
