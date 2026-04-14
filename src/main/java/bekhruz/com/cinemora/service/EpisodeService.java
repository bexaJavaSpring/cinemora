package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.episode.EpisodeRequest;
import bekhruz.com.cinemora.dto.episode.EpisodeResponse;
import bekhruz.com.cinemora.entity.Content;
import bekhruz.com.cinemora.entity.Episode;
import bekhruz.com.cinemora.entity.Season;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.ContentRepository;
import bekhruz.com.cinemora.repository.EpisodeRepository;
import bekhruz.com.cinemora.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final ContentRepository contentRepository;
    private final SeasonRepository seasonRepository;

    public List<EpisodeResponse> getByContent(UUID contentId) {
        return episodeRepository
                .findByContent_IdOrderBySeasonSeasonNumberAscEpisodeNumberAsc(contentId)
                .stream().map(this::toResponse).toList();
    }

    public List<EpisodeResponse> getBySeason(UUID seasonId) {
        return episodeRepository
                .findBySeason_IdOrderByEpisodeNumberAsc(seasonId)
                .stream().map(this::toResponse).toList();
    }

    public EpisodeResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    @Transactional
    public EpisodeResponse create(EpisodeRequest req) {
        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + req.getContentId()));

        Season season = seasonRepository.findById(req.getSeasonId())
                .orElseThrow(() -> new GenericNotFoundException("Mavsum topilmadi: " + req.getSeasonId()));

        Episode episode = Episode.builder()
                .content(content)
                .season(season)
                .episodeNumber(req.getEpisodeNumber())
                .title(req.getTitle())
                .description(req.getDescription())
                .durationMin(req.getDurationMin())
                .thumbnailUrl(req.getThumbnailUrl())
                .viewCount(0L)
                .build();

        return toResponse(episodeRepository.save(episode));
    }


    @Transactional
    public EpisodeResponse update(UUID id, EpisodeRequest req) {
        Episode episode = findById(id);
        episode.setEpisodeNumber(req.getEpisodeNumber());
        episode.setTitle(req.getTitle());
        episode.setDescription(req.getDescription());
        episode.setDurationMin(req.getDurationMin());
        episode.setThumbnailUrl(req.getThumbnailUrl());
        return toResponse(episodeRepository.save(episode));
    }


    @Transactional
    public void delete(UUID id) {
        episodeRepository.deleteById(id);
    }

    // ── Ko'rishlar sonini oshirish ────────────────────────
    @Transactional
    public void incrementView(UUID id) {
        Episode episode = findById(id);
        episode.setViewCount(episode.getViewCount() + 1);
    }


    private Episode findById(UUID id) {
        return episodeRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Epizod topilmadi: " + id));
    }

    private EpisodeResponse toResponse(Episode e) {
        EpisodeResponse res = new EpisodeResponse();
        res.setId(e.getId());
        res.setContentId(e.getContent().getId());
        res.setSeasonId(e.getSeason().getId());
        res.setSeasonNumber(e.getSeason().getSeasonNumber());
        res.setEpisodeNumber(e.getEpisodeNumber());
        res.setTitle(e.getTitle());
        res.setDescription(e.getDescription());
        res.setDurationMin(e.getDurationMin());
        res.setThumbnailUrl(e.getThumbnailUrl());   // objectName — frontend Minio URL yasaydi
        res.setViewCount(e.getViewCount());
        return res;
    }
}
