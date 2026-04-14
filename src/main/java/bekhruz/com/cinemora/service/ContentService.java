package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.content.ContentRequest;
import bekhruz.com.cinemora.dto.content.ContentResponse;
import bekhruz.com.cinemora.entity.Content;
import bekhruz.com.cinemora.entity.enums.Status;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.mapper.ContentMapper;
import bekhruz.com.cinemora.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final ContentMapper contentMapper;


    public Page<ContentResponse> getAll(Pageable pageable) {
        return contentRepository.findAll(pageable).map(contentMapper::toResponse);
    }

    public ContentResponse getById(UUID id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + id));
        return contentMapper.toResponse(content);
    }

    // ── 3. Slug bo'yicha + ko'rishlar sonini oshirish ────────
    @Transactional
    public ContentResponse getBySlug(String slug) {
        Content content = contentRepository.findBySlug(slug)
                .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + slug));
        content.setViewCount(content.getViewCount() + 1);
        return contentMapper.toResponse(content);
    }

    public Page<ContentResponse> getByType(String typeSlug, Pageable pageable) {
        return contentRepository
                .findByContentType_SlugAndStatus(typeSlug, Status.ACTIVE, pageable)
                .map(contentMapper::toResponse);
    }


    public Page<ContentResponse> getByGenre(String genreSlug, Pageable pageable) {
        return contentRepository
                .findByGenres_SlugAndStatus(genreSlug, Status.ACTIVE, pageable)
                .map(contentMapper::toResponse);
    }

    // ── 6. Qidiruv (title yoki originalTitle) ───────────────
    public Page<ContentResponse> search(String query, Pageable pageable) {
        return contentRepository
                .findByTitleContainingIgnoreCaseOrOriginalTitleContainingIgnoreCase(
                        query, query, pageable)
                .map(contentMapper::toResponse);
    }

    // ── 7. Yangi qo'shilganlar ───────────────────────────────
    public List<ContentResponse> getLatest(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return contentRepository
                .findByStatus(Status.ACTIVE, pageable)
                .map(contentMapper::toResponse)
                .getContent();
    }

    public List<ContentResponse> getTop(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("imdbRating").descending());
        return contentRepository
                .findByStatus(Status.ACTIVE, pageable)
                .map(contentMapper::toResponse)
                .getContent();
    }

    // ── 9. Featured (hero slider uchun) ─────────────────────
    public List<ContentResponse> getFeatured() {
        return contentRepository
                .findByIsFeaturedTrueAndStatus(Status.ACTIVE)
                .stream()
                .map(contentMapper::toResponse)
                .toList();
    }

    // ── 10. O'xshash kontentlar (bir xil janr va tur) ────────
    public List<ContentResponse> getSimilar(UUID id, int limit) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + id));

        Pageable pageable = PageRequest.of(0, limit + 1);
        return contentRepository
                .findSimilar(
                        content.getContentType().getId(),
                        content.getGenres().stream().map(g -> g.getId()).toList(),
                        id,
                        Status.ACTIVE,
                        pageable)
                .stream()
                .map(contentMapper::toResponse)
                .toList();
    }

    // ── 11. Yilga qarab ─────────────────────────────────────
    public Page<ContentResponse> getByYear(Integer year, Pageable pageable) {
        return contentRepository
                .findByReleaseYearAndStatus(year, Status.ACTIVE, pageable)
                .map(contentMapper::toResponse);
    }

    // ── 12. Mamlakatga qarab ─────────────────────────────────
    public Page<ContentResponse> getByCountry(String countryCode, Pageable pageable) {
        return contentRepository
                .findByCountry_CodeIgnoreCaseAndStatus(countryCode, Status.ACTIVE, pageable)
                .map(contentMapper::toResponse);
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // ADMIN METHODLAR
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    @Transactional
    public ContentResponse create(ContentRequest request) {
        Content content = contentMapper.toEntity(request);
        content.setViewCount(0L);
        content.setStatus(Status.ACTIVE);
        content.setIsFeatured(false);
        content.setIsTop(false);
        return contentMapper.toResponse(contentRepository.save(content));
    }

    @Transactional
    public ContentResponse update(UUID id, ContentRequest request) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + id));
        contentMapper.updateFromRequest(request, content);
        return contentMapper.toResponse(contentRepository.save(content));
    }

    @Transactional
    public void delete(UUID id) {
        if (!contentRepository.existsById(id))
            throw new GenericNotFoundException("Kontent topilmadi: " + id);
        contentRepository.deleteById(id);
    }

    // ── 16. Status o'zgartirish ──────────────────────────────
    @Transactional
    public void changeStatus(UUID id, String status) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + id));
        content.setStatus(Status.valueOf(status.toUpperCase()));
    }

    // ── 17. Featured toggle ──────────────────────────────────
    @Transactional
    public boolean toggleFeatured(UUID id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + id));
        content.setIsFeatured(!content.getIsFeatured());
        return content.getIsFeatured();
    }
}
