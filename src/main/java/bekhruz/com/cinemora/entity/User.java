package bekhruz.com.cinemora.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends Auditable {

    @Column(unique = true, length = 100)
    private String email;

    @Column(unique = true, length = 50)
    private String username;

    @Column(unique = true, length = 8)
    private String password;

    @Column(length = 150)
    private String fullName;

    @Column(length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;             // USER, ADMIN, MODERATOR

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    // OAuth (Google, Telegram orqali kirish)
    @Column(length = 100)
    private String googleId;

    @Column(length = 100)
    private String telegramId;

    // Sevimlilar
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "content_id")
    )
    @Builder.Default
    private List<Content> favorites = new ArrayList<>();

    // Ko'rish tarixi
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<WatchHistory> watchHistory = new ArrayList<>();

    public enum UserRole {
        USER, MODERATOR, ADMIN
    }
}
