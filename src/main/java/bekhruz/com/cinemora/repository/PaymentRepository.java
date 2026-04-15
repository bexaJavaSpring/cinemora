package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findTopByUser_IdAndPaymentStatusOrderByCreatedAtDesc(
            UUID userId, Payment.PaymentStatus status);
}
