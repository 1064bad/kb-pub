package library.lv3.usecase;

import library.lv1.entity.Book;
import library.lv2.spi.repo.BookRepository;
import library.lv3.usecase.dto.BookMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.IntStream;

class GetAllBooksInteractorTest {
    GetAllBooksInteractor sut;
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        sut = new GetAllBooksInteractor(bookRepository);
    }

    @Test
    void givenNoBooks_whenGet_returnsEmptyResponse() {
        Mockito.when(bookRepository.findAll()).thenReturn(List.of());
        var result = new GetAllBooksInteractor.Response(List.of());

        Assertions.assertThat(sut.get()).isEqualTo(result);
    }

    @Test
    void givenBooks_whenGet_returnsEmptyResponse() {
        var books = getBookList(3);
        Mockito.when(bookRepository.findAll())
                .thenReturn(books);
        var result = new GetAllBooksInteractor.Response(
                books.stream()
                        .map(BookMapper::toDto)
                                .toList());

        Assertions.assertThat(sut.get()).isEqualTo(result);
    }

    private List<Book> getBookList(int count) {
        return IntStream.range(1, count + 1)
                .mapToObj(id -> new Book(id, "title " + id, 2000 + id))
                .toList();
    }

}