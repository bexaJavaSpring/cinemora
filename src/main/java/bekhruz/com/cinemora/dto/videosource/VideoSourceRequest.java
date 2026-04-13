package bekhruz.com.cinemora.dto.videosource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class VideoSourceRequest {
    private UUID contentId;     // Kino uchun
    private UUID episodeId;     // Serial epizodi uchun

    @NotBlank
    private String quality;     // "HD" | "FHD" | "SD" | "CAM" | "UHD"

    @NotBlank
    private String sourceUrl;   // Minio objectName: "videos/content/5/hd.m3u8"

    @NotBlank
    private String sourceType;  // "HLS" | "IFRAME" | "DIRECT" | "TORRENT"

    private String translator;  // "O'zbek tilida", "Rus tilida"

    @Size(max = 10)
    private String language;    // "uz", "ru", "en"

    private Integer sortOrder;  // 0 = birinchi ko'rsatiladi
}
