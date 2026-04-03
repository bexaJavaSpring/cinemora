package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Actors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActorsRepository extends JpaRepository<Actors, UUID> {
}
