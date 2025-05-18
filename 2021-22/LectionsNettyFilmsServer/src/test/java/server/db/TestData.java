package server.db;

import server.domain.Actor;
import server.domain.Award;
import server.domain.Film;

import java.util.List;

public class TestData {
    long nextFilmId = 1;
    Film flatFilm1 = new Film(nextFilmId++, "film a", (short) 120);
    Film flatFilm2 = new Film(nextFilmId++, "film b", (short) 140);
    Film flatFilm3 = new Film(nextFilmId++, "film c", (short) 160);
    Film flatFilmQwerty = new Film(nextFilmId++, "qwerty", (short) 180);

    List<Film> flatFilms = List.of(flatFilm1, flatFilm2, flatFilm3, flatFilmQwerty);


    long nextActorId = 1;
    Actor flatActor1 = new Actor(nextActorId++, "actor 1");
    Actor flatActor2 = new Actor(nextActorId++, "actor 2");
    Actor flatActor3 = new Actor(nextActorId++, "actor 3");
    Actor flatActor4 = new Actor(nextActorId++, "actor 4");
    Actor flatActor5 = new Actor(nextActorId++, "actor 5");

    List<Actor> flatActors = List.of(flatActor1, flatActor2, flatActor3, flatActor4, flatActor5);


    long nextAwardId = 1;
    Award flatAward1 = new Award(nextAwardId++, "award 1");
    Award flatAward2 = new Award(nextAwardId++, "award 2");
    Award flatAward3 = new Award(nextAwardId++, "award 3");

    List<Award> flatAwards = List.of(flatAward1, flatAward2, flatAward3);


    Film film1 = new Film(flatFilm1.getId(), flatFilm1.getTitle(), flatFilm1.getDuration());
    {
        film1.setActors(List.of(flatActor1, flatActor2, flatActor4));
        film1.setAwards(List.of(flatAward1, flatAward2, flatAward3));
    }
    Film film2 = new Film(flatFilm2.getId(), flatFilm2.getTitle(), flatFilm2.getDuration());
    {
        film2.setActors(List.of(flatActor2));
        film2.setAwards(List.of());
    }
    Film film3 = new Film(flatFilm3.getId(), flatFilm3.getTitle(), flatFilm3.getDuration());
    {
        film3.setActors(List.of(flatActor1, flatActor2, flatActor3, flatActor4));
        film3.setAwards(List.of(flatAward1, flatAward3));
    }
    Film filmQwerty = new Film(flatFilmQwerty.getId(), flatFilmQwerty.getTitle(), flatFilmQwerty.getDuration());
    {
        filmQwerty.setActors(List.of());
        filmQwerty.setAwards(List.of());
    }
    Film newFilm = new Film(0, "film for insert", (short) 999);
    {
        newFilm.setActors(List.of(flatActor1, flatActor4));
        newFilm.setAwards(List.of(flatAward1, flatAward3));
    }
}
