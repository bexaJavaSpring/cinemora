package bekhruz.com.cinemora.dto.comment;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CommentResponse {
    private UUID id;
    private UUID   contentId;
    private UUID   userId;
    private String username;
    private String avatarUrl;
    private String text;
    private Integer likesCount;
    private Boolean isActive;
    private UUID   parentId;
    private List<CommentResponse> replies;
    private LocalDateTime createdAt;
}
