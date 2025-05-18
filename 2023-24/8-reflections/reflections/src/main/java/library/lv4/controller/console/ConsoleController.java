package library.lv4.controller.console;

import library.lv0.crosscutting.di.Dependency;
import library.lv4.controller.Controller;
import lombok.RequiredArgsConstructor;

@Dependency
@RequiredArgsConstructor
public class ConsoleController implements Controller {
    private final IO io;
    private final BookController bookController;
    private final AuthorController authorController;

    public void start() {
        var command = "";
        do {
            try {
                io.print(">>> ");
                command = io.read().strip().toLowerCase();
                switch (command) {
                    case "book ls" -> bookController.showAllBooks();
                    case "book lsa" -> bookController.showAllBooksWithAuthors();
                    case "book add" -> bookController.addNewBook();
                    case "author ls" -> authorController.showAllAuthors();
                    case "exit" -> io.println("bye!");
                    default -> io.println("unrecognized command: " + command);
                }
            }
            catch (Exception e ) {
                io.println("error: " + e.getMessage());
            }
        }
        while (! "exit".equals(command));
    }
}
