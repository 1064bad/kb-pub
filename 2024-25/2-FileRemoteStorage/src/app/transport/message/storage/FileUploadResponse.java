package app.transport.message.storage;

import app.transport.message.Message;

public class FileUploadResponse extends Message {
    private final boolean rewriteCollision;

    public FileUploadResponse(boolean rewriteCollision) {
        this.rewriteCollision = rewriteCollision;
    }

    public boolean isRewriteCollision() {
        return rewriteCollision;
    }
}
