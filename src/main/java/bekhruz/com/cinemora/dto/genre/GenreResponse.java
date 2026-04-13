package bekhruz.com.cinemora.dto.genre;

import lombok.Data;

import java.util.UUID;

@Data
public class GenreResponse {
    private UUID id;
    private String  name;
    private String  slug;
}
