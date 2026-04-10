package bekhruz.com.cinemora.dto.content;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ContentRequest {
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
    private Integer durationMin;

    private String  status;        // ACTIVE | INACTIVE | SOON
    private Boolean isFeatured;
    private Boolean isTop;

    private UUID imdbId;
    private UUID kinopoiskId;

    // Foreign key ID'lari
    private UUID       contentTypeId;
    private UUID       countryId;
    private List<UUID> genreIds;
    private List<UUID> actorIds;
}
