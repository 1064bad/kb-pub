package library.lv2.spi.service;

import library.AppException;

public class ServiceAppException extends AppException {
    public ServiceAppException(String message) {
        super(message);
    }

    public ServiceAppException(Throwable cause) {
        super(cause);
    }
}
