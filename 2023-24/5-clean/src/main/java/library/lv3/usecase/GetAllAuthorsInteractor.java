package library.lv3.usecase;

import library.lv2.spi.repo.AuthorRepository;
import library.lv3.usecase.dto.AuthorDto;
import library.lv3.usecase.dto.AuthorMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetAllAuthorsInteractor {
    private final AuthorRepository authorRepository;

    public List<AuthorDto> get() {
        return authorRepository.findAll().stream()
                .map(AuthorMapper::toDto)
                .toList();
    }
}
