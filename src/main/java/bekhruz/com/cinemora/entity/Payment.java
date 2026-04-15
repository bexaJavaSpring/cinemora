package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment", indexes = {
        @Index(name = "idx_payment_user_id", columnList = "user_id"),
        @Index(name = "idx_payment_tariff_id", columnList = "tariff_id"),
        @Index(name = "idx_payment_status", columnList = "payment_status"),
        @Index(name = "idx_payment_created_at", columnList = "created_at")
})
@Builder
public class Payment extends Auditable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Qaysi tarif
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

    // Obuna davri
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionPeriod period;   // MONTHLY, SIX_MONTH, YEARLY

    // To'lov tizimi
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentProvider provider;    // CLICK, PAYME, UZUM

    // Summa
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    // To'lov holati
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;        // PENDING, CONFIRMED, FAILED, CANCELLED

    // Karta ma'lumotlari (faqat oxirgi 4 raqam saqlanadi — xavfsizlik)
    @Column(length = 4)
    private String cardLastFour;         // "4242"

    @Column(length = 50)
    private String cardHolder;           // "BEKHRUZ TOSHMATOV"

    // SMS tasdiqlash uchun telefon raqam
    @Column(length = 20)
    private String phoneNumber;          // "+998901234567"

    // Tashqi to'lov tizimi ID si (Click/Payme/Uzum dan keladi)
    @Column(length = 200)
    private String externalTransactionId;

    // Tarif boshlanish va tugash sanasi
    private LocalDateTime subscriptionStart;
    private LocalDateTime subscriptionEnd;

    // Xato bo'lsa sababi
    @Column(columnDefinition = "TEXT")
    private String failureReason;

    // ── Enumlar ───────────────────────────────────────────
    public enum SubscriptionPeriod {
        MONTHLY,       // 1 oy
        SIX_MONTH,     // 6 oy
        YEARLY         // 1 yil
    }

    public enum PaymentProvider {
        CLICK, PAYME, UZUM
    }

    public enum PaymentStatus {
        PENDING,       // SMS kod yuborildi, kutilmoqda
        CONFIRMED,     // To'lov tasdiqlandi
        FAILED,        // To'lov amalga oshmadi
        CANCELLED      // Bekor qilindi
    }
}
