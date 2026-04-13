package bekhruz.com.cinemora.dto.genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenreRequest {
    @NotBlank(message = "Nom bo'sh bo'lishi mumkin emas")
    @Size(max = 100, message = "Nom 100 belgidan oshmasin")
    private String name;   // "Drama"

    @NotBlank(message = "Slug bo'sh bo'lishi mumkin emas")
    @Size(max = 100, message = "Slug 100 belgidan oshmasin")
    private String slug;
}
