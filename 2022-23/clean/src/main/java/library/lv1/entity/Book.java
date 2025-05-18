package library.lv1.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Book {
    private final long id;
    private final String title;
    private final int year;

    public Book(long id, String title, int year) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be non negative integer");
        }
        this.id = id;
        this.title = title;
        this.year = year;
    }
}
