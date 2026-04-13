package bekhruz.com.cinemora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "actors")
@PrimaryKeyJoinColumn(name = "user_id")
@SuperBuilder
public class Actors extends User {

    private String birthYear;

    private String nationality;

    private String fullName;

    @Column(name = "photo_url")
    private String photoUrl;
}
