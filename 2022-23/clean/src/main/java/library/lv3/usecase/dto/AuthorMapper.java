package library.lv3.usecase.dto;

import library.lv1.entity.Author;

public class AuthorMapper {
    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }

    public static Author fromDto(AuthorDto dto) {
        return new Author(dto.getId(), dto.getName());
    }
}
