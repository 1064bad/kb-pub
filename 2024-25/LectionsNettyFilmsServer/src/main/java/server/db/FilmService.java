package server.db;

import server.domain.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmService {
    Collection<Film> findFlatAll();

    Optional<Film> findById(long id);

    Optional<Film> findFlatById(long id);

    Film save(Film film);

    Collection<Film> findFlatAllLikeTitle(String titlePattern);

    Collection<Film> findFlatPageAllOrderByTitle(long offset, long limit);

    Collection<Film> findPageAllOrderByTitle(long offset, long limit);

    long getFilmCount();
}
