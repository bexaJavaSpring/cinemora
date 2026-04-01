package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "resource")
public class Resource extends Auditable{

    @Column(name = "object_id")
    private String objectId;

    private String contentType;

    private Long size;

    private String viewUrl;

}
