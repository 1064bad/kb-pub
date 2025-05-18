package library.lv2.spi.repo;

import library.lv1.entity.Book;

import java.util.List;

public interface BookRepository extends BaseRepository {
    List<Book> findAll();

    /**
     *
     * @param book
     * @return !!!!!!!!
     */
    Book save(Book book, List<Long> authorIds);
}
