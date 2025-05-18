package server.db;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.domain.Film;

import java.sql.Connection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class FilmServiceImplTest {
    private FilmService filmService;
    private Connection mainConn;
    private TestData data;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        var dataSource = new TestDataSource();
        mainConn = dataSource.getConnection();
        fillDb(mainConn);
        filmService = new FilmServiceImpl(dataSource, new FilmRepository());
        data = new TestData();
    }

    @SneakyThrows
    private void fillDb(Connection connection) {
        var bytes = getClass().getClassLoader().getResourceAsStream("test_db.sql").readAllBytes();
        var sql = new String(bytes);
        for (var stmt : sql.split(";"))
            connection.createStatement().execute(stmt);
    }

    @AfterEach
    @SneakyThrows
    void shutDown() {
        mainConn.close();
    }

    @Test
    void whenFindFlatAll_thenSuccess() {
        var result = filmService.findFlatAll();

        assertThat(result).containsExactlyInAnyOrderElementsOf(data.flatFilms);
    }

    @Test
    void givenExistsId_whenFindById_thenSuccess() {
        var result = filmService.findFlatById(data.flatFilm2.getId());
        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo(data.flatFilm2);
    }

    @Test
    void givenUnknownId_whenFindById_thenEmpty() {
        var result = filmService.findFlatById(Integer.MAX_VALUE);
        assertThat(result).isEmpty();
    }

    @Test
    void givenNewFilm_whenSave_thenSuccessfulInsert() {
        var newFilm = data.newFilm;

        var persistFilm = filmService.save(newFilm);

        var filmFromDb = filmService.findById(persistFilm.getId());
        assertThat(filmFromDb)
                .isPresent()
                .get()
                .isEqualTo(newFilm)
                .extracting("id")
                .isEqualTo(data.nextFilmId);
    }

    @Test
    void givenExistFilm_whenSave_thenSuccessfulUpdate() {
        var updating = data.flatFilm3;
        updating.setTitle("updated film 3");
        updating.setDuration((short) (updating.getDuration() + 100));
        updating.setActors(List.of(data.flatActor2, data.flatActor5));
        updating.setAwards(List.of(data.flatAward2, data.flatAward3));

        var updatingReturned = filmService.save(updating);

        var filmFromDb = filmService.findById(updating.getId());
        assertThat(filmFromDb)
                .isPresent()
                .get()
                .isEqualTo(updating)
                .isEqualTo(updatingReturned);
    }

    @Test
    void givenBadPattern_whenFindAllLikeTitle_thenEmptyResult() {
        var pattern = "no such pattern";
        var result = filmService.findFlatAllLikeTitle(pattern);
        assertThat(result).isEmpty();
    }

    @Test
    void givenGoodPattern_whenFindAllLikeTitle_thenSuccess() {
        var pattern = "FilM";
        var result = filmService.findFlatAllLikeTitle(pattern);
        assertThat(result).containsExactlyInAnyOrder(data.flatFilm1, data.flatFilm2, data.flatFilm3);
    }

    @Test
    void givenBlankPattern_whenFindAllLikeTitle_thenReturnAll() {
        var pattern = " \t ";
        var result = filmService.findFlatAllLikeTitle(pattern);
        assertThat(result).containsExactlyInAnyOrderElementsOf(data.flatFilms);
    }

    @Test
    void givenEmptyPattern_whenFindAllLikeTitle_thenReturnAll() {
        var pattern = "";
        var result = filmService.findFlatAllLikeTitle(pattern);
        assertThat(result).containsExactlyInAnyOrderElementsOf(data.flatFilms);
    }

    @Test
    void givenCorrectBounds_findFlatPageAllOrderByTitle_thenSuccess() {
        var offset = 1;
        var limit = 2;

        var result = filmService.findFlatPageAllOrderByTitle(offset, limit);

        assertThat(result)
                .containsExactlyElementsOf(List.of(data.flatFilm2, data.flatFilm3));
    }

    @Test
    void givenAllElementBounds_findFlatPageAllOrderByTitle_thenSuccess() {
        var offset = 0;
        var limit = data.nextFilmId;

        var result = filmService.findFlatPageAllOrderByTitle(offset, limit);

        assertThat(result)
                .containsExactlyElementsOf(data.flatFilms);
    }

    @Test
    void givenOutLimitBounds_findFlatPageAllOrderByTitle_thenSuccess() {
        var offset = 0;
        var limit = data.nextFilmId + 100;

        var result = filmService.findFlatPageAllOrderByTitle(offset, limit);

        assertThat(result)
                .containsExactlyElementsOf(data.flatFilms);
    }

    @Test
    void givenOutBounds_findFlatPageAllOrderByTitle_thenEmpty() {
        var offset = data.nextFilmId;
        var limit = 2;

        var result = filmService.findFlatPageAllOrderByTitle(offset, limit);

        assertThat(result).isEmpty();
    }

    @Test
    void givenCorrectBounds_findPageAllOrderByTitle_thenSuccess() {
        var offset = 1;
        var limit = 2;

        var result = filmService.findPageAllOrderByTitle(offset, limit);

        assertThat(result)
                .containsExactlyElementsOf(List.of(data.film2, data.film3));
    }
}