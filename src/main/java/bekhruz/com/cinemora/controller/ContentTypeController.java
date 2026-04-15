package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.content.ContentTypeRequest;
import bekhruz.com.cinemora.dto.content.ContentTypeResponse;
import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.service.ContentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/content-types")
@PreAuthorize("hasRole('ADMIN')")
public class ContentTypeController {
    private final ContentTypeService contentTypeService;

    public ContentTypeController(ContentTypeService contentTypeService) {
        this.contentTypeService = contentTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ContentTypeResponse>> getAll() {
        return ResponseEntity.ok(contentTypeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentTypeResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(contentTypeService.getById(id));
    }


    @GetMapping("/slug/{slug}")
    public ResponseEntity<ContentTypeResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(contentTypeService.getBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<ContentTypeResponse> create(@RequestBody ContentTypeRequest request) {
        return ResponseEntity.status(201).body(contentTypeService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentTypeResponse> update(
            @PathVariable UUID id,
            @RequestBody ContentTypeRequest request
    ) {
        return ResponseEntity.ok(contentTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        contentTypeService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Content turi o'chirildi"));
    }
}
