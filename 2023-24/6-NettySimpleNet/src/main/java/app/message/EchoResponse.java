package app.message;

public class EchoResponse extends Message {
    private static final long serialVersionUID = -1L;
    private final String message;
    public EchoResponse(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
