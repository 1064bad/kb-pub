package library.lv2.spi.repo;

import library.lv1.entity.Author;

import java.util.List;

public interface AuthorRepository extends BaseRepository {
    Author findById(long id);
    List<Author> findAll();
    List<Author> findByBookId();
}
