package library.lv2.spi.repo;

public interface BaseRepository {
    void beginTransaction();
    void commitTransaction();
    void rollbackTransaction();
}
