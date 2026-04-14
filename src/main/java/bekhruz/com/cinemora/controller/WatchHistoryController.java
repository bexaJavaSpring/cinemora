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

    @GetMapping
    public ResponseEntity<Page<WatchHistoryResponse>> getMyHistory(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        return ResponseEntity.ok(watchHistoryService.getMyHistory(pageable));
    }

    @PostMapping
    public ResponseEntity<WatchHistoryResponse> save(
            @RequestBody WatchHistoryRequest request
    ) {
        return ResponseEntity.ok(watchHistoryService.save(request));
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<WatchHistoryResponse> getByContent(
            @PathVariable UUID contentId) {
        return ResponseEntity.ok(watchHistoryService.getByContent(contentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable UUID id) {
        watchHistoryService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Tarixdan o'chirildi"));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearAll() {
        watchHistoryService.clearAll();
        return ResponseEntity.ok(new ApiResponse(true, "Ko'rish tarixi tozalandi"));
    }
}
