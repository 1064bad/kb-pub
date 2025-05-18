package library.lv5.impl.repo.pg;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import library.lv0.crosscutting.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PgDataSource {
//    private Connection connection = null;
//
//    public Connection getConnection() throws SQLException {
//        if (connection == null || connection.isClosed()) {
//            connection = DriverManager.getConnection(
//                    Settings.PG_CONN_STRING,
//                    Settings.PG_USERNAME,
//                    Settings.PG_PASSWORD);
//        }
//        return connection;
//    }


    private final HikariDataSource ds;

    public PgDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Settings.PG_CONN_STRING);
        config.setUsername(Settings.PG_USERNAME);
        config.setPassword(Settings.PG_PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
