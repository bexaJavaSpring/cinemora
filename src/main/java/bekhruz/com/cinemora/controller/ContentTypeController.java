package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.content.ContentTypeRequest;
import bekhruz.com.cinemora.dto.content.ContentTypeResponse;
import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.service.ContentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/content-types")
public class ContentTypeController {
    private final ContentTypeService contentTypeService;

    public ContentTypeController(ContentTypeService contentTypeService) {
        this.contentTypeService = contentTypeService;
    }

    // ── 1. BARCHASI ──────────────────────────────────────
    // GET /api/v1/content-types
    @GetMapping
    public ResponseEntity<List<ContentTypeResponse>> getAll() {
        return ResponseEntity.ok(contentTypeService.getAll());
    }

    // ── 2. ID ORQALI ─────────────────────────────────────
    // GET /api/v1/content-types/1
    @GetMapping("/{id}")
    public ResponseEntity<ContentTypeResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(contentTypeService.getById(id));
    }

    // ── 3. SLUG ORQALI ───────────────────────────────────
    // GET /api/v1/content-types/slug/movie
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ContentTypeResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(contentTypeService.getBySlug(slug));
    }

    // ── 4. YANGI QO'SHISH (Admin) ─────────────────────────
    // POST /api/v1/content-types
    // Body: { "name": "Kino", "slug": "movie" }
    @PostMapping
    public ResponseEntity<ContentTypeResponse> create(@RequestBody ContentTypeRequest request) {
        return ResponseEntity.status(201).body(contentTypeService.create(request));
    }

    // ── 5. TAHRIRLASH (Admin) ─────────────────────────────
    // PUT /api/v1/content-types/1
    @PutMapping("/{id}")
    public ResponseEntity<ContentTypeResponse> update(
            @PathVariable UUID id,
            @RequestBody ContentTypeRequest request
    ) {
        return ResponseEntity.ok(contentTypeService.update(id, request));
    }

    // ── 6. O'CHIRISH (Admin) ──────────────────────────────
    // DELETE /api/v1/content-types/1
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        contentTypeService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Content turi o'chirildi"));
    }
}
