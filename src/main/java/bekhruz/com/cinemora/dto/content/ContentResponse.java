package bekhruz.com.cinemora.dto.content;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ContentResponse {
    private UUID id;
    private String  title;
    private String  originalTitle;
    private String  slug;
    private String  description;

    private String  posterUrl;
    private String  backdropUrl;
    private String  trailerUrl;

    private Integer releaseYear;
    private Float   imdbRating;
    private Float   kinopoiskRating;
    private Float   siteRating;
    private Integer durationMin;

    private String  status;
    private Boolean isFeatured;
    private Boolean isTop;
    private Long    viewCount;

    private String  contentTypeName;   // "Kino"
    private String  contentTypeSlug;   // "movie"
    private String  countryName;       // "Amerika"

    private List<GenreInfo> genres;
    private List<ActorInfo>  actors;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    public static class GenreInfo {
        private UUID id;
        private String  name;
        private String  slug;
    }

    @Data
    public static class ActorInfo {
        private UUID id;
        private String  fullName;
        private String  photoUrl;
    }
}
