package bekhruz.com.cinemora.dto.actors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActorRequest {
    @NotBlank(message = "Ism-familiya bo'sh bo'lishi mumkin emas")
    @Size(max = 150, message = "Ism 150 belgidan oshmasin")
    private String fullName;     // "Leonardo DiCaprio"

    @Size(max = 500, message = "URL 500 belgidan oshmasin")
    private String photoUrl;

    private String birthYear;    // "1974"

    @Size(max = 100)
    private String nationality;  // "Amerikalik"
}
