package library.lv5.impl.repo.pg;

import library.lv2.spi.repo.BaseRepository;
import library.lv2.spi.repo.RepositoryAppException;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class PgBaseRepository implements BaseRepository {
    private final PgDataSource dataSource;
    private final ThreadLocal<Connection> threadConnection = new ThreadLocal<>();

    @Override
    public void withinTransaction(TransactionTask task) throws RepositoryAppException {
        if (threadConnection.get() != null) {
            throw new RepositoryAppException("transaction already in use");
        }
        try (var connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            threadConnection.set(connection);
            try {
                task.perform(); // !!!
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw new RepositoryAppException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RepositoryAppException(e);
        } finally {
            threadConnection.remove();
        }
    }

    protected Connection getConnection() throws SQLException {
        return threadConnection.get() != null ? threadConnection.get() : dataSource.getConnection();
    }

    @Override
    public void beginTransaction() {
        try {
            //dataSource.getConnection().setTransactionIsolation();
            dataSource.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public void commitTransaction() {
        try (var conn = dataSource.getConnection()) {
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public void rollbackTransaction() {
        try (var conn = dataSource.getConnection()) {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }
}
