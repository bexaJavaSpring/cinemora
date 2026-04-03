package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Genres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GenresRepository extends JpaRepository<Genres, UUID> {
}
