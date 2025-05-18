package kb.tmp2.orm.controller;

import kb.tmp2.orm.AppException;
import lombok.Getter;

@Getter
public class HttpServiceException extends AppException {
    private final int code;

    public HttpServiceException(int code) {
        this.code = code;
    }

    public HttpServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public HttpServiceException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public HttpServiceException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }
}
