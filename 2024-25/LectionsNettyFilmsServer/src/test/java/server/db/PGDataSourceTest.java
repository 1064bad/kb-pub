package server.db;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class PGDataSourceTest {

    @Test
    void getConnection() {
        assertThatCode(() -> {
            try (var x = new PGDataSource("postgres", "secret")
                    .getConnection()) {
                assertThat(x).isNotNull();
            }
        }).doesNotThrowAnyException();
    }
}