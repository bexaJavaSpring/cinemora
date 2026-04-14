package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeasonRepository extends JpaRepository<Season, UUID> {

    List<Season> findByContent_IdOrderBySeasonNumberAsc(UUID contentId);

    boolean existsByContentIdAndSeasonNumber(UUID contentId, String seasonNumber);
}
