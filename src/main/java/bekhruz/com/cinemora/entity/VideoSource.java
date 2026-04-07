package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "video_sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoSource extends Auditable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id")
    private Episode episode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private VideoQuality quality;      // HD, FHD, UHD

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sourceUrl;          // iframe yoki to'g'ri link

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SourceType sourceType;     // IFRAME, DIRECT, HLS, TORRENT

    @Column(length = 100)
    private String translator;         // "O'zbek tilida", "Rus tilida"

    @Column(length = 10)
    private String language;           // "uz", "ru", "en"

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder;         // tartib raqami (birinchi ko'rsatiladigan)

    public enum VideoQuality {
        CAM, SD, HD, FHD, UHD
    }

    public enum SourceType {
        IFRAME, DIRECT, HLS, TORRENT
    }
}
