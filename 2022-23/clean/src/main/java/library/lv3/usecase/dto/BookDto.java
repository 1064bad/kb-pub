package library.lv3.usecase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private long id;
    private String title;
    private int year;

    public static BookDto newBook(String title, int year) {
        return new BookDto(0, title, year);
    }
}
