package net.core.exception;

public class UnexpectedCommandException extends RuntimeException {
    public UnexpectedCommandException(String message) {
        super(message);
    }
}
