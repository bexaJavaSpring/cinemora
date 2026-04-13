package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.genre.GenreRequest;
import bekhruz.com.cinemora.dto.genre.GenreResponse;
import bekhruz.com.cinemora.entity.Genres;
import bekhruz.com.cinemora.exception.AlreadyExistsException;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.GenresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenresRepository genreRepository;

    // ── Barchasi ─────────────────────────────────────────
    public List<GenreResponse> getAll() {
        return genreRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ── ID bo'yicha ───────────────────────────────────────
    public GenreResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    // ── Slug bo'yicha ─────────────────────────────────────
    public GenreResponse getBySlug(String slug) {
        Genres genre = genreRepository.findBySlug(slug)
                .orElseThrow(() -> new GenericNotFoundException("Janr topilmadi: " + slug));
        return toResponse(genre);
    }

    // ── Yaratish ─────────────────────────────────────────
    @Transactional
    public GenreResponse create(GenreRequest request) {
        // Slug takrorlanmasligini tekshirish
        if (genreRepository.existsBySlug(request.getSlug())) {
            throw new AlreadyExistsException("Bu slug allaqachon mavjud: " + request.getSlug());
        }
        if (genreRepository.existsByNameIgnoreCase(request.getName())) {
            throw new AlreadyExistsException("Bu nom allaqachon mavjud: " + request.getName());
        }

        Genres genre = Genres.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .build();

        return toResponse(genreRepository.save(genre));
    }

    // ── Tahrirlash ────────────────────────────────────────
    @Transactional
    public GenreResponse update(UUID id, GenreRequest request) {
        Genres genre = findById(id);

        // O'zi bundan tashqari slug/name tekshirish
        if (!genre.getSlug().equals(request.getSlug())
                && genreRepository.existsBySlug(request.getSlug())) {
            throw new AlreadyExistsException("Bu slug allaqachon mavjud: " + request.getSlug());
        }
        if (!genre.getName().equalsIgnoreCase(request.getName())
                && genreRepository.existsByNameIgnoreCase(request.getName())) {
            throw new AlreadyExistsException("Bu nom allaqachon mavjud: " + request.getName());
        }

        genre.setName(request.getName());
        genre.setSlug(request.getSlug());

        return toResponse(genreRepository.save(genre));
    }

    // ── O'chirish ─────────────────────────────────────────
    @Transactional
    public void delete(UUID id) {
        if (!genreRepository.existsById(id))
            throw new GenericNotFoundException("Janr topilmadi: " + id);
        genreRepository.deleteById(id);
    }

    // ── Helper metodlar ───────────────────────────────────
    private Genres findById(UUID id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Janr topilmadi: " + id));
    }

    private GenreResponse toResponse(Genres genre) {
        GenreResponse res = new GenreResponse();
        res.setId(genre.getId());
        res.setName(genre.getName());
        res.setSlug(genre.getSlug());
        return res;
    }
}
