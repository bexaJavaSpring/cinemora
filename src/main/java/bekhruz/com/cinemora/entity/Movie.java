package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    @Column(name = "release_year")
    private int releaseYear;

    private String country;

    @Enumerated(EnumType.STRING)
    private MovieCategory movieCategory;

    private Long durationMin;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Enumerated(EnumType.STRING)
    private AudioTrek audioTrek;

    @Enumerated(EnumType.STRING)
    private Subtitr subtitr;

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genres> genres = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private MovieType type;
}
