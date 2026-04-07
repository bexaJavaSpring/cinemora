package bekhruz.com.cinemora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "file_entity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileEntity extends Auditable implements Serializable {

    @Column(length = 500)
    private String name;

    @Column(length = 25)
    private String extension;

    private Long size;

    private String contentType;

    private String path;

    private String etag;

    private String objectName;

    private String bucketName;

    public String getFileName() {
        return name + "." + extension;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, extension, size, contentType);
    }
}
