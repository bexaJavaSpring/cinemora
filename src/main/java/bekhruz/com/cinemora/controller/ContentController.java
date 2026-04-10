package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.content.ContentRequest;
import bekhruz.com.cinemora.dto.content.ContentResponse;
import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.service.ContentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contents")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public ResponseEntity<Page<ContentResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(contentService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(contentService.getById(id));
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 3. SLUG ORQALI BITTA KONTENT
    // GET /api/v1/contents/slug/oppenheimer-2023
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ContentResponse> getBySlug(@PathVariable String slug) {
        // Ko'rishlar sonini +1 qiladi
        return ResponseEntity.ok(contentService.getBySlug(slug));
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 4. KINO TURLARIGA QARAB FILTER
    // GET /api/v1/contents/type/movie?page=0&size=20
    // GET /api/v1/contents/type/serial
    // GET /api/v1/contents/type/cartoon
    // GET /api/v1/contents/type/anime
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @GetMapping("/type/{typeSlug}")
    public ResponseEntity<Page<ContentResponse>> getByType(
            @PathVariable String typeSlug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(contentService.getByType(typeSlug, pageable));
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 5. JANRGA QARAB FILTER
    // GET /api/v1/contents/genre/drama?page=0&size=20
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @GetMapping("/genre/{genreSlug}")
    public ResponseEntity<Page<ContentResponse>> getByGenre(
            @PathVariable String genreSlug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(contentService.getByGenre(genreSlug, pageable));
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 6. QIDIRUV
    // GET /api/v1/contents/search?q=batman&page=0&size=20
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @GetMapping("/search")
    public ResponseEntity<Page<ContentResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(contentService.search(q, pageable));
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 7. YANGI KONTENTLAR (so'nggi qo'shilganlar)
    // GET /api/v1/contents/latest?limit=10
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @GetMapping("/latest")
    public ResponseEntity<List<ContentResponse>> getLatest(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(contentService.getLatest(limit));
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 8. TOP KONTENTLAR (reytingga qarab)
    // GET /api/v1/contents/top?limit=10
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @GetMapping("/top")
    public ResponseEntity<List<ContentResponse>> getTop(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(contentService.getTop(limit));
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 9. FEATURED (hero slider uchun)
    // GET /api/v1/contents/featured
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @GetMapping("/featured")
    public ResponseEntity<List<ContentResponse>> getFeatured() {
        return ResponseEntity.ok(contentService.getFeatured());
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 10. O'XSHASH KONTENTLAR (detail sahifa uchun)
    // GET /api/v1/contents/5/similar?limit=8
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @GetMapping("/{id}/similar")
    public ResponseEntity<List<ContentResponse>> getSimilar(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "8") int limit
    ) {
        return ResponseEntity.ok(contentService.getSimilar(id, limit));
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 11. YILGA QARAB FILTER
    // GET /api/v1/contents/year/2024?page=0&size=20
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @GetMapping("/year/{year}")
    public ResponseEntity<Page<ContentResponse>> getByYear(
            @PathVariable Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("imdbRating").descending());
        return ResponseEntity.ok(contentService.getByYear(year, pageable));
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 12. MAMLAKATGA QARAB FILTER
    // GET /api/v1/contents/country/us?page=0&size=20
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @GetMapping("/country/{countryCode}")
    public ResponseEntity<Page<ContentResponse>> getByCountry(
            @PathVariable String countryCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(contentService.getByCountry(countryCode, pageable));
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // ══ ADMIN ENDPOINTLAR ══
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    // 13. YANGI KONTENT QO'SHISH
    // POST /api/v1/contents
    // Body: { title, originalTitle, slug, description, ... }
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @PostMapping
    public ResponseEntity<ContentResponse> create(
            @RequestBody ContentRequest request
    ) {
        return ResponseEntity.status(201).body(contentService.create(request));
    }

    // 14. KONTENT TAHRIRLASH
    // PUT /api/v1/contents/5
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @PutMapping("/{id}")
    public ResponseEntity<ContentResponse> update(
            @PathVariable UUID id,
            @RequestBody ContentRequest request
    ) {
        return ResponseEntity.ok(contentService.update(id, request));
    }

    // 15. KONTENT O'CHIRISH
    // DELETE /api/v1/contents/5
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        contentService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Kontent o'chirildi"));
    }

    // 16. STATUSNI O'ZGARTIRISH (ACTIVE / INACTIVE / SOON)
    // PATCH /api/v1/contents/5/status?status=ACTIVE
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse> changeStatus(
            @PathVariable UUID id,
            @RequestParam String status
    ) {
        contentService.changeStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "Status yangilandi"));
    }

    // 17. FEATURED TOGGLE (slider uchun)
    // PATCH /api/v1/contents/5/featured
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @PatchMapping("/{id}/featured")
    public ResponseEntity<ApiResponse> toggleFeatured(@PathVariable UUID id) {
        boolean result = contentService.toggleFeatured(id);
        return ResponseEntity.ok(new ApiResponse(true,
                result ? "Featured qilindi" : "Featured olib tashlandi"));
    }

}
