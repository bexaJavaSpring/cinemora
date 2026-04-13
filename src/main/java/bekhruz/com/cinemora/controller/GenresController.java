package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.genre.GenreRequest;
import bekhruz.com.cinemora.dto.genre.GenreResponse;
import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/genres")
public class GenresController {
    private final GenreService genreService;

    public GenresController(GenreService genreService) {
        this.genreService = genreService;
    }

    // ── 1. BARCHA JANRLAR ────────────────────────────────
    // GET /api/v1/genres
    // Hamma ko'ra oladi (guest ham)
    @GetMapping
    public ResponseEntity<List<GenreResponse>> getAll() {
        return ResponseEntity.ok(genreService.getAll());
    }

    // ── 2. ID ORQALI ─────────────────────────────────────
    // GET /api/v1/genres/3
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(genreService.getById(id));
    }

    // ── 3. SLUG ORQALI ───────────────────────────────────
    // GET /api/v1/genres/slug/drama
    @GetMapping("/slug/{slug}")
    public ResponseEntity<GenreResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(genreService.getBySlug(slug));
    }

    // ── 4. YANGI JANR QO'SHISH (Admin) ───────────────────
    // POST /api/v1/genres
    // Body: { "name": "Drama", "slug": "drama" }
    @PostMapping
    public ResponseEntity<GenreResponse> create(@RequestBody GenreRequest request) {
        return ResponseEntity.status(201).body(genreService.create(request));
    }

    // ── 5. JANRNI TAHRIRLASH (Admin) ──────────────────────
    // PUT /api/v1/genres/3
    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> update(
            @PathVariable UUID id,
            @RequestBody GenreRequest request
    ) {
        return ResponseEntity.ok(genreService.update(id, request));
    }

    // ── 6. JANRNI O'CHIRISH (Admin) ───────────────────────
    // DELETE /api/v1/genres/3
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        genreService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Janr o'chirildi"));
    }
}
