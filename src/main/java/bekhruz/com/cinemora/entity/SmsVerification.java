package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms_verifications", indexes = {
        @Index(name = "idx_sms_payment_id", columnList = "payment_id"),
        @Index(name = "idx_sms_is_used", columnList = "is_used"),
        @Index(name = "idx_sms_expires_at", columnList = "expires_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsVerification extends Auditable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isUsed;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer attempts;
}
