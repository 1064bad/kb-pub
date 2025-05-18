package library.lv4.controller.console;

import library.lv3.usecase.GetAllAuthorsInteractor;
import library.lv3.usecase.dto.AuthorDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorController {
    private final IO io;
    private final GetAllAuthorsInteractor getAllAuthorsInteractor;

    public void showAllAuthors() {
        getAllAuthorsInteractor.get().stream()
                .map(this::author2String)
                .forEach(io::println);
    }

    private String author2String(AuthorDto author) {
        return "(%d) %s".formatted(author.getId(), author.getName());
    }
}
