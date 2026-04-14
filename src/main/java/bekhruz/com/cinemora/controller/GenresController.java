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


    @GetMapping
    public ResponseEntity<List<GenreResponse>> getAll() {
        return ResponseEntity.ok(genreService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(genreService.getById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<GenreResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(genreService.getBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<GenreResponse> create(@RequestBody GenreRequest request) {
        return ResponseEntity.status(201).body(genreService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> update(
            @PathVariable UUID id,
            @RequestBody GenreRequest request
    ) {
        return ResponseEntity.ok(genreService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        genreService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Janr o'chirildi"));
    }
}
