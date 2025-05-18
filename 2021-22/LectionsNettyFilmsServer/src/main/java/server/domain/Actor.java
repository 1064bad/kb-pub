package server.domain;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Actor {
    private long id;
    private String name;
}
