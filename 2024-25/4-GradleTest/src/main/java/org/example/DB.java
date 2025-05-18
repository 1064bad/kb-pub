package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/library",
                "user",
                "secret");
    }

    public List<Book> findAll() {
        var result = new ArrayList<Book>();
        try (var conn = getConnection()) {
            try (var stmt = conn.createStatement()) {
                try (var resultSet = stmt.executeQuery("select id, title, year from books")) {
                    while (resultSet.next()) {
                        result.add(new Book(
                                resultSet.getLong("id"),
                                resultSet.getString("title"),
                                resultSet.getInt("year")
                        ));
                    }
                    return result;
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public List<Book> findByTitlePart(String titlePart) {
        var result = new ArrayList<Book>();
        try (var conn = getConnection()) {
            try (var stmt = conn.prepareStatement("""
                        select id, title, year
                        from books
                        where lower(title) like ?
                        """)) {
                stmt.setString(1, "%" + titlePart.toLowerCase() + "%");
                var resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    result.add(new Book(
                            resultSet.getLong("id"),
                            resultSet.getString("title"),
                            resultSet.getInt("year")
                    ));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public Book save(Book book) {
        return book.getId() == 0 ? insert(book) : update(book);
    }

    public Book insert(Book book) {
        if (book.getId() != 0) {
            throw new DBException("id must be 0 to insert");
        }
        try (var conn = getConnection()) {
            var stmt = conn.prepareStatement("""
                    insert into books (title, year) values (?, ?)
                    """, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getYear());
            stmt.executeUpdate();
            var keys = stmt.getGeneratedKeys();
            keys.next();
            return book.toBuilder().id(keys.getLong(1)).build();
        }
        catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
