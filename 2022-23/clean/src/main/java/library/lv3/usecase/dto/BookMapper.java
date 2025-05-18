package library.lv3.usecase.dto;

import library.lv1.entity.Book;

public class BookMapper {
    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getYear());
    }

    public static Book fromDtoToNewBook(BookDto dto) {
        return new Book(0, dto.getTitle(), dto.getYear());
    }
}
