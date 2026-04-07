package bekhruz.com.cinemora.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "content_actors")
public class ContentActor extends Auditable {

    private UUID contentId;

    private UUID actorId;

    private String characterName;
}
