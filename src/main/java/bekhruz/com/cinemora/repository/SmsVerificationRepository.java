package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.SmsVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SmsVerificationRepository extends JpaRepository<SmsVerification, UUID> {
    Optional<SmsVerification> findByPayment_IdAndIsUsedFalse(UUID paymentId);

}
