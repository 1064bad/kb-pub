package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;
import app.transport.message.Message;

public class FileUploadRewriteConfirmation extends AuthorizedMessage {
    public FileUploadRewriteConfirmation(String authToken) {
        super(authToken);
    }
}
