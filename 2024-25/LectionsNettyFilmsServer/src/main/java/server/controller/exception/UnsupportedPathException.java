package server.controller.exception;

import server.net.exception.NetException;

public class UnsupportedPathException extends NetException {
    public UnsupportedPathException() {
    }

    public UnsupportedPathException(String message) {
        super(message);
    }

    public UnsupportedPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedPathException(Throwable cause) {
        super(cause);
    }

    public UnsupportedPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
