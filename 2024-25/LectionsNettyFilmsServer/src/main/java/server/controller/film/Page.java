package server.controller.film;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Page {
    private final long pageNum;
    private final long offset;
    private final long limit;
}
