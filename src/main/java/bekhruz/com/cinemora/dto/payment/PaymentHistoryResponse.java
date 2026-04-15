package bekhruz.com.cinemora.dto.payment;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class PaymentHistoryResponse {
    private UUID id;
    private UUID userId;
    private String username;
    private String tariffName;
    private BigDecimal amount;
    private String period;
    private String provider;
    private String status;
    private LocalDateTime subscriptionStart;
    private LocalDateTime subscriptionEnd;
    private String note;
    private LocalDateTime createdAt;
}
