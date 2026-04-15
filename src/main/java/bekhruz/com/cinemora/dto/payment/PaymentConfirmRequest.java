package bekhruz.com.cinemora.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentConfirmRequest {
    @NotBlank(message = "PaymentId kiritilishi shart")
    private UUID paymentId;  // UUID

    @NotBlank(message = "SMS kod kiritilishi shart")
    @Size(min = 6, max = 6, message = "SMS kod 6 ta raqamdan iborat bo'lishi kerak")
    @Pattern(regexp = "\\d{6}", message = "SMS kod faqat raqamlardan iborat bo'lishi kerak")
    private String smsCode;
}
