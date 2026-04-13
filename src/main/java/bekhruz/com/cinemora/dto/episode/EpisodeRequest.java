package bekhruz.com.cinemora.dto.episode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class EpisodeRequest {
    @NotNull(message = "ContentId kiritilishi shart")
    private UUID contentId;

    @NotNull(message = "SeasonId kiritilishi shart")
    private UUID seasonId;

    @NotNull(message = "Epizod raqami kiritilishi shart")
    @Min(value = 1, message = "Epizod raqami 1 dan kichik bo'lmasin")
    private Integer episodeNumber;

    @Size(max = 200)
    private String title;          // "Birinchi epizod"

    private String description;

    private Integer durationMin;

    private String thumbnailUrl;
}
