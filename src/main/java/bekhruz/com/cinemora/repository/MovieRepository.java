package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
}
