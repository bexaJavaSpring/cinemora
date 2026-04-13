package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByContent_IdAndParentIsNullAndIsActiveTrue(
            UUID contentId, Pageable pageable
    );
}
