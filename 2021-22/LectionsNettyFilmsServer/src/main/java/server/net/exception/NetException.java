package server.net.exception;

public class NetException extends RuntimeException {
    public NetException() {
        super();
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetException(Throwable cause) {
        super(cause);
    }

    protected NetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
