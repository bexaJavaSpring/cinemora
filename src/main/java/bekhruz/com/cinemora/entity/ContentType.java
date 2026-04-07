package bekhruz.com.cinemora.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "content_type")
public class ContentType extends Auditable {

    private String name;

    private String slug;
}
