package library.lv1.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class Book {
    private final long id;
    private final String title;
    private final int year;
    private final List<Author> authors;

    public Book(long id, String title, int year) {
        this(id, title, year, null);
    }

    public Book(long id, String title, int year, List<Author> authors) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be non negative integer");
        }
        this.id = id;
        this.title = title;
        this.year = year;
        this.authors = authors;
    }

    public static Book flat(long id, String title, int year) {
        return new Book(id, title, year, null);
    }

    public static Book full(long id, String title, int year, List<Author> authors) {
        return new Book(id, title, year, authors);
    }
}
