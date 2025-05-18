package library.lv2.spi.service;

import library.AppException;

public class ServiceAppException extends AppException {
    public ServiceAppException(Throwable cause) {
        super(cause);
    }
}
