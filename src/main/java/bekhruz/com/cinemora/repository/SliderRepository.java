package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Slider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SliderRepository extends JpaRepository<Slider, UUID> {
    List<Slider> findByIsActiveTrueOrderBySortOrderAsc();

    List<Slider> findAllByOrderBySortOrderAsc();
}
