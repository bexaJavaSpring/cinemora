package bekhruz.com.cinemora.dto.tariff;

import bekhruz.com.cinemora.entity.Payment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TariffAttachRequest {
    @NotBlank
    private UUID userId;

    @NotBlank
    private UUID tariffId;

    @NotNull
    private Payment.SubscriptionPeriod period;
}
