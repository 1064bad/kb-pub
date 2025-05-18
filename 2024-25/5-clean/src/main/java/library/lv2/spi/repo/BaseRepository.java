package library.lv2.spi.repo;

public interface BaseRepository {
    void beginTransaction();
    void commitTransaction();
    void rollbackTransaction();

    void withinTransaction(TransactionTask task) throws RepositoryAppException;

    interface TransactionTask {
        void perform() throws Exception;
    }
}
