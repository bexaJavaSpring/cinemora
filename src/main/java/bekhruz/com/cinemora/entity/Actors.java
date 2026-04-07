package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "actors")
@PrimaryKeyJoinColumn(name = "user_id")
public class Actors extends User {

    private LocalDate birthDate;

    private String fullName;

    @Column(name = "photo_url")
    private String photoUrl;
}
