package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sliders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slider extends Auditable {
    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    // Qaysi kontentga olib boradi (ixtiyoriy)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    // Yoki tashqi link
    @Column(length = 500)
    private String externalUrl;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder;
}
