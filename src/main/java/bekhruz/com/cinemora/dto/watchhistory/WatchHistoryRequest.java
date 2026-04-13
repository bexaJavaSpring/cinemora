package bekhruz.com.cinemora.dto.watchhistory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class WatchHistoryRequest {
    @NotNull(message = "ContentId kiritilishi shart")
    private UUID contentId;

    private UUID episodeId;   // Serial uchun, kino uchun null

    @NotNull(message = "StoppedAt kiritilishi shart")
    @Min(value = 0)
    private Integer stoppedAt;   // Sekundlarda (1240 = 20 daqiqa 40 soniya)

    private Boolean isCompleted;
}
