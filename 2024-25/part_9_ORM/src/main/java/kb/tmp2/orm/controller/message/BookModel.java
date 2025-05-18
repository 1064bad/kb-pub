package kb.tmp2.orm.controller.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookModel {
    private long id;
    private String title;
    private String year;
    private List<AuthorFlatModel> authors;
    private List<AwardFlatModel> awards;
}
