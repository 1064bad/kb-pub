package library.lv3.usecase;

import library.lv0.crosscutting.Settings;
import library.lv0.crosscutting.di.Dependency;
import library.lv2.spi.repo.BookRepository;
import library.lv2.spi.repo.RepositoryAppException;
import library.lv2.spi.service.Email;
import library.lv2.spi.service.EmailService;
import library.lv3.usecase.dto.BookDto;
import library.lv3.usecase.dto.BookMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Dependency
@RequiredArgsConstructor
public class AddNewBookInteractor {
    private final BookRepository bookRepository;
    private final EmailService emailService;

    public void add(BookDto book, List<Long> authorIds) {
        try {
            bookRepository.beginTransaction();
            var newBook = bookRepository.save(BookMapper.fromDtoToNewBook(book), authorIds);
            bookRepository.commitTransaction();
            emailService.send(new Email(
                    Settings.EMAIL_ADDRESS, "new Book added: " + newBook));
        } catch (Throwable t) {
            try {
                bookRepository.rollbackTransaction();
            } catch (RepositoryAppException e) {
                /* log */
            }
            throw t;
        }
    }
}
