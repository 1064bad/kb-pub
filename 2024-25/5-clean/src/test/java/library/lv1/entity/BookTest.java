package library.lv1.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class BookTest {
    @Test
    void givenNonNegativeId_whenConstruct_thenOk() {
        // arrange
        var ids = List.of(0L, 1L, 999999L, Long.MAX_VALUE);

        // act

        // assert
        ids.forEach(id -> Assertions.assertThatNoException()
                .isThrownBy(() -> new Book(id, "testTitle", 2020)));
    }
    @Test
    void givenNegativeId_whenConstruct_thenThrows() {
        // arrange
        var ids = List.of(-1L, -999999L, Long.MIN_VALUE);

        // act

        // assert
        ids.forEach(id -> Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Book(id, "testTitle", 2020)));
    }
}