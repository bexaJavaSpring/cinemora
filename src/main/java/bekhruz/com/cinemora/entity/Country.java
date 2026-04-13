package bekhruz.com.cinemora.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "countries")
@Builder
public class Country extends Auditable {

    private String name;

    private String code;
}
