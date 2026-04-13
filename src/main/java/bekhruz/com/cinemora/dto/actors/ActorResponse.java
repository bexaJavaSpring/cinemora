package bekhruz.com.cinemora.dto.actors;

import lombok.Data;

import java.util.UUID;

@Data
public class ActorResponse {
    private UUID id;
    private String  fullName;
    private String  photoUrl;
    private String  birthYear;
    private String  nationality;
}
