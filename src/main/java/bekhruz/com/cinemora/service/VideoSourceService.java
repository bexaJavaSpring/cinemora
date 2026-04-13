package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.videosource.VideoSourceRequest;
import bekhruz.com.cinemora.dto.videosource.VideoSourceResponse;
import bekhruz.com.cinemora.entity.Content;
import bekhruz.com.cinemora.entity.Episode;
import bekhruz.com.cinemora.entity.VideoSource;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.ContentRepository;
import bekhruz.com.cinemora.repository.EpisodeRepository;
import bekhruz.com.cinemora.repository.VideoSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoSourceService {
    private final VideoSourceRepository videoSourceRepository;
    private final ContentRepository contentRepository;
    private final EpisodeRepository episodeRepository;

    // ── Kontent video manbalari ───────────────────────────
    public List<VideoSourceResponse> getByContent(UUID contentId) {
        return videoSourceRepository
                .findByContent_IdAndIsActiveTrueOrderBySortOrderAsc(contentId)
                .stream().map(this::toResponse).toList();
    }

    // ── Epizod video manbalari ────────────────────────────
    public List<VideoSourceResponse> getByEpisode(UUID episodeId) {
        return videoSourceRepository
                .findByEpisode_IdAndIsActiveTrueOrderBySortOrderAsc(episodeId)
                .stream().map(this::toResponse).toList();
    }

    // ── ID bo'yicha ───────────────────────────────────────
    public VideoSourceResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    // ── Yaratish ─────────────────────────────────────────
    @Transactional
    public VideoSourceResponse create(VideoSourceRequest req) {
        VideoSource vs = VideoSource.builder()
                .quality(VideoSource.VideoQuality.valueOf(req.getQuality()))
                .sourceUrl(req.getSourceUrl())
                .sourceType(VideoSource.SourceType.valueOf(req.getSourceType()))
                .translator(req.getTranslator())
                .language(req.getLanguage())
                .isActive(true)
                .sortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0)
                .build();

        // Kino uchun contentId, serial epizodi uchun episodeId
        if (req.getContentId() != null) {
            Content content = contentRepository.findById(req.getContentId())
                    .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + req.getContentId()));
            vs.setContent(content);
        }

        if (req.getEpisodeId() != null) {
            Episode episode = episodeRepository.findById(req.getEpisodeId())
                    .orElseThrow(() -> new GenericNotFoundException("Epizod topilmadi: " + req.getEpisodeId()));
            vs.setEpisode(episode);
        }

        return toResponse(videoSourceRepository.save(vs));
    }

    // ── Tahrirlash ────────────────────────────────────────
    @Transactional
    public VideoSourceResponse update(UUID id, VideoSourceRequest req) {
        VideoSource vs = findById(id);

        vs.setQuality(VideoSource.VideoQuality.valueOf(req.getQuality()));
        vs.setSourceUrl(req.getSourceUrl());
        vs.setSourceType(VideoSource.SourceType.valueOf(req.getSourceType()));
        vs.setTranslator(req.getTranslator());
        vs.setLanguage(req.getLanguage());
        if (req.getSortOrder() != null) vs.setSortOrder(req.getSortOrder());

        return toResponse(videoSourceRepository.save(vs));
    }

    // ── O'chirish ─────────────────────────────────────────
    @Transactional
    public void delete(UUID id) {
        if (!videoSourceRepository.existsById(id))
            throw new GenericNotFoundException("Video manbaa topilmadi: " + id);
        videoSourceRepository.deleteById(id);
    }

    // ── Faol/Nofaol ───────────────────────────────────────
    @Transactional
    public boolean toggleActive(UUID id) {
        VideoSource vs = findById(id);
        vs.setIsActive(!vs.getIsActive());
        return vs.getIsActive();
    }

    // ── Helper ────────────────────────────────────────────
    private VideoSource findById(UUID id) {
        return videoSourceRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Video manbaa topilmadi: " + id));
    }

    private VideoSourceResponse toResponse(VideoSource vs) {
        VideoSourceResponse res = new VideoSourceResponse();
        res.setId(vs.getId());
        res.setContentId(vs.getContent() != null ? vs.getContent().getId() : null);
        res.setEpisodeId(vs.getEpisode() != null ? vs.getEpisode().getId() : null);
        res.setQuality(vs.getQuality().name());
        res.setSourceUrl(vs.getSourceUrl());
        res.setSourceType(vs.getSourceType().name());
        res.setTranslator(vs.getTranslator());
        res.setLanguage(vs.getLanguage());
        res.setIsActive(vs.getIsActive());
        res.setSortOrder(vs.getSortOrder());
        return res;
    }
}
