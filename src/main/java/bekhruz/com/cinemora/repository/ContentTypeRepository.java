package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContentTypeRepository extends JpaRepository<ContentType, UUID> {
    Optional<ContentType> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsByNameIgnoreCase(String name);
}
