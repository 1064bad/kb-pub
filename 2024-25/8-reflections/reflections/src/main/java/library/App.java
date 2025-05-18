package library;

import library.lv0.crosscutting.di.DependencyProcessor;
import library.lv3.usecase.AddNewBookInteractor;
import library.lv3.usecase.GetAllAuthorsInteractor;
import library.lv3.usecase.GetAllBooksInteractor;
import library.lv3.usecase.GetAllBooksWithAuthorsInteractor;
import library.lv4.controller.Controller;
import library.lv4.controller.console.AuthorController;
import library.lv4.controller.console.BookController;
import library.lv4.controller.console.ConsoleController;
import library.lv4.controller.mapper.Mapper;
import library.lv5.impl.infrastructure.ConsoleIO;
import library.lv5.impl.repo.pg.PgAuthorRepository;
import library.lv5.impl.repo.pg.PgBookRepository;
import library.lv5.impl.repo.pg.PgDataSource;
import library.lv5.impl.service.StubEmailService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.logging.log4j.LogManager;

@Slf4j
public class App {
    public static final Mapper mapper;
    public static final GetAllBooksInteractor GET_ALL_BOOKS_INTERACTOR;
    public static final GetAllBooksWithAuthorsInteractor GET_ALL_BOOKS_WITH_AUTHORS_INTERACTOR;
    public static final AddNewBookInteractor ADD_NEW_BOOK_INTERACTOR;
    public static final GetAllAuthorsInteractor GET_ALL_AUTHORS_INTERACTOR;
    public static final Controller CONTROLLER;

    static {
        LogManager.getLogger(App.class).info("init started...");

        mapper = new Mapper();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new AppException(e);
        }

        var dataSource = new PgDataSource();

//        var bookRepo = new MapBookRepository();
        var bookRepo = new PgBookRepository(dataSource);
        var authorRepo = new PgAuthorRepository(dataSource);

        var io = new ConsoleIO();

        var emailService = new StubEmailService(io);
//        var emailService = new YandexMailService();

        GET_ALL_BOOKS_INTERACTOR = new GetAllBooksInteractor(bookRepo);
        GET_ALL_BOOKS_WITH_AUTHORS_INTERACTOR = new GetAllBooksWithAuthorsInteractor(bookRepo);
        ADD_NEW_BOOK_INTERACTOR = new AddNewBookInteractor(
                bookRepo,
                emailService);
        GET_ALL_AUTHORS_INTERACTOR = new GetAllAuthorsInteractor(authorRepo);

        val bookController = new BookController(
                io,
                GET_ALL_BOOKS_INTERACTOR,
                GET_ALL_BOOKS_WITH_AUTHORS_INTERACTOR,
                ADD_NEW_BOOK_INTERACTOR);
        var authorController = new AuthorController(io, GET_ALL_AUTHORS_INTERACTOR);

        CONTROLLER = new ConsoleController(
                io, bookController, authorController);
//        GetAllBooksAction.Factory.init(getAllBooksUseCase);
//        var controller = new TelegramBotController();

        LogManager.getLogger(App.class).info("init done!");
    }

    public static void main(String[] args) {
        new DependencyProcessor("library")
                .getDependency(Controller.class)
                .start();
    }

}
