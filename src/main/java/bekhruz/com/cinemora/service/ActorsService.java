package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.actors.ActorRequest;
import bekhruz.com.cinemora.dto.actors.ActorResponse;
import bekhruz.com.cinemora.entity.Actors;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.ActorsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ActorsService {
    private final ActorsRepository actorRepository;

    public ActorsService(ActorsRepository actorRepository) {
        this.actorRepository = actorRepository;
    }


    public Page<ActorResponse> getAll(Pageable pageable) {
        return actorRepository.findAll(pageable).map(this::toResponse);
    }

    public ActorResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    public List<ActorResponse> search(String query) {
        return actorRepository
                .findByFullNameContainingIgnoreCaseOrderByFullNameAsc(query)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ActorResponse create(ActorRequest request) {
        Actors actor = Actors.builder()
                .fullName(request.getFullName())
                .photoUrl(request.getPhotoUrl())
                .birthYear(request.getBirthYear())
                .nationality(request.getNationality())
                .build();
        return toResponse(actorRepository.save(actor));
    }

    @Transactional
    public ActorResponse update(UUID id, ActorRequest request) {
        Actors actor = findById(id);
        actor.setFullName(request.getFullName());
        actor.setPhotoUrl(request.getPhotoUrl());
        actor.setBirthYear(request.getBirthYear());
        actor.setNationality(request.getNationality());
        return toResponse(actorRepository.save(actor));
    }

    @Transactional
    public void delete(UUID id) {
        if (!actorRepository.existsById(id))
            throw new GenericNotFoundException("Aktyor topilmadi: " + id);
        actorRepository.deleteById(id);
    }


    private Actors findById(UUID id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Aktyor topilmadi: " + id));
    }

    private ActorResponse toResponse(Actors a) {
        ActorResponse res = new ActorResponse();
        res.setId(a.getId());
        res.setFullName(a.getFullName());
        res.setPhotoUrl(a.getPhotoUrl());
        res.setBirthYear(a.getBirthYear());
        res.setNationality(a.getNationality());
        return res;
    }
}
