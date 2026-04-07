package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "episodes")
@Builder
public class Episode extends Auditable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @Column(nullable = false)
    private Integer episodeNumber;     // 1, 2, 3...

    @Column(length = 200)
    private String title;              // "Birinchi epizod"

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer durationMin;

    @Column(length = 500)
    private String thumbnailUrl;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long viewCount;

    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<VideoSource> videoSources = new ArrayList<>();
}
