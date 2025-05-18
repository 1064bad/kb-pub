package library.lv5.impl.repo.pg;

import library.lv1.entity.Book;
import library.lv2.spi.repo.BookRepository;
import library.lv2.spi.repo.RepositoryAppException;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PgBookRepository extends PgBaseRepository implements BookRepository {

    public PgBookRepository(PgDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Book> findAll() {
        try {
            var conn = dataSource.getConnection();
            try (var stmt = conn.prepareStatement("select * from books")) {
                var result = new ArrayList<Book>();
                try (var rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        result.add(new Book(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getInt("year")));
                    }
                }
                return Collections.unmodifiableList(result);
            }
        } catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public Book save(Book book, List<Long> updatedAuthorIds) {
        return book.getId() == 0 ? insert(book, updatedAuthorIds)
                : update(book, updatedAuthorIds);
    }

    private Book insert(Book book, List<Long> authorIds) {
        try {
            var conn = dataSource.getConnection();
            var id = insertBook(book, conn);
            updateAuthors(id, authorIds, conn);
            return book.toBuilder().id(id).build();
        } catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    private long insertBook(Book book, Connection connection) throws SQLException {
        try (var stmt = connection.prepareStatement("""
                    insert into books (id, title, year)
                    values (default, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getYear());
            ResultSet keyRs;
            stmt.executeUpdate();
            keyRs = stmt.getGeneratedKeys();
            if (!keyRs.next())
                throw new RepositoryAppException("no generated keys in query result!");
            return keyRs.getLong(1);
        }
    }

    private Book update(Book book, List<Long> updatedAuthorIds) {
        try {
            var conn = dataSource.getConnection();
            updateBook(book, conn);
            updateAuthors(book.getId(), updatedAuthorIds, conn);
            return book;
        }
        catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    private void updateBook(Book book, Connection connection) throws SQLException {
        try (var stmt = connection.prepareStatement("""
                    update books set
                        title = ?,
                        year = ?
                    where id = ?
                    """)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getYear());
            stmt.setLong(3, book.getId());
            stmt.executeUpdate();
        }
    }

    private void updateAuthors(long bookId, List<Long> authorIds, Connection conn) throws SQLException {
        if (authorIds == null)
            return;

        try (var stmt = conn.prepareStatement("""
                delete from books_authors where book_id = %d
                """.formatted(bookId))) {
            stmt.executeUpdate();
        }
        if (!authorIds.isEmpty()) {
            var insertSql = "insert into books_authors(book_id, author_id) values " +
                    authorIds.stream()
                            .map(authorId -> "(%d, %d)".formatted(bookId, authorId))
                            .collect(Collectors.joining(","));
            try (var stmt = conn.prepareStatement(insertSql)) {
                stmt.executeUpdate();
            }
        }
    }
}
