package bekhruz.com.cinemora.entity;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "content_type")
@Builder
public class ContentType extends Auditable {

    private String name;

    private String slug;
}
