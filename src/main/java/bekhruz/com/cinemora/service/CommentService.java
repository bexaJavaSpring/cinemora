package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.config.CustomUserDetails;
import bekhruz.com.cinemora.config.UserSession;
import bekhruz.com.cinemora.dto.comment.CommentRequest;
import bekhruz.com.cinemora.dto.comment.CommentResponse;
import bekhruz.com.cinemora.entity.Comment;
import bekhruz.com.cinemora.entity.Content;
import bekhruz.com.cinemora.entity.User;
import bekhruz.com.cinemora.exception.CustomAccessDeniedException;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.CommentRepository;
import bekhruz.com.cinemora.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ContentRepository contentRepository;
    private final UserSession userSession;

    public Page<CommentResponse> getByContent(UUID contentId, Pageable pageable) {
        return commentRepository
                .findByContent_IdAndParentIsNullAndIsActiveTrue(contentId, pageable)
                .map(this::toResponse);
    }

    public CommentResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    @Transactional
    public CommentResponse create(CommentRequest req) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + req.getContentId()));

        Comment comment = Comment.builder()
                .user(user)
                .content(content)
                .text(req.getText())
                .isActive(true)
                .likesCount(0)
                .build();

        // Javob (reply) bo'lsa — parent o'rnatish
        if (req.getParentId() != null) {
            Comment parent = findById(req.getParentId());
            comment.setParent(parent);
        }

        return toResponse(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponse update(UUID id, CommentRequest req) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        Comment comment = findById(id);

        // O'zi yoki Admin tahrirlashi mumkin
        if (!comment.getUser().getId().equals(user.getId())
                && user.getRole() != User.UserRole.ADMIN) {
            throw new CustomAccessDeniedException("Bu izohni tahrirlash huquqingiz yo'q");
        }

        comment.setText(req.getText());
        return toResponse(commentRepository.save(comment));
    }

    @Transactional
    public void delete(UUID id) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        Comment comment = findById(id);
        if (!comment.getUser().getId().equals(user.getId())
                && user.getRole() != User.UserRole.ADMIN) {
            throw new CustomAccessDeniedException("Bu izohni o'chirish huquqingiz yo'q");
        }
        commentRepository.deleteById(id);
    }


    @Transactional
    public int like(UUID id) {
        Comment comment = findById(id);
        comment.setLikesCount(comment.getLikesCount() + 1);
        return comment.getLikesCount();
    }

    // ── Admin: bloklash / faollashtirish ──────────────────
    @Transactional
    public boolean toggleActive(UUID id) {
        Comment comment = findById(id);
        comment.setIsActive(!comment.getIsActive());
        return comment.getIsActive();
    }


    private Comment findById(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Izoh topilmadi: " + id));
    }

    private CommentResponse toResponse(Comment c) {
        CommentResponse res = new CommentResponse();
        res.setId(c.getId());
        res.setContentId(c.getContent().getId());
        res.setUserId(c.getUser().getId());
        res.setUsername(c.getUser().getUsername());
        res.setAvatarUrl(c.getUser().getAvatarUrl());
        res.setText(c.getText());
        res.setLikesCount(c.getLikesCount());
        res.setIsActive(c.getIsActive());
        res.setParentId(c.getParent() != null ? c.getParent().getId() : null);
        res.setCreatedAt(c.getCreatedAt());

        if (c.getReplies() != null && !c.getReplies().isEmpty()) {
            res.setReplies(
                    c.getReplies().stream()
                            .filter(Comment::getIsActive)
                            .map(this::toResponse)
                            .toList()
            );
        }
        return res;
    }
}
