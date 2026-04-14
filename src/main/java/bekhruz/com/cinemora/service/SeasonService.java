package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.season.SeasonRequest;
import bekhruz.com.cinemora.dto.season.SeasonResponse;
import bekhruz.com.cinemora.entity.Content;
import bekhruz.com.cinemora.entity.Season;
import bekhruz.com.cinemora.exception.AlreadyExistsException;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.ContentRepository;
import bekhruz.com.cinemora.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final SeasonRepository seasonRepository;
    private final ContentRepository contentRepository;

    // ── Content bo'yicha barcha mavsum ────────────────────
    public List<SeasonResponse> getByContent(UUID contentId) {
        return seasonRepository
                .findByContent_IdOrderBySeasonNumberAsc(contentId)
                .stream().map(this::toResponse).toList();
    }

    // ── ID bo'yicha ───────────────────────────────────────
    public SeasonResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    // ── Yaratish ─────────────────────────────────────────
    @Transactional
    public SeasonResponse create(SeasonRequest req) {
        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + req.getContentId()));

        // Bir xil mavsum raqami bo'lmasligi kerak
        if (seasonRepository.existsByContentIdAndSeasonNumber(req.getContentId(), String.valueOf(req.getSeasonNumber())))
            throw new AlreadyExistsException(
                    req.getSeasonNumber() + "-mavsum allaqachon mavjud"
            );

        Season season = Season.builder()
                .content(content)
                .seasonNumber(req.getSeasonNumber() + "")
                .title(req.getTitle())
                .releaseYear(req.getReleaseYear())
                .posterUrl(req.getPosterUrl())
                .build();

        return toResponse(seasonRepository.save(season));
    }

    // ── Tahrirlash ────────────────────────────────────────
    @Transactional
    public SeasonResponse update(UUID id, SeasonRequest req) {
        Season season = findById(id);

        // Mavsum raqami o'zgaryaptimi va u allaqachon bormi?
        if (!season.getSeasonNumber().equals(req.getSeasonNumber()) &&
                seasonRepository.existsByContentIdAndSeasonNumber(
                        season.getContent().getId(), String.valueOf(req.getSeasonNumber())))
            throw new AlreadyExistsException(
                    req.getSeasonNumber() + "-mavsum allaqachon mavjud"
            );

        season.setSeasonNumber(req.getSeasonNumber()+"");
        season.setTitle(req.getTitle());
        season.setReleaseYear(req.getReleaseYear());
        season.setPosterUrl(req.getPosterUrl());

        return toResponse(seasonRepository.save(season));
    }

    // ── O'chirish ─────────────────────────────────────────
    @Transactional
    public void delete(UUID id) {
        if (!seasonRepository.existsById(id))
            throw new GenericNotFoundException("Mavsum topilmadi: " + id);
        seasonRepository.deleteById(id);
    }

    // ── Helper ────────────────────────────────────────────
    private Season findById(UUID id) {
        return seasonRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Mavsum topilmadi: " + id));
    }

    private SeasonResponse toResponse(Season s) {
        SeasonResponse res = new SeasonResponse();
        res.setId(s.getId());
        res.setContentId(s.getContent().getId());
        res.setSeasonNumber(Integer.valueOf(s.getSeasonNumber()));
        res.setTitle(s.getTitle());
        res.setReleaseYear(s.getReleaseYear());
        res.setPosterUrl(s.getPosterUrl());
        res.setEpisodeCount(
                s.getEpisodes() != null ? s.getEpisodes().size() : 0
        );
        return res;
    }
}
