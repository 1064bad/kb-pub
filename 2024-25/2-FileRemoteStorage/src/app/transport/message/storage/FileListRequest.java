package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileListRequest extends AuthorizedMessage {
    private final String direction;

    public FileListRequest(String authToken, String direction) {
        super(authToken);
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

}
