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


    @GetMapping("/slug/{slug}")
    public ResponseEntity<ContentResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(contentService.getBySlug(slug));
    }


    @GetMapping("/type/{typeSlug}")
    public ResponseEntity<Page<ContentResponse>> getByType(
            @PathVariable String typeSlug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(contentService.getByType(typeSlug, pageable));
    }


    @GetMapping("/genre/{genreSlug}")
    public ResponseEntity<Page<ContentResponse>> getByGenre(
            @PathVariable String genreSlug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(contentService.getByGenre(genreSlug, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ContentResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(contentService.search(q, pageable));
    }


    @GetMapping("/latest")
    public ResponseEntity<List<ContentResponse>> getLatest(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(contentService.getLatest(limit));
    }

    @GetMapping("/top")
    public ResponseEntity<List<ContentResponse>> getTop(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(contentService.getTop(limit));
    }


    @GetMapping("/featured")
    public ResponseEntity<List<ContentResponse>> getFeatured() {
        return ResponseEntity.ok(contentService.getFeatured());
    }


    @GetMapping("/{id}/similar")
    public ResponseEntity<List<ContentResponse>> getSimilar(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "8") int limit
    ) {
        return ResponseEntity.ok(contentService.getSimilar(id, limit));
    }


    @GetMapping("/year/{year}")
    public ResponseEntity<Page<ContentResponse>> getByYear(
            @PathVariable Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("imdbRating").descending());
        return ResponseEntity.ok(contentService.getByYear(year, pageable));
    }


    @GetMapping("/country/{countryCode}")
    public ResponseEntity<Page<ContentResponse>> getByCountry(
            @PathVariable String countryCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(contentService.getByCountry(countryCode, pageable));
    }


    @PostMapping
    public ResponseEntity<ContentResponse> create(
            @RequestBody ContentRequest request
    ) {
        return ResponseEntity.status(201).body(contentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentResponse> update(
            @PathVariable UUID id,
            @RequestBody ContentRequest request
    ) {
        return ResponseEntity.ok(contentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        contentService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Kontent o'chirildi"));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse> changeStatus(
            @PathVariable UUID id,
            @RequestParam String status
    ) {
        contentService.changeStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "Status yangilandi"));
    }

    @PatchMapping("/{id}/featured")
    public ResponseEntity<ApiResponse> toggleFeatured(@PathVariable UUID id) {
        boolean result = contentService.toggleFeatured(id);
        return ResponseEntity.ok(new ApiResponse(true,
                result ? "Featured qilindi" : "Featured olib tashlandi"));
    }

}
