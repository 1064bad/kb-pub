package kb.tmp2.orm.controller.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookCreationModel {
    private long id;
    private String title;
    private String year;
    private List<Integer> authors;
}
