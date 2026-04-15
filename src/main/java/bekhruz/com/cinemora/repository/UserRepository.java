package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Tariff;
import bekhruz.com.cinemora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    long countByTariffIsNotNullAndTariffExpireAtAfter(LocalDateTime tariffExpireAtAfter);
}
