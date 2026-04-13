package bekhruz.com.cinemora.dto.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class MeResponse {
    private UUID id;
    private String username;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String role;
}
