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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // ── 1. KONTENT IZOHLARI (sahifalab) ──────────────────
    // GET /api/v1/comments/content/5?page=0&size=10
    // Faqat parent commentlar qaytadi (replies ichida)
    @GetMapping("/content/{contentId}")
    public ResponseEntity<Page<CommentResponse>> getByContent(
            @PathVariable UUID contentId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(commentService.getByContent(contentId, pageable));
    }

    // ── 2. BITTA IZOH ─────────────────────────────────────
    // GET /api/v1/comments/15
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.getById(id));
    }

    // ── 3. IZOH YOZISH (login kerak) ──────────────────────
    // POST /api/v1/comments
    // Body: { "contentId": 5, "text": "Zo'r kino!", "parentId": null }
    // Javob yozish: { "contentId": 5, "text": "Rozi!", "parentId": 15 }
    @PostMapping
    public ResponseEntity<CommentResponse> create(
            @RequestBody CommentRequest request
    ) {
        return ResponseEntity.status(201).body(commentService.create(request));
    }

    // ── 4. IZOH TAHRIRLASH (faqat o'z izohini) ────────────
    // PUT /api/v1/comments/15
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable UUID id,
            @RequestBody CommentRequest request
    ) {
        return ResponseEntity.ok(commentService.update(id, request));
    }

    // ── 5. IZOH O'CHIRISH ─────────────────────────────────
    // DELETE /api/v1/comments/15
    // User o'z izohini, Admin istalgan izohni o'chira oladi
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable UUID id
    ) {
        commentService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Izoh o'chirildi"));
    }

    // ── 6. LIKE BOSISH ────────────────────────────────────
    // PATCH /api/v1/comments/15/like
    @PatchMapping("/{id}/like")
    public ResponseEntity<ApiResponse> like(@PathVariable UUID id) {
        int count = commentService.like(id);
        return ResponseEntity.ok(new ApiResponse(true, "Likes: " + count));
    }

    // ── 7. ADMIN: IZOHNI BLOKLASH ─────────────────────────
    // PATCH /api/v1/comments/15/toggle-active
    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ApiResponse> toggleActive(@PathVariable UUID id) {
        boolean active = commentService.toggleActive(id);
        return ResponseEntity.ok(new ApiResponse(true, active ? "Faollashtirildi" : "Bloklandi"));
    }
}
