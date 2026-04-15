package bekhruz.com.cinemora.dto.payment;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class PaymentStatsResponse {
    private BigDecimal totalRevenue;       // Jami daromad
    private BigDecimal todayRevenue;       // Bugungi daromad
    private BigDecimal monthRevenue;       // Oylik daromad
    private Long       totalConfirmed;     // Tasdiqlangan to'lovlar
    private Long       totalPending;       // Kutilayotgan to'lovlar
    private Long       totalFailed;        // Muvaffaqiyatsiz to'lovlar
    private Long       activeSubscribers;
}
