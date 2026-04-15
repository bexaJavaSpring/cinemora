package bekhruz.com.cinemora.dto.tariff;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TariffRequest {
    @NotBlank(message = "Nom kiritilishi shart")
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull @DecimalMin("0.0")
    private BigDecimal priceMonthly;

    @NotNull @DecimalMin("0.0")
    private BigDecimal priceSixMonth;

    @NotNull @DecimalMin("0.0")
    private BigDecimal priceYearly;

    private Boolean hdAccess;
    private Boolean noAds;
    private Boolean premiumContent;

    @Min(1) @Max(10)
    private Integer devicesAllowed;

    private Integer sortOrder;
}
