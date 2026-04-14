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

    @GetMapping("/season/{seasonId}")
    public ResponseEntity<List<EpisodeResponse>> getBySeason(@PathVariable UUID seasonId) {
        return ResponseEntity.ok(episodeService.getBySeason(seasonId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpisodeResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(episodeService.getById(id));
    }

    @PostMapping
    public ResponseEntity<EpisodeResponse> create(
            @RequestPart("data") EpisodeRequest request
    ) {
        return ResponseEntity.status(201).body(episodeService.create(request));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EpisodeResponse> update(
            @PathVariable UUID id,
            @RequestPart("data") EpisodeRequest request
    ) {
        return ResponseEntity.ok(episodeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        episodeService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Epizod o'chirildi"));
    }

    @PatchMapping("/{id}/view")
    public ResponseEntity<ApiResponse> incrementView(@PathVariable UUID id) {
        episodeService.incrementView(id);
        return ResponseEntity.ok(new ApiResponse(true, "OK"));
    }
}
