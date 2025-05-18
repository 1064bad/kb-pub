package kb.tmp2.orm.controller;

import com.linecorp.armeria.common.HttpRequest;
import kb.tmp2.orm.config.di.Dependency;
import kb.tmp2.orm.controller.message.*;
import kb.tmp2.orm.repository.BookRepository;
import lombok.RequiredArgsConstructor;

@Dependency
@RequiredArgsConstructor
public class BookHttpService extends BaseHttpService{
    private final BookRepository bookRepository;

    @Override
    protected BookModel get(HttpRequest req) throws Exception {
        var id = getIntQueryParam(req, "id");
        var book = bookRepository.findById(id);
        if (book == null) {
            throw new HttpServiceException("book not found: " + id, 404);
        }
        return new BookModel(
                book.getId(),
                book.getTitle(),
                book.getYear(),
                book.getAuthors().stream()
                        .map(a -> new AuthorFlatModel(a.getId(), a.getName())).toList(),
                book.getAwards().stream()
                        .map(a -> new AwardFlatModel(a.getId(), a.getTitle())).toList()
        );
    }

    @Override
    protected Object post(HttpRequest req) throws Exception {
        var newBook = getBody(req, BookCreationModel.class);
        bookRepository.insert(newBook);
        return new SuccessResponse();
    }
}
