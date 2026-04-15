package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.comment.CommentRequest;
import bekhruz.com.cinemora.dto.comment.CommentResponse;
import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/comments")
@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR','USER')")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/content/{contentId}")
    public ResponseEntity<Page<CommentResponse>> getByContent(
            @PathVariable UUID contentId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(commentService.getByContent(contentId, pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.getById(id));
    }


    @PostMapping
    public ResponseEntity<CommentResponse> create(
            @RequestBody CommentRequest request
    ) {
        return ResponseEntity.status(201).body(commentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable UUID id,
            @RequestBody CommentRequest request
    ) {
        return ResponseEntity.ok(commentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable UUID id
    ) {
        commentService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Izoh o'chirildi"));
    }

    @PatchMapping("/{id}/like")
    public ResponseEntity<ApiResponse> like(@PathVariable UUID id) {
        int count = commentService.like(id);
        return ResponseEntity.ok(new ApiResponse(true, "Likes: " + count));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ApiResponse> toggleActive(@PathVariable UUID id) {
        boolean active = commentService.toggleActive(id);
        return ResponseEntity.ok(new ApiResponse(true, active ? "Faollashtirildi" : "Bloklandi"));
    }
}
