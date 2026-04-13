package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.dto.season.SeasonRequest;
import bekhruz.com.cinemora.dto.season.SeasonResponse;
import bekhruz.com.cinemora.service.SeasonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seasons")
public class SeasonController {

    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    // ── 1. SERIAL BARCHA MAVSUMLARI ───────────────────────
    // GET /api/v1/seasons/content/5
    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<SeasonResponse>> getByContent(@PathVariable UUID contentId) {
        return ResponseEntity.ok(seasonService.getByContent(contentId));
    }

    // ── 2. BITTA MAVSUM ───────────────────────────────────
    // GET /api/v1/seasons/3
    @GetMapping("/{id}")
    public ResponseEntity<SeasonResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(seasonService.getById(id));
    }

    // ── 3. YANGI MAVSUM QO'SHISH (Admin) ──────────────────
    // POST /api/v1/seasons
    // Body: { "contentId": 5, "seasonNumber": 1, "title": "1-mavsum", "releaseYear": 2023 }
    @PostMapping
    public ResponseEntity<SeasonResponse> create(@RequestBody SeasonRequest request) {
        return ResponseEntity.status(201).body(seasonService.create(request));
    }

    // ── 4. MAVSUM TAHRIRLASH (Admin) ──────────────────────
    // PUT /api/v1/seasons/3
    @PutMapping("/{id}")
    public ResponseEntity<SeasonResponse> update(
            @PathVariable UUID id,
            @RequestBody SeasonRequest request
    ) {
        return ResponseEntity.ok(seasonService.update(id, request));
    }

    // ── 5. MAVSUM O'CHIRISH (Admin) ───────────────────────
    // DELETE /api/v1/seasons/3
    // Ichidagi barcha epizodlar ham o'chadi (CASCADE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        seasonService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Mavsum o'chirildi"));
    }
}
