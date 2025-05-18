package app.transport.message;

abstract public class AuthorizedMessage extends Message {
    private final String authToken;

    public AuthorizedMessage(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
