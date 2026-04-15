package bekhruz.com.cinemora.dto.tariff;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TariffResponse {
    private UUID id;
    private String     name;
    private String     description;
    private BigDecimal priceMonthly;
    private BigDecimal priceSixMonth;
    private BigDecimal priceYearly;
    private Boolean    hdAccess;
    private Boolean    noAds;
    private Boolean    premiumContent;
    private Integer    devicesAllowed;
    private Boolean    isActive;
    private Integer    sortOrder;
}
