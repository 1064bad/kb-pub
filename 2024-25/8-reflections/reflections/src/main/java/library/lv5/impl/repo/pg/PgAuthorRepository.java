package library.lv5.impl.repo.pg;

import library.lv0.crosscutting.di.Dependency;
import library.lv1.entity.Author;
import library.lv2.spi.repo.AuthorRepository;
import library.lv2.spi.repo.RepositoryAppException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Dependency
public class PgAuthorRepository extends PgBaseRepository implements AuthorRepository {

    public PgAuthorRepository(PgDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Author findById(long id) {
        try {
            var conn = dataSource.getConnection();
            try (var stmt = conn.prepareStatement(
                    "select * from authors where id = " + id)) {
                var rs = stmt.executeQuery();
                if (!rs.next()) {
                    throw new RepositoryAppException("no author with id" + id);
                }
                return row2author(rs);
            }

        } catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public List<Author> findAll() {
        try {
            var conn = dataSource.getConnection();
            try (var stmt = conn.prepareStatement("select * from authors")) {
                var rs = stmt.executeQuery();
                var result = new ArrayList<Author>();
                while (rs.next()) {
                    result.add(row2author(rs));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public List<Author> findByBookId() {
        return null;
    }

    private Author row2author(ResultSet rs) throws SQLException {
        return new Author(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
