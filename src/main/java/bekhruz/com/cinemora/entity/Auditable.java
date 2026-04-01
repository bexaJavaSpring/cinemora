package bekhruz.com.cinemora.entity;

import bekhruz.com.cinemora.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdBy", "updatedBy",
                "updatedAt"},
        allowGetters = true
)
public abstract class Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreatedBy
    @JsonIgnore
    @Column(name = "created_by")
    private Long createdBy;

    @LastModifiedBy
    @JsonIgnore
    @Column(name = "updated_by")
    private Long updatedBy;

    @CreationTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    @Column(name = "created_at" /*nullable = false*/)
    private LocalDateTime createdAt;

    @JsonIgnore
    @Setter(value = AccessLevel.PRIVATE)
    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.NEW;

    @JsonIgnore
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void markAsDeleted() {
        this.status = Status.DELETED;
        this.deletedAt = LocalDateTime.now();
    }
}
