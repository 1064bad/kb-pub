package kb.tmp2.orm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.Set;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "book_with_authors_awards",
        attributeNodes = {
                @NamedAttributeNode("authors"),
                @NamedAttributeNode(
                        value = "awards",
                        subgraph = "award_with_commission"
                )
        },
        subgraphs = @NamedSubgraph(
                name = "award_with_commission",
                attributeNodes = @NamedAttributeNode("commission"))
)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String year;

    @ManyToMany
    @BatchSize(size = 15)
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Author> authors;

    @OneToMany(mappedBy = "book")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Award> awards;
}
