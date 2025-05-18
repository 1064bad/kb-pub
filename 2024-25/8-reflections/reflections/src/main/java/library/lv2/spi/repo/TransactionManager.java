package library.lv2.spi.repo;

import java.util.concurrent.Callable;

public interface TransactionManager {
    <T> void doInTransaction(Callable<T> action) throws Exception;
}