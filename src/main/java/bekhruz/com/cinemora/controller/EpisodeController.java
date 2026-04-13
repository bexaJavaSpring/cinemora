package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.episode.EpisodeRequest;
import bekhruz.com.cinemora.dto.episode.EpisodeResponse;
import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.service.EpisodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/episodes")
public class EpisodeController {

    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<EpisodeResponse>> getByContent(@PathVariable UUID contentId) {
        return ResponseEntity.ok(episodeService.getByContent(contentId));
    }

    // ── 2. MAVSUM EPIZODLARI ─────────────────────────────
    // GET /api/v1/episodes/season/3
    @GetMapping("/season/{seasonId}")
    public ResponseEntity<List<EpisodeResponse>> getBySeason(@PathVariable UUID seasonId) {
        return ResponseEntity.ok(episodeService.getBySeason(seasonId));
    }

    // ── 3. BITTA EPIZOD ───────────────────────────────────
    // GET /api/v1/episodes/10
    @GetMapping("/{id}")
    public ResponseEntity<EpisodeResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(episodeService.getById(id));
    }

    // ── 4. YANGI EPIZOD QO'SHISH (Admin) ─────────────────
    // POST /api/v1/episodes
    // multipart/form-data:
    //   - data (JSON): { title, episodeNumber, contentId, seasonId, durationMin }
    //   - thumbnail (file, ixtiyoriy): epizod muqovasi → Minio ga ketadi
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<EpisodeResponse> create(
            @RequestPart("data") EpisodeRequest request
    ) {
        return ResponseEntity.status(201).body(episodeService.create(request));
    }

    // ── 5. EPIZOD TAHRIRLASH (Admin) ──────────────────────
    // PUT /api/v1/episodes/10
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<EpisodeResponse> update(
            @PathVariable UUID id,
            @RequestPart("data") EpisodeRequest request
    ) {
        return ResponseEntity.ok(episodeService.update(id, request));
    }

    // ── 6. EPIZOD O'CHIRISH (Admin) ───────────────────────
    // DELETE /api/v1/episodes/10
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        episodeService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Epizod o'chirildi"));
    }

    // ── 7. KO'RISHLAR SONINI OSHIRISH ────────────────────
    // PATCH /api/v1/episodes/10/view
    @PatchMapping("/{id}/view")
    public ResponseEntity<ApiResponse> incrementView(@PathVariable UUID id) {
        episodeService.incrementView(id);
        return ResponseEntity.ok(new ApiResponse(true, "OK"));
    }
}
