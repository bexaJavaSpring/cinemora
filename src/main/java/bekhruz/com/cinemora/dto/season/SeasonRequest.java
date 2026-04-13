package bekhruz.com.cinemora.dto.season;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class SeasonRequest {
    @NotNull(message = "ContentId kiritilishi shart")
    private UUID contentId;

    @NotNull(message = "Mavsum raqami kiritilishi shart")
    @Min(value = 1, message = "Mavsum raqami 1 dan kichik bo'lmasin")
    private Integer seasonNumber;

    @Size(max = 200)
    private String title;        // "1-mavsum", "Birinchi mavsum"

    private Integer releaseYear;

    private String posterUrl;
}
