package bekhruz.com.cinemora.dto.payment;

import bekhruz.com.cinemora.entity.Payment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentInitRequest {
    @NotBlank(message = "Tarif tanlanishi shart")
    private UUID tariffId;

    @NotNull(message = "Davr tanlanishi shart")
    private Payment.SubscriptionPeriod period;  // MONTHLY | SIX_MONTH | YEARLY

    @NotNull(message = "To'lov tizimi tanlanishi shart")
    private Payment.PaymentProvider provider;   // CLICK | PAYME | UZUM

    @NotBlank(message = "Karta raqami kiritilishi shart")
    @Pattern(regexp = "\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}",
            message = "Karta raqami noto'g'ri formatda")
    private String cardNumber;   // "8600 1234 5678 1234"

    @NotBlank(message = "Karta muddati kiritilishi shart")
    @Pattern(regexp = "\\d{2}/\\d{2}", message = "Format: MM/YY")
    private String cardExpiry;   // "03/26"

    @Size(max = 100)
    private String cardHolder;   // "BEKHRUZ TOSHMATOV" (ixtiyoriy)

    @NotBlank(message = "Telefon raqam kiritilishi shart")
    @Pattern(regexp = "\\+998\\d{9}", message = "Format: +998XXXXXXXXX")
    private String phoneNumber;

}
