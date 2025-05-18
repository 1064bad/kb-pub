package library.lv3.usecase;

import library.lv2.spi.repo.BookRepository;
import library.lv3.usecase.dto.BookDto;
import library.lv3.usecase.dto.BookMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetAllBooksInteractor {
    private final BookRepository bookRepository;
    public Response get() {
        var books = bookRepository.findAllFlat().stream()
                .map(BookMapper::toDto)
                .toList();
        return new Response(books);
    }

    @Data
    public static class Response {
        private final List<BookDto> books;
    }
}
