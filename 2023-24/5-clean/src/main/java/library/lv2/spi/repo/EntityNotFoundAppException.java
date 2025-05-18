package library.lv2.spi.repo;

import library.AppException;

public class EntityNotFoundAppException extends RepositoryAppException {
    public EntityNotFoundAppException(String message) {
        super(message);
    }
}
