package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "watch_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchHistory extends Auditable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    // Serial uchun (ixtiyoriy)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id")
    private Episode episode;

    // Qaysi vaqtgacha ko'rilgan (sekundlarda)
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer stoppedAt;

    // To'liq ko'rilganmi
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isCompleted;
}
