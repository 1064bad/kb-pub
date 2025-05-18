package library.lv5.impl.repo.pg;

import library.lv2.spi.repo.BaseRepository;
import library.lv2.spi.repo.RepositoryAppException;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class PgBaseRepository implements BaseRepository {
    protected final PgDataSource dataSource;

    @Override
    public void beginTransaction() {
        try {
            //dataSource.getConnection().setTransactionIsolation();
            dataSource.getConnection().setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public void commitTransaction() {
        try (var conn = dataSource.getConnection()) {
            conn.commit();
            conn.setAutoCommit(true);
        }
        catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public void rollbackTransaction() {
        try (var conn = dataSource.getConnection()) {
            conn.rollback();
            conn.setAutoCommit(true);
        }
        catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }
}
