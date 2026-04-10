package bekhruz.com.cinemora.repository;

import bekhruz.com.cinemora.entity.Content;
import bekhruz.com.cinemora.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContentRepository extends JpaRepository<Content, UUID> {

    Optional<Content> findBySlug(String slug);

    // status bo'yicha (latest, top uchun)
    Page<Content> findByStatus(Status status, Pageable pageable);

    // Kino turi bo'yicha
    Page<Content> findByContentType_SlugAndStatus(
            String typeSlug, Status status, Pageable pageable);

    // Janr bo'yicha (many-to-many)
    Page<Content> findByGenres_SlugAndStatus(
            String genreSlug, Status status, Pageable pageable);

    // Qidiruv (title yoki originalTitle)
    Page<Content> findByTitleContainingIgnoreCaseOrOriginalTitleContainingIgnoreCase(
            String title, String originalTitle, Pageable pageable);

    // Featured (hero slider)
    List<Content> findByIsFeaturedTrueAndStatus(Status status);

    // Yilga qarab
    Page<Content> findByReleaseYearAndStatus(
            Integer year, Status status, Pageable pageable);

    // Mamlakatga qarab
    Page<Content> findByCountry_CodeIgnoreCaseAndStatus(
            String countryCode, Status status, Pageable pageable);

    // O'xshash kontentlar (bir xil tur va janrlar, o'zi bundan tashqari)
    @Query("""
        SELECT DISTINCT c FROM Content c
        JOIN c.genres g
        WHERE c.contentType.id = :typeId
          AND g.id IN :genreIds
          AND c.id <> :excludeId
          AND c.status = :status
        ORDER BY c.imdbRating DESC
    """)
    Page<Content> findSimilar(
            @Param("typeId")    UUID typeId,
            @Param("genreIds")  List<UUID> genreIds,
            @Param("excludeId") UUID excludeId,
            @Param("status")    Status status,
            Pageable pageable);
}
