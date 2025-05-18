package server.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.db.FilmServiceFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

class FilmServiceFactoryTest {

    @Test
    void getInstance() {
        Assertions.assertThat(FilmServiceFactory.getInstance()).isNotNull();
    }
}