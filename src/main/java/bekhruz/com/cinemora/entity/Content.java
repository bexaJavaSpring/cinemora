package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "contents")
@Builder
public class Content extends Auditable {

    private String title;

    private String slug;

    private String originalTitle;

    private String description;

    private String posterUrl;

    private String backDropUrl;

    private int releaseYear;

    private float imdbRating;

    private float kinopoiskRating;

    private int durationMin;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isFeatured;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isTop;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long viewCount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "content_genres",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Builder.Default
    private List<Genres> genres = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "content_actors",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @Builder.Default
    private List<Actors> actors = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_type_id", nullable = false)
    private ContentType contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Season> seasons = new ArrayList<>();

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<VideoSource> videoSources = new ArrayList<>();

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
}
