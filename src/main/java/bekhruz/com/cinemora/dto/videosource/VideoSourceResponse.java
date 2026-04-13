package bekhruz.com.cinemora.dto.videosource;

import lombok.Data;

import java.util.UUID;

@Data
public class VideoSourceResponse {
    private UUID id;
    private UUID    contentId;
    private UUID    episodeId;
    private String  quality;      // "HD", "FHD"...
    private String  sourceUrl;    // Minio objectName
    private String  sourceType;   // "HLS", "IFRAME"...
    private String  translator;
    private String  language;
    private Boolean isActive;
    private Integer sortOrder;
}
