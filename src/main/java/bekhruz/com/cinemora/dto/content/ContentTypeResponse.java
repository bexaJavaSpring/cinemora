package bekhruz.com.cinemora.dto.content;

import lombok.Data;

import java.util.UUID;

@Data
public class ContentTypeResponse {
    private UUID id;
    private String  name;
    private String  slug;
}
