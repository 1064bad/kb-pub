package library.lv4.controller.console;

import library.AppException;
import library.lv0.crosscutting.di.Dependency;
import library.lv3.usecase.AddNewBookInteractor;
import library.lv3.usecase.GetAllBooksInteractor;
import library.lv3.usecase.GetAllBooksWithAuthorsInteractor;
import library.lv3.usecase.dto.AuthorDto;
import library.lv3.usecase.dto.BookDto;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Dependency
@RequiredArgsConstructor
public class BookController {
    private final IO io;
    private final GetAllBooksInteractor getAllBooksInteractor;
    private final GetAllBooksWithAuthorsInteractor getAllBooksWithAuthorsInteractor;
    private final AddNewBookInteractor addNewBookInteractor;

    public void showAllBooks() {
        var response = getAllBooksInteractor.get();
        if (response.getBooks().isEmpty()) {
            io.println("no books found");
        }
        else {
            io.println("books in library:");
            response.getBooks().forEach(b -> io.println(book2String(b)));
        }
    }

    public void showAllBooksWithAuthors() {
        var response = getAllBooksWithAuthorsInteractor.get();
        if (response.getBooks().isEmpty()) {
            io.println("no books found");
        }
        else {
            io.println("books in library:");
            response.getBooks().forEach(b -> io.println(book2String(b)));
        }
    }

    public void addNewBook() {
        try {
            io.print("Title: ");
            var title = io.read();
            io.print("Year: ");
            var year = io.readInt();
            io.print("List of author ids (sep by comma): ");
            var authorIds = parseIdsFromString(io.read());

            addNewBookInteractor.add(BookDto.newBook(title, year), authorIds);
            io.println("all done!");
        }
        catch (AppException e) {
            io.println("error: " + e.getMessage());
        }
    }

    private List<Long> parseIdsFromString(String text) {
        if (text == null) {
            throw new NullPointerException("text is null");
        }

        try {
            return Arrays.stream(text.split(","))
                    .map(String::strip)
                    .map(Long::parseLong)
                    .toList();
        }
        catch (NumberFormatException e) {
            throw new AppException("bad list of author ids format, at least one author must be");
        }
    }

    private String book2String(BookDto b) {
        var book =  "(%d)'%s', year %s".formatted(b.getId(), b.getTitle(), b.getYear());
        if (b.getAuthors() != null) {
            book = book + b.getAuthors().stream()
                    .map(AuthorDto::getName)
                    .collect(Collectors.joining(", ", " (", ")"));
        }
        return book;
    }
}
