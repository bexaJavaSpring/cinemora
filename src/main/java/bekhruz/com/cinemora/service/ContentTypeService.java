package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.content.ContentTypeRequest;
import bekhruz.com.cinemora.dto.content.ContentTypeResponse;
import bekhruz.com.cinemora.entity.ContentType;
import bekhruz.com.cinemora.exception.AlreadyExistsException;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.ContentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentTypeService {

    private final ContentTypeRepository contentTypeRepository;

    public List<ContentTypeResponse> getAll() {
        return contentTypeRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    public ContentTypeResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    public ContentTypeResponse getBySlug(String slug) {
        return toResponse(
                contentTypeRepository.findBySlug(slug)
                        .orElseThrow(() -> new GenericNotFoundException("Content turi topilmadi: " + slug))
        );
    }

    @Transactional
    public ContentTypeResponse create(ContentTypeRequest req) {
        if (contentTypeRepository.existsBySlug(req.getSlug()))
            throw new AlreadyExistsException("Bu slug band: " + req.getSlug());
        if (contentTypeRepository.existsByNameIgnoreCase(req.getName()))
            throw new AlreadyExistsException("Bu nom band: " + req.getName());

        ContentType ct = ContentType.builder()
                .name(req.getName())
                .slug(req.getSlug())
                .build();
        return toResponse(contentTypeRepository.save(ct));
    }

    @Transactional
    public ContentTypeResponse update(UUID id, ContentTypeRequest req) {
        ContentType ct = findById(id);

        if (!ct.getSlug().equals(req.getSlug()) && contentTypeRepository.existsBySlug(req.getSlug()))
            throw new AlreadyExistsException("Bu slug band: " + req.getSlug());
        if (!ct.getName().equalsIgnoreCase(req.getName()) && contentTypeRepository.existsByNameIgnoreCase(req.getName()))
            throw new AlreadyExistsException("Bu nom band: " + req.getName());

        ct.setName(req.getName());
        ct.setSlug(req.getSlug());
        return toResponse(contentTypeRepository.save(ct));
    }

    @Transactional
    public void delete(UUID id) {
        if (!contentTypeRepository.existsById(id))
            throw new GenericNotFoundException("Content turi topilmadi: " + id);
        contentTypeRepository.deleteById(id);
    }

    private ContentType findById(UUID id) {
        return contentTypeRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Content turi topilmadi: " + id));
    }

    private ContentTypeResponse toResponse(ContentType ct) {
        ContentTypeResponse res = new ContentTypeResponse();
        res.setId(ct.getId());
        res.setName(ct.getName());
        res.setSlug(ct.getSlug());
        return res;
    }
}
