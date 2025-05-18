package library.lv3.usecase;

import library.AppException;
import library.lv1.entity.Book;
import library.lv2.spi.repo.BookRepository;
import library.lv2.spi.service.EmailService;
import library.lv3.usecase.dto.BookMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class AddNewBookInteractorTest {

    AddNewBookInteractor sut;
    BookRepository bookRepository;
    EmailService emailService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        emailService = mock(EmailService.class);
        sut = new AddNewBookInteractor(bookRepository, emailService);
    }

    @Test
    void givenCorrectBook_whenAdd_thenCallRepoAndSendEmail() {
        // arrange
        var book = new Book(0, "testTitle", 2020);
        var bookDto = BookMapper.toDto(book);
        var insertedBook = book.toBuilder().id(1).build();
        when(bookRepository.save(book, null)).thenReturn(insertedBook);

        // act
        sut.add(bookDto, null);

        // assert
        verify(bookRepository).save(book, null);
        verify(emailService).send(any());
    }

    @Test
    void givenIncorrectBook_whenAdd_thenCallRepoAndNotSendEmail() {
        // arrange
        var book = new Book(0, "testTitle", 2020);
        var bookDto = BookMapper.toDto(book);
        when(bookRepository.save(book, null)).thenThrow(new AppException("test"));

        // act
        Assertions.assertThatExceptionOfType(AppException.class)
                .isThrownBy(() -> sut.add(bookDto, null));

        // assert
        verify(bookRepository).save(book, null);
        verify(emailService, never()).send(any());
    }
}