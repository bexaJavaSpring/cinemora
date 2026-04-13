package bekhruz.com.cinemora.dto.watchhistory;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WatchHistoryResponse {
    private UUID id;
    private UUID contentId;
    private String contentTitle;
    private String contentPoster;
    private UUID episodeId;
    private Integer episodeNumber;
    private Integer stoppedAt;
    private Boolean isCompleted;
    private LocalDateTime updatedAt;
}
