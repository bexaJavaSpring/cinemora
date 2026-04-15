package bekhruz.com.cinemora.dto.payment;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class PaymentResponse {
    private UUID id;
    private UUID tariffId;
    private String tariffName;
    private BigDecimal amount;
    private String period;           // "MONTHLY" | "SIX_MONTH" | "YEARLY"
    private String provider;         // "CLICK" | "PAYME" | "UZUM"
    private String status;           // "PENDING" | "CONFIRMED" | "FAILED" | "CANCELLED"
    private String cardLastFour;     // "1234"
    private String phoneNumber;      // "+99890***4567" (yashirilgan)
    private LocalDateTime subscriptionStart;
    private LocalDateTime subscriptionEnd;
    private LocalDateTime createdAt;
}
