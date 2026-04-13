package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.WatchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, UUID> {
    Page<WatchHistory> findByUser_Id(UUID userId, Pageable pageable);

    Optional<WatchHistory> findByUser_IdAndContent_Id(UUID userId, UUID contentId);

    // Kino uchun (epizod yo'q)
    Optional<WatchHistory> findByUser_IdAndContent_IdAndEpisodeIsNull(UUID userId, UUID contentId);

    // Serial epizodi uchun
    Optional<WatchHistory> findByUser_IdAndContent_IdAndEpisode_Id(
            UUID userId, UUID contentId, UUID episodeId);

    void deleteAllByUser_Id(UUID userId);
}
