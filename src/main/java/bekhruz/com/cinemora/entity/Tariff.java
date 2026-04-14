package bekhruz.com.cinemora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tariffs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tariff extends Auditable {

    @Column(nullable = false, length = 100)
    private String name;            // "Premium", "VIP", "Standart"

    @Column(columnDefinition = "TEXT")
    private String description;     // Tarif haqida ma'lumot

    // Narxlar (so'mda)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal priceMonthly;    // 1 oylik narx

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal priceSixMonth;   // 6 oylik narx

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal priceYearly;     // 1 yillik narx

    // Imkoniyatlar
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean hdAccess;           // HD ko'rish

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean noAds;             // Reklama yo'q

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean premiumContent;    // Premium kontentlar

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer devicesAllowed;    // Nechta qurilmada ko'rish mumkin

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder;
}
