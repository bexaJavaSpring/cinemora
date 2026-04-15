package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.UserPaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface PaymentHistoryRepository extends JpaRepository<UserPaymentHistory, UUID> {
    Page<UserPaymentHistory> findByUser_Id(UUID userId, Pageable pageable);

    @Query("""
                SELECT h FROM UserPaymentHistory h
                WHERE (:status IS NULL OR h.paymentStatus = :status)
                  AND (:provider IS NULL OR h.provider = :provider)
                ORDER BY h.createdAt DESC
            """)
    Page<UserPaymentHistory> findAllWithFilters(
            @Param("status") String status,
            @Param("provider") String provider,
            Pageable pageable
    );

    // Statistika uchun
    @Query("SELECT COALESCE(SUM(h.amount), 0) FROM UserPaymentHistory h WHERE h.paymentStatus = :status")
    BigDecimal sumByStatus(@Param("status") String status);

    @Query("""
                SELECT COALESCE(SUM(h.amount), 0) FROM UserPaymentHistory h
                WHERE h.paymentStatus = :status
                  AND h.createdAt >= :startOfDay
                  AND h.createdAt < :endOfDay
            """)
    BigDecimal sumTodayByStatus(
            @Param("status") String status,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("""
                SELECT COALESCE(SUM(h.amount), 0) FROM UserPaymentHistory h
                WHERE h.paymentStatus = :status
                  AND MONTH(h.createdAt) = MONTH(CURRENT_DATE)
                  AND YEAR(h.createdAt)  = YEAR(CURRENT_DATE)
            """)
    BigDecimal sumMonthByStatus(@Param("status") String status);

    @Query("SELECT COUNT(h) FROM UserPaymentHistory h WHERE h.paymentStatus = :status")
    Long countByStatus(@Param("status") String status);
}
