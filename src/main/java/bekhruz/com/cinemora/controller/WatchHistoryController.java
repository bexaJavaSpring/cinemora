package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.dto.watchhistory.WatchHistoryRequest;
import bekhruz.com.cinemora.dto.watchhistory.WatchHistoryResponse;
import bekhruz.com.cinemora.service.WatchHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/watch-histories")
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    public WatchHistoryController(WatchHistoryService watchHistoryService) {
        this.watchHistoryService = watchHistoryService;
    }

    // ── 1. O'Z KO'RISH TARIXI (login kerak) ──────────────
    // GET /api/v1/history?page=0&size=20
    @GetMapping
    public ResponseEntity<Page<WatchHistoryResponse>> getMyHistory(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        return ResponseEntity.ok(watchHistoryService.getMyHistory(pageable));
    }

    // ── 2. QAYSI VAQTDA TO'XTATGANINI SAQLASH ────────────
    // POST /api/v1/history
    // Video ko'rayotganda har 10 sekundda chaqiriladi
    // Body: { "contentId": 5, "episodeId": null, "stoppedAt": 1240 }
    // Mavjud bo'lsa update, bo'lmasa create qiladi (upsert)
    @PostMapping
    public ResponseEntity<WatchHistoryResponse> save(
            @RequestBody WatchHistoryRequest request
    ) {
        return ResponseEntity.ok(watchHistoryService.save(request));
    }

    // ── 3. BITTA KONTENT UCHUN TARIX ─────────────────────
    // GET /api/v1/history/content/5
    // Player ochilganda — qayerda to'xtatganini bilish uchun
    @GetMapping("/content/{contentId}")
    public ResponseEntity<WatchHistoryResponse> getByContent(
            @PathVariable UUID contentId) {
        return ResponseEntity.ok(watchHistoryService.getByContent(contentId));
    }

    // ── 4. TARIXDAN O'CHIRISH ─────────────────────────────
    // DELETE /api/v1/history/15
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable UUID id) {
        watchHistoryService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Tarixdan o'chirildi"));
    }

    // ── 5. BARCHA TARIXNI TOZALASH ────────────────────────
    // DELETE /api/v1/history/clear
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearAll() {
        watchHistoryService.clearAll();
        return ResponseEntity.ok(new ApiResponse(true, "Ko'rish tarixi tozalandi"));
    }
}
