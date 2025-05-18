package server.db;

import lombok.SneakyThrows;
import org.eclipse.collections.api.factory.Bags;
import server.domain.Actor;
import server.domain.Award;
import server.domain.Film;

import java.sql.*;
import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class FilmRepository {

    @SneakyThrows
    public long getFilmCount(Connection conn) {
        try (var resultSet = conn.createStatement()
                .executeQuery("select count(*) from films")) {
            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    @SneakyThrows
    public Collection<Film> findFlatAll(Connection conn) {
        try (var resultSet = conn.createStatement()
                .executeQuery("select id, title, duration from films")) {
            var result = new ArrayList<Film>();
            while (resultSet.next()) {
                result.add(extractFlatFilm(resultSet));
            }
            return result;
        }
    }

    @SneakyThrows
    public Collection<Film> findFlatAllLikeTitle(Connection conn, String titlePattern) {
        try (var ps = conn.prepareStatement(
                "select id, title, duration from films where lower(title) like ?")) {
            ps.setString(1, "%" + titlePattern.strip().toLowerCase() + "%");
            var resultSet = ps.executeQuery();
            return extractFlatFilmCollection(resultSet);
        }
    }

    @SneakyThrows
    public Collection<Film> findFlatPageAllOrderByTitle(Connection conn, long offset, long limit) {
        try (var ps = conn.prepareStatement(
                "select id, title, duration from films order by lower(title) limit ? offset ?")) {
            ps.setLong(1, limit);
            ps.setLong(2, offset);
            var rs = ps.executeQuery();
            return extractFlatFilmCollection(rs);
        }
    }

    @SneakyThrows
    public Collection<Film> findPageAllOrderByTitle(Connection conn, long offset, long limit) {
        var films = findFlatPageAllOrderByTitle(conn, offset, limit);
        fillFlatActors(conn, films);
        fillFlatAwards(conn, films);
        return films;
    }

    @SneakyThrows void fillFlatActors(Connection conn, Collection<Film> films) {
        films.forEach(f -> f.setActors(new ArrayList<>()));
        var ids = films.stream().map(f -> Long.toString(f.getId())).collect(joining(","));
        var filmMap = films.stream().collect(toMap(Film::getId, x -> x));
        var actorMap = new HashMap<Long, Actor>();
        try (var ps = conn.prepareStatement(
                "select distinct id_film, a.id a_id, a.name a_name" +
                        " from actors_films af join actors a on a.id = af.id_actor" +
                        " where id_film in (" + ids + ")")) {
            var rs = ps.executeQuery();
            while (rs.next()) {
                var filmId = rs.getLong("id_film");
                var actorId = rs.getLong("a_id");
                var actorName = rs.getString("a_name");
                var actor = actorMap.computeIfAbsent(actorId, x -> new Actor(actorId, actorName));
                filmMap.get(filmId).getActors().add(actor);
            }
        }
    }

    @SneakyThrows void fillFlatAwards(Connection conn, Collection<Film> films) {
        films.forEach(f -> f.setAwards(new ArrayList<>()));
        var ids = films.stream().map(f -> Long.toString(f.getId())).collect(joining(","));
        var filmMap = films.stream().collect(toMap(Film::getId, x -> x));
        var awardMap = new HashMap<Long, Award>();
        try (var ps = conn.prepareStatement(
                "select distinct id_film, a.id a_id, a.title a_title" +
                        " from films_awards fa join awards a on a.id = fa.id_award" +
                        " where id_film in (" + ids + ")")) {
            var rs = ps.executeQuery();
            while (rs.next()) {
                var filmId = rs.getLong("id_film");
                var awardId = rs.getLong("a_id");
                var awardTitle = rs.getString("a_title");
                var award = awardMap.computeIfAbsent(awardId, x -> new Award(awardId, awardTitle));
                filmMap.get(filmId).getAwards().add(award);
            }
        }
    }

    @SneakyThrows
    public Optional<Film> findFlatById(Connection conn, long id) {
        try (var ps = conn.prepareStatement(
                "select id, title, duration from films where id = ?")) {
            ps.setLong(1, id);
            var rs = ps.executeQuery();
            return rs.next() ? Optional.of(extractFlatFilm(rs)) : Optional.empty();
        }
    }

    @SneakyThrows
    private Collection<Film> extractFlatFilmCollection(ResultSet rs) {
        var result = new ArrayList<Film>();
        while (rs.next()) {
            result.add(extractFlatFilm(rs));
        }
        return result;
    }

    @SneakyThrows
    private Film extractFlatFilm(ResultSet rs) {
        return new Film(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getShort("duration"));
    }

    @SneakyThrows
    public Optional<Film> findById(Connection conn, long id) {
        var opt = findFlatById(conn, id);
        opt.ifPresent(film -> {
            var actors = selectActors(conn, id);
            var awards = selectAwards(conn, id);
            film.setActors(actors);
            film.setAwards(awards);
        });
        return opt;
    }

    @SneakyThrows
    private Collection<Actor> selectActors(Connection conn, long filmId) {
        var result = new ArrayList<Actor>();
        try (var ps = conn.prepareStatement("select distinct id, name " +
                "from actors a join actors_films af on a.id = af.id_actor where id_film = ?")) {
            ps.setLong(1, filmId);
            var rs = ps.executeQuery();
            while (rs.next())
                result.add(extractFlatActor(rs));
        }
        return result;
    }

    @SneakyThrows
    private Actor extractFlatActor(ResultSet rs) {
        return new Actor(rs.getLong("id"), rs.getString("name"));
    }

    @SneakyThrows
    private Collection<Award> selectAwards(Connection conn, long filmId) {
        var result = new ArrayList<Award>();
        try (var ps = conn.prepareStatement("select distinct id, title " +
                "from awards a join films_awards fa on a.id = fa.id_award where fa.id_film = ?")) {
            ps.setLong(1, filmId);
            var rs = ps.executeQuery();
            while (rs.next())
                result.add(extractFlatAward(rs));
        }
        return result;
    }

    @SneakyThrows
    private Award extractFlatAward(ResultSet rs) {
        return new Award(rs.getLong("id"), rs.getString("title"));
    }

    @SneakyThrows
    public Film save(Connection conn, Film film) {
        if (film.getId() == 0) {
            return insert(conn, film);
        } else {
            return update(conn, film);
        }
    }

    @SneakyThrows
    private Film update(Connection conn, Film film) {
        var filmOld = findById(conn, film.getId())
                .orElseThrow(() -> new SQLException("no film with id " + film.getId()));
        updateFlatFilm(conn, film);
        updateActors(conn, film.getId(), filmOld.getActors(), film.getActors());
        updateAwards(conn, film.getId(), filmOld.getAwards(), film.getAwards());
        return film;
    }

    @SneakyThrows
    private void updateActors(Connection conn, long filmId,
                              Collection<Actor> oldActors, Collection<Actor> newActors) {
        var actorsToDelete = new HashSet<>(oldActors);
        actorsToDelete.removeAll(newActors);
        deleteActors(conn, filmId, actorsToDelete);

        var actorsToInsert = new HashSet<>(newActors);
        actorsToInsert.removeAll(oldActors);
        insertActors(conn, filmId, actorsToInsert);
    }

    @SneakyThrows
    private void deleteActors(Connection conn, long filmId, Collection<Actor> actors) {
        if (!actors.isEmpty()) {
            var qMarks = String.join(",", Bags.immutable.withOccurrences("?", actors.size()));
            var ps = conn.prepareStatement("delete from actors_films " +
                    "where id_film = ? and id_actor in (" + qMarks + ")");
            int cnt = 1;
            ps.setLong(cnt++, filmId);
            for (var actor : actors)
                ps.setLong(cnt++, actor.getId());
            ps.executeUpdate();
        }
    }

    @SneakyThrows
    private void updateAwards(Connection conn, long filmId,
                              Collection<Award> oldAwards, Collection<Award> newAwards) {
        var awardsToDelete = new HashSet<>(oldAwards);
        awardsToDelete.removeAll(newAwards);
        deleteAwards(conn, filmId, awardsToDelete);

        var awardsToInsert = new HashSet<>(newAwards);
        awardsToInsert.removeAll(oldAwards);
        insertAwards(conn, filmId, awardsToInsert);
    }

    @SneakyThrows
    private void deleteAwards(Connection conn, long filmId, Collection<Award> awards) {
        if (!awards.isEmpty()) {
            var qMarks = awards.stream().map(x -> "?").collect(joining(","));
            try (var ps = conn.prepareStatement("delete from films_awards " +
                    "where id_film = ? and id_award in (" + qMarks + ")")) {
                int cnt = 1;
                ps.setLong(cnt++, filmId);
                for (var award : awards)
                    ps.setLong(cnt++, award.getId());
                ps.executeUpdate();
            }
        }
    }

    @SneakyThrows
    private void updateFlatFilm(Connection conn, Film film) {
        try (var ps = conn.prepareStatement("update films set title = ?, duration = ?" +
                " where id = ?")) {
            ps.setString(1, film.getTitle());
            ps.setShort(2, film.getDuration());
            ps.setLong(3, film.getId());
            ps.executeUpdate();
        }
    }

    private Film insert(Connection conn, Film film) throws SQLException {
        var filmId = insertFlatFilm(conn, film);
        insertActors(conn, filmId, film.getActors());
        insertAwards(conn, filmId, film.getAwards());
        film.setId(filmId);
        return film;
    }

    @SneakyThrows
    private long insertFlatFilm(Connection conn, Film film) {
        try (var ps = conn.prepareStatement(
                "insert into films (title, duration) values (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            executeFlatFilmInsertion(ps, film);
            return obtainGeneratedId(ps);
        }
    }

    @SneakyThrows
    private void executeFlatFilmInsertion(PreparedStatement ps, Film film) {
        ps.setString(1, film.getTitle());
        ps.setShort(2, film.getDuration());
        ps.executeUpdate();
    }

    @SneakyThrows
    long obtainGeneratedId(PreparedStatement ps) {
        var keyRS = ps.getGeneratedKeys();
        if (!keyRS.next()) {
            throw new SQLException("cannot obtain auto generated key from '" + ps + "'");
        }
        return keyRS.getLong(1);
    }

    private void insertActors(Connection conn, long filmId, Iterable<Actor> actors) throws SQLException {
        try (var ps = conn.prepareStatement(
                "insert into actors_films(id_film, id_actor) values (?, ?)")) {
            actors.forEach(actor -> executeActorInsertion(ps, filmId, actor.getId()));
        }
    }

    @SneakyThrows
    private void executeActorInsertion(PreparedStatement ps, long filmId, long actorId) {
        ps.setLong(1, filmId);
        ps.setLong(2, actorId);
        ps.executeUpdate();
    }

    private void insertAwards(Connection conn, long filmId, Iterable<Award> awards) throws SQLException {
        try (var ps = conn.prepareStatement(
                "insert into films_awards(id_film, id_award) values (?, ?)")) {
            awards.forEach(award -> executeAwardInsertion(ps, filmId, award.getId()));
        }
    }

    @SneakyThrows
    private void executeAwardInsertion(PreparedStatement ps, long filmId, long awardId) {
        ps.setLong(1, filmId);
        ps.setLong(2, awardId);
        ps.executeUpdate();
    }
}
