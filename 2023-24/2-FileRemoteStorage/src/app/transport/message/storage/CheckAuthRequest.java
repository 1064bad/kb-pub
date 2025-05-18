package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class CheckAuthRequest extends AuthorizedMessage {
    public CheckAuthRequest(String authToken) {
        super(authToken);
    }
}
