package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.dto.videosource.VideoSourceRequest;
import bekhruz.com.cinemora.dto.videosource.VideoSourceResponse;
import bekhruz.com.cinemora.service.VideoSourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<VideoSourceResponse>> getByContent(@PathVariable UUID contentId) {
        return ResponseEntity.ok(videoSourceService.getByContent(contentId));
    }

    @GetMapping("/episode/{episodeId}")
    public ResponseEntity<List<VideoSourceResponse>> getByEpisode(@PathVariable UUID episodeId) {
        return ResponseEntity.ok(videoSourceService.getByEpisode(episodeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoSourceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(videoSourceService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VideoSourceResponse> create(@RequestBody VideoSourceRequest request) {
        return ResponseEntity.status(201).body(videoSourceService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VideoSourceResponse> update(
            @PathVariable UUID id,
            @RequestBody VideoSourceRequest request
    ) {
        return ResponseEntity.ok(videoSourceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        videoSourceService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Video manbaa o'chirildi"));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ApiResponse> toggleActive(@PathVariable UUID id) {
        boolean active = videoSourceService.toggleActive(id);
        return ResponseEntity.ok(new ApiResponse(true, active ? "Faollashtirildi" : "O'chirildi"));
    }
}
