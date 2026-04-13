package bekhruz.com.cinemora.dto.content;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContentTypeRequest {
    @NotBlank
    @Size(max = 50)
    private String name;   // "Kino", "Serial", "Multfilm", "Anime"

    @NotBlank @Size(max = 50)
    private String slug;   // "movie", "serial", "cartoon", "anime"
}
