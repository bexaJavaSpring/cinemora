package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EpisodeRepository extends JpaRepository<Episode, UUID> {
    List<Episode> findByContent_IdOrderBySeasonSeasonNumberAscEpisodeNumberAsc(UUID contentId);

    List<Episode> findBySeason_IdOrderByEpisodeNumberAsc(UUID seasonId);
}
