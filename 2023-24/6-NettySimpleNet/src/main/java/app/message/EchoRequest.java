package app.message;

public class EchoRequest extends Message {
    private static final long serialVersionUID = -1L;
    private final String message;

    public EchoRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
