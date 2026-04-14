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

    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<SeasonResponse>> getByContent(@PathVariable UUID contentId) {
        return ResponseEntity.ok(seasonService.getByContent(contentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeasonResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(seasonService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SeasonResponse> create(@RequestBody SeasonRequest request) {
        return ResponseEntity.status(201).body(seasonService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeasonResponse> update(
            @PathVariable UUID id,
            @RequestBody SeasonRequest request
    ) {
        return ResponseEntity.ok(seasonService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        seasonService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Mavsum o'chirildi"));
    }
}
