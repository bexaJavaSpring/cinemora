package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.slider.SliderRequest;
import bekhruz.com.cinemora.dto.slider.SliderResponse;
import bekhruz.com.cinemora.entity.Content;
import bekhruz.com.cinemora.entity.Slider;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.ContentRepository;
import bekhruz.com.cinemora.repository.SliderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SliderService {
    private final SliderRepository sliderRepository;
    private final ContentRepository contentRepository;

    // ── Faol sliderlar ────────────────────────────────────
    public List<SliderResponse> getActive() {
        return sliderRepository
                .findByIsActiveTrueOrderBySortOrderAsc()
                .stream().map(this::toResponse).toList();
    }

    // ── Barchasi (Admin) ──────────────────────────────────
    public List<SliderResponse> getAll() {
        return sliderRepository
                .findAllByOrderBySortOrderAsc()
                .stream().map(this::toResponse).toList();
    }

    // ── ID bo'yicha ───────────────────────────────────────
    public SliderResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    // ── Yaratish ─────────────────────────────────────────
    @Transactional
    public SliderResponse create(SliderRequest req) {
        Slider slider = Slider.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .imageUrl(req.getImageUrl())
                .externalUrl(req.getExternalUrl())
                .isActive(true)
                .sortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0)
                .build();

        // Kontent bilan bog'lash (ixtiyoriy)
        if (req.getContentId() != null) {
            Content content = contentRepository.findById(req.getContentId())
                    .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + req.getContentId()));
            slider.setContent(content);
        }

        return toResponse(sliderRepository.save(slider));
    }

    // ── Tahrirlash ────────────────────────────────────────
    @Transactional
    public SliderResponse update(UUID id, SliderRequest req) {
        Slider slider = findById(id);

        slider.setTitle(req.getTitle());
        slider.setDescription(req.getDescription());
        slider.setExternalUrl(req.getExternalUrl());
        if (req.getSortOrder() != null) slider.setSortOrder(req.getSortOrder());
        if (req.getImageUrl() != null) slider.setImageUrl(req.getImageUrl());

        // Kontent o'zgartirish
        if (req.getContentId() != null) {
            Content content = contentRepository.findById(req.getContentId())
                    .orElseThrow(() -> new GenericNotFoundException("Kontent topilmadi: " + req.getContentId()));
            slider.setContent(content);
        } else {
            slider.setContent(null);
        }

        return toResponse(sliderRepository.save(slider));
    }

    // ── O'chirish ─────────────────────────────────────────
    @Transactional
    public void delete(UUID id) {
        sliderRepository.deleteById(id);
    }

    // ── Faol/Nofaol ───────────────────────────────────────
    @Transactional
    public boolean toggleActive(UUID id) {
        Slider slider = findById(id);
        slider.setIsActive(!slider.getIsActive());
        return slider.getIsActive();
    }

    // ── Tartibni o'zgartirish ─────────────────────────────
    @Transactional
    public void updateSort(UUID id, Integer order) {
        Slider slider = findById(id);
        slider.setSortOrder(order);
    }

    // ── Helper ────────────────────────────────────────────
    private Slider findById(UUID id) {
        return sliderRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Slider topilmadi: " + id));
    }

    private SliderResponse toResponse(Slider s) {
        SliderResponse res = new SliderResponse();
        res.setId(s.getId());
        res.setTitle(s.getTitle());
        res.setDescription(s.getDescription());
        res.setImageUrl(s.getImageUrl());        // Minio objectName
        res.setExternalUrl(s.getExternalUrl());
        res.setIsActive(s.getIsActive());
        res.setSortOrder(s.getSortOrder());
        if (s.getContent() != null) {
            res.setContentId(s.getContent().getId());
            res.setContentSlug(s.getContent().getSlug());
            res.setContentTitle(s.getContent().getTitle());
        }
        return res;
    }
}
