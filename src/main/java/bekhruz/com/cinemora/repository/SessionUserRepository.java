package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.SessionUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionUserRepository extends JpaRepository<SessionUser, UUID> {
    SessionUser findByUserId(UUID userId);
}
