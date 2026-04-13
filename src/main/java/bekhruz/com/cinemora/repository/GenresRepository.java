package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Genres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenresRepository extends JpaRepository<Genres, UUID> {

    List<Genres> findAllByOrderByNameAsc();

    // Slug bo'yicha
    Optional<Genres> findBySlug(String slug);

    // Mavjudligini tekshirish
    boolean existsBySlug(String slug);
    boolean existsByNameIgnoreCase(String name);
}
