package bekhruz.com.cinemora.dto.episode;

import lombok.Data;

import java.util.UUID;

@Data
public class EpisodeResponse {
    private UUID id;
    private UUID contentId;
    private UUID seasonId;
    private String seasonNumber;
    private Integer episodeNumber;
    private String title;
    private String description;
    private Integer durationMin;
    private String thumbnailUrl;   // objectName — frontend: MINIO_URL + "/" + thumbnailUrl
    private Long viewCount;
}
