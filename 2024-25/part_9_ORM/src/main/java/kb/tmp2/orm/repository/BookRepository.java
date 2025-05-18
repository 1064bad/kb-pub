package kb.tmp2.orm.repository;

import kb.tmp2.orm.config.Db;
import kb.tmp2.orm.config.di.Dependency;
import kb.tmp2.orm.controller.message.BookCreationModel;
import kb.tmp2.orm.domain.Author;
import kb.tmp2.orm.domain.Book;

import java.util.Set;
import java.util.stream.Collectors;

@Dependency
public class BookRepository {
    public Book findById(int id) {
        return Db.fromTransaction(session -> {
            var book = session.find(Book.class, id);
            if (book == null) {
                return null;
            }
            book.getAuthors().size();
            book.getAwards().size();
            return book;
        });
    }

    public void insert(BookCreationModel model) {
        Db.inTransaction(session -> {
            var authors = model.getAuthors().stream()
                    .map(id -> {
                        var a = session.find(Author.class, id);
                        if (a == null) {
                            throw new RepositoryException("author not found: " + id);
                        }
                        return a;
                    }).collect(Collectors.toSet());
            var book = new Book(
                    0, model.getTitle(), model.getYear(), authors, Set.of());
            session.persist(book);
        });
    }
}
