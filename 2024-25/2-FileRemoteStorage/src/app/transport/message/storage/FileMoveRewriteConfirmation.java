package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileMoveRewriteConfirmation extends AuthorizedMessage {
    public FileMoveRewriteConfirmation(String authToken) {
        super(authToken);
    }
}
