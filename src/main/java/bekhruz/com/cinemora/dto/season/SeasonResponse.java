package bekhruz.com.cinemora.dto.season;

import lombok.Data;

import java.util.UUID;

@Data
public class SeasonResponse {
    private UUID id;
    private UUID    contentId;
    private Integer seasonNumber;
    private String  title;
    private Integer releaseYear;
    private String  posterUrl;
    private Integer episodeCount;
}
