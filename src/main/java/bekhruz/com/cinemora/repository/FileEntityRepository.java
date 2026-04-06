package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
}
