package library.lv3.usecase;

import library.lv2.spi.repo.BookRepository;
import library.lv2.spi.repo.RepositoryAppException;
import library.lv2.spi.service.EmailService;
import library.lv3.usecase.dto.BookDto;
import library.lv3.usecase.dto.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
    void givenCorrectBook_whenAdd_thenSaveAndEmail() {
        var dto = new BookDto("t1", 1234);
        var book = BookMapper.fromDtoToNewBook(dto);
        var insertedBook = book.toBuilder().id(1).build();
        when(bookRepository.save(book, null)).thenReturn(insertedBook);

        sut.add(dto, null);

        verify(bookRepository).save(book, null);
        verify(emailService).send(any());
    }

    @Test
    void givenIncorrectBook_whenAdd_thenThrowsAndNoEmail() {
        var dto = new BookDto("t1", 1234);
        var book = BookMapper.fromDtoToNewBook(dto);
        when(bookRepository.save(book, null)).thenThrow(new RepositoryAppException("test"));

        assertThatExceptionOfType(RepositoryAppException.class)
                .isThrownBy(() -> sut.add(dto, null));

        verify(emailService, never()).send(any());
    }
}