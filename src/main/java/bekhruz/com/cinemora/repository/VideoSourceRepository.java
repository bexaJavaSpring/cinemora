package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.VideoSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VideoSourceRepository extends JpaRepository<VideoSource, UUID> {
    List<VideoSource> findByContent_IdAndIsActiveTrueOrderBySortOrderAsc(UUID contentId);

    // Epizod uchun — sortOrder bo'yicha tartiblangan, faqat faollar
    List<VideoSource> findByEpisode_IdAndIsActiveTrueOrderBySortOrderAsc(UUID episodeId);
}
