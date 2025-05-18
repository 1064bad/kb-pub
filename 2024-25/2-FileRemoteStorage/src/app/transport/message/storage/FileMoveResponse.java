package app.transport.message.storage;

import app.transport.message.Message;

public class FileMoveResponse extends Message {

    private final boolean rewriteCollision;

    public FileMoveResponse(boolean rewriteCollision) {
        this.rewriteCollision = rewriteCollision;
    }

    public boolean isRewriteCollision() {
        return rewriteCollision;
    }
}
