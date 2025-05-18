package library.lv4.controller;

import library.AppException;

public class ControllerAppException extends AppException {
    public ControllerAppException(String message) {
        super(message);
    }

    public ControllerAppException(Throwable cause) {
        super(cause);
    }
}
