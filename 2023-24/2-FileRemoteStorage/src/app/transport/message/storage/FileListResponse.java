package app.transport.message.storage;

import app.transport.message.Message;

import java.util.List;

public class FileListResponse extends Message {
    private final List<String> files;

    public FileListResponse(List<String> files) {
        this.files = files;
    }

    public List<String> getFiles() {
        return files;
    }
}
