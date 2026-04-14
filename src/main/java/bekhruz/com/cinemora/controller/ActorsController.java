package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.actors.ActorRequest;
import bekhruz.com.cinemora.dto.actors.ActorResponse;
import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.service.ActorsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/actors")
public class ActorsController {

    private final ActorsService actorService;

    public ActorsController(ActorsService actorService) {
        this.actorService = actorService;
    }


    @GetMapping
    public ResponseEntity<Page<ActorResponse>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());
        return ResponseEntity.ok(actorService.getAll(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ActorResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(actorService.getById(id));
    }


    @GetMapping("/search")
    public ResponseEntity<List<ActorResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(actorService.search(q));
    }


    @PostMapping
    public ResponseEntity<ActorResponse> create(@RequestBody ActorRequest request) {
        return ResponseEntity.status(201).body(actorService.create(request));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ActorResponse> update(
            @PathVariable UUID id,
            @RequestBody ActorRequest request
    ) {
        return ResponseEntity.ok(actorService.update(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        actorService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Aktyor o'chirildi"));
    }
}
