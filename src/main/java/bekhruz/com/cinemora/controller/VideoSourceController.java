package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.dto.videosource.VideoSourceRequest;
import bekhruz.com.cinemora.dto.videosource.VideoSourceResponse;
import bekhruz.com.cinemora.service.VideoSourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/video-sources")
public class VideoSourceController {

    private final VideoSourceService videoSourceService;

    public VideoSourceController(VideoSourceService videoSourceService) {
        this.videoSourceService = videoSourceService;
    }

    // ── 1. KONTENT VIDEO MANBALARI ────────────────────────
    // GET /api/v1/video-sources/content/5
    // Kino uchun (serial emas) barcha tarjimon/sifat variantlari
    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<VideoSourceResponse>> getByContent(@PathVariable UUID contentId) {
        return ResponseEntity.ok(videoSourceService.getByContent(contentId));
    }

    // ── 2. EPIZOD VIDEO MANBALARI ─────────────────────────
    // GET /api/v1/video-sources/episode/10
    // Serial epizodi uchun barcha tarjimon/sifat variantlari
    @GetMapping("/episode/{episodeId}")
    public ResponseEntity<List<VideoSourceResponse>> getByEpisode(@PathVariable UUID episodeId) {
        return ResponseEntity.ok(videoSourceService.getByEpisode(episodeId));
    }

    // ── 3. BITTA VIDEO MANBAA ─────────────────────────────
    // GET /api/v1/video-sources/7
    @GetMapping("/{id}")
    public ResponseEntity<VideoSourceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(videoSourceService.getById(id));
    }

    // ── 4. YANGI VIDEO MANBAA QO'SHISH (Admin) ────────────
    // POST /api/v1/video-sources
    // Body: {
    //   "contentId": 5,        ← kino uchun
    //   "episodeId": null,     ← serial uchun null, epizod uchun ID
    //   "quality": "HD",
    //   "sourceUrl": "videos/content/5/hd.m3u8",   ← Minio objectName
    //   "sourceType": "HLS",
    //   "translator": "O'zbek tilida",
    //   "language": "uz",
    //   "sortOrder": 0
    // }
    @PostMapping
    public ResponseEntity<VideoSourceResponse> create(@RequestBody VideoSourceRequest request) {
        return ResponseEntity.status(201).body(videoSourceService.create(request));
    }

    // ── 5. VIDEO MANBAA TAHRIRLASH (Admin) ────────────────
    // PUT /api/v1/video-sources/7
    @PutMapping("/{id}")
    public ResponseEntity<VideoSourceResponse> update(
            @PathVariable UUID id,
            @RequestBody VideoSourceRequest request
    ) {
        return ResponseEntity.ok(videoSourceService.update(id, request));
    }

    // ── 6. VIDEO MANBAA O'CHIRISH (Admin) ─────────────────
    // DELETE /api/v1/video-sources/7
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        videoSourceService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Video manbaa o'chirildi"));
    }

    // ── 7. FAOL/NOFAOL QILISH (Admin) ─────────────────────
    // PATCH /api/v1/video-sources/7/toggle-active
    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ApiResponse> toggleActive(@PathVariable UUID id) {
        boolean active = videoSourceService.toggleActive(id);
        return ResponseEntity.ok(new ApiResponse(true, active ? "Faollashtirildi" : "O'chirildi"));
    }
}
