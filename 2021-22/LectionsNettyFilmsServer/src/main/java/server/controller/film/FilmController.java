package server.controller.film;

import server.controller.HttpController;
import server.controller.Model;
import server.controller.Request;
import server.controller.UrlPath;
import server.domain.Film;
import server.db.FilmServiceFactory;
import server.net.exception.NetException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@HttpController
public class FilmController {
    @UrlPath("/film/search")
    public Model find(Request request) {
        var model = new Model("film_search");
        var filmService = FilmServiceFactory.getInstance();

        model.getVariables().put("test", "some cool variable");

        var offset = request.getSingleLongOption("offset", 0L);
        var limit = request.getSingleLongOption("limit", 20L);
        var filmCount = filmService.getFilmCount();

        var pages = new ArrayList<Page>();
        for (long pageOffset = 0, pageNum = 1; pageOffset < filmCount; pageOffset += limit, pageNum++) {
            pages.add(new Page(pageNum, pageOffset, limit));
        }
        model.getVariables().put("pages", pages);

        model.getVariables().put("films",
                filmService.findFlatPageAllOrderByTitle(offset, limit));
        return model;
    }

    @UrlPath("/film/edit/(?<id>\\d+)")
    public Model edit(Request request) {
        var id = getEditId(request);

        var optFilm = FilmServiceFactory.getInstance().findById(id);
        if (optFilm.isEmpty())
            throw new NetException("no such id: " + id);

        var film = optFilm.get();

        var param = request.getOptions();
        if (param.containsKey("edit")) {
            var newTitle = param.get("title").get(0);
            var newDuration = Short.parseShort(param.get("duration").get(0));
            film.setTitle(newTitle);
            film.setDuration(newDuration);
            FilmServiceFactory.getInstance().save(film);
        }

        var model = new Model();
        model.getVariables().put("film", film);
        model.setTemplate("film_edit");
        return model;
    }

    private long getEditId(Request request) {
        var matcher = Pattern.compile("/film/edit/(?<id>\\d+)", Pattern.CASE_INSENSITIVE)
                .matcher(request.getPath());
        if (!matcher.matches())
            throw new NetException("pattern doesn't matches");
        return Long.parseLong(matcher.group("id"));
    }
}
