package server.db;

public class FilmServiceFactory {
    private static FilmService service = null;

    public static synchronized FilmService getInstance() {
        if (service == null) {
            service = new FilmServiceImpl(
                    new PGDataSource("postgres", "secret"), new FilmRepository());
        }
        return service;
    }
}
