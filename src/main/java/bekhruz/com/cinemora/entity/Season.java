package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "seasons")
@Builder
public class Season extends Auditable {

    private String title;

    private String seasonNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    private Integer releaseYear;

    @Column(length = 500)
    private String posterUrl;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Episode> episodes = new ArrayList<>();
}
