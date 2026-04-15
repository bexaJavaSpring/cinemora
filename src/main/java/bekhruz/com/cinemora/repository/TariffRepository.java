package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TariffRepository extends JpaRepository<Tariff, UUID> {
    List<Tariff> findByIsActiveTrueOrderBySortOrderAsc();
    List<Tariff> findAllByOrderBySortOrderAsc();
}
