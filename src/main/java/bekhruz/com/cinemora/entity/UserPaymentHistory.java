package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_payment_history",
        indexes = {
                @Index(name = "idx_pay_hist_user_id", columnList = "user_id"),
                @Index(name = "idx_pay_hist_payment_id", columnList = "payment_id"),
                @Index(name = "idx_pay_hist_status", columnList = "payment_status"),
                @Index(name = "idx_pay_hist_created_at", columnList = "created_at")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPaymentHistory extends Auditable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Qaysi payment ga tegishli
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    // Qaysi tarif
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

    @Column(nullable = false, length = 100)
    private String tariffName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Payment.PaymentProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Payment.PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Payment.SubscriptionPeriod period;

    private LocalDateTime subscriptionStart;
    private LocalDateTime subscriptionEnd;

    @Column(columnDefinition = "TEXT")
    private String note;
}
