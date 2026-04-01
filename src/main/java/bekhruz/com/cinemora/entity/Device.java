package bekhruz.com.cinemora.entity;

import bekhruz.com.cinemora.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "devices")
public class Device extends Auditable {

    private String name;

    private LocalDateTime connectedDateTime;

    private LocalDateTime untilDateTime;

}
