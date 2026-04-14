package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.config.CustomUserDetails;
import bekhruz.com.cinemora.config.UserSession;
import bekhruz.com.cinemora.dto.watchhistory.WatchHistoryRequest;
import bekhruz.com.cinemora.dto.watchhistory.WatchHistoryResponse;
import bekhruz.com.cinemora.entity.Content;
import bekhruz.com.cinemora.entity.Episode;
import bekhruz.com.cinemora.entity.User;
import bekhruz.com.cinemora.entity.WatchHistory;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.ContentRepository;
import bekhruz.com.cinemora.repository.EpisodeRepository;
import bekhruz.com.cinemora.repository.WatchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchHistoryService {
    private final WatchHistoryRepository watchHistoryRepository;
    private final ContentRepository contentRepository;
    private final EpisodeRepository episodeRepository;
    private final UserSession userSession;


    public Page<WatchHistoryResponse> getMyHistory(Pageable pageable) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        return watchHistoryRepository
                .findByUser_Id(currentUser.getUserId(), pageable)
                .map(this::toResponse);
    }


    public WatchHistoryResponse getByContent(UUID contentId) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        WatchHistory wh = watchHistoryRepository
                .findByUser_IdAndContent_Id(currentUser.getUserId(), contentId)
                .orElseThrow(() -> new GenericNotFoundException("Tarix topilmadi"));
        return toResponse(wh);
    }

    // ── Saqlash (upsert) ──────────────────────────────────
    // Mavjud bo'lsa yangilanadi, bo'lmasa yangi yaratiladi
    @Transactional
    public WatchHistoryResponse save(WatchHistoryRequest req) {
        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + req.getContentId()));

        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        // Mavjudmi?
        Optional<WatchHistory> existing = req.getEpisodeId() != null
                ? watchHistoryRepository.findByUser_IdAndContent_IdAndEpisode_Id(
                user.getId(), req.getContentId(), req.getEpisodeId())
                : watchHistoryRepository.findByUser_IdAndContent_IdAndEpisodeIsNull(
                user.getId(), req.getContentId());

        WatchHistory wh = existing.orElse(new WatchHistory());

        wh.setUser(user);
        wh.setContent(content);
        wh.setStoppedAt(req.getStoppedAt());
        wh.setIsCompleted(req.getIsCompleted() != null && req.getIsCompleted());

        // Serial epizodi uchun
        if (req.getEpisodeId() != null) {
            Episode episode = episodeRepository.findById(req.getEpisodeId())
                    .orElseThrow(() -> new GenericNotFoundException("Epizod topilmadi: " + req.getEpisodeId()));
            wh.setEpisode(episode);
        }

        return toResponse(watchHistoryRepository.save(wh));
    }


    @Transactional
    public void delete(UUID id) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        WatchHistory wh = watchHistoryRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Tarix topilmadi: " + id));
        // Faqat o'z tarixini o'chira oladi
        if (!wh.getUser().getId().equals(currentUser.getUserId()))
            throw new GenericNotFoundException("Tarix topilmadi: " + id);
        watchHistoryRepository.deleteById(id);
    }

    // ── Hammasini tozalash ────────────────────────────────
    @Transactional
    public void clearAll() {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        watchHistoryRepository.deleteAllByUser_Id(currentUser.getUserId());
    }


    private WatchHistoryResponse toResponse(WatchHistory wh) {
        WatchHistoryResponse res = new WatchHistoryResponse();
        res.setId(wh.getId());
        res.setContentId(wh.getContent().getId());
        res.setContentTitle(wh.getContent().getTitle());
        res.setContentPoster(wh.getContent().getPosterUrl());
        res.setEpisodeId(wh.getEpisode() != null ? wh.getEpisode().getId() : null);
        res.setEpisodeNumber(wh.getEpisode() != null ? wh.getEpisode().getEpisodeNumber() : null);
        res.setStoppedAt(wh.getStoppedAt());
        res.setIsCompleted(wh.getIsCompleted());
        res.setUpdatedAt(wh.getUpdatedAt());
        return res;
    }
}
