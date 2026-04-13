package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<Country, UUID> {
    List<Country> findAllByOrderByNameAsc();

    // Code bo'yicha (US, KR, UZ...)
    Optional<Country> findByCodeIgnoreCase(String code);

    // Mavjudligini tekshirish
    boolean existsByCodeIgnoreCase(String code);
    boolean existsByNameIgnoreCase(String name);
}
