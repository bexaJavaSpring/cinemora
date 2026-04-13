package bekhruz.com.cinemora.dto.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CountryRequest {
    @NotBlank(message = "Nom bo'sh bo'lishi mumkin emas")
    @Size(max = 100, message = "Nom 100 belgidan oshmasin")
    private String name;   // "Amerika"

    @NotBlank(message = "Kod bo'sh bo'lishi mumkin emas")
    @Size(min = 2, max = 5, message = "Kod 2-5 belgidan iborat bo'lishi kerak")
    private String code;   // "US"
}
