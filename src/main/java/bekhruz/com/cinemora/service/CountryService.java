package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.country.CountryRequest;
import bekhruz.com.cinemora.dto.country.CountryResponse;
import bekhruz.com.cinemora.entity.Country;
import bekhruz.com.cinemora.exception.AlreadyExistsException;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<CountryResponse> getAll() {
        return countryRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CountryResponse getById(UUID id) {
        return toResponse(findById(id));
    }

    public CountryResponse getByCode(String code) {
        Country country = countryRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new GenericNotFoundException("Mamlakat topilmadi: " + code));
        return toResponse(country);
    }

    @Transactional
    public CountryResponse create(CountryRequest request) {
        if (countryRepository.existsByCodeIgnoreCase(request.getCode()))
            throw new AlreadyExistsException("Bu kod allaqachon mavjud: " + request.getCode());
        if (countryRepository.existsByNameIgnoreCase(request.getName()))
            throw new AlreadyExistsException("Bu nom allaqachon mavjud: " + request.getName());

        Country country = Country.builder()
                .name(request.getName())
                .code(request.getCode().toUpperCase())
                .build();

        return toResponse(countryRepository.save(country));
    }

    @Transactional
    public CountryResponse update(UUID id, CountryRequest request) {
        Country country = findById(id);

        if (!country.getCode().equalsIgnoreCase(request.getCode())
                && countryRepository.existsByCodeIgnoreCase(request.getCode()))
            throw new AlreadyExistsException("Bu kod allaqachon mavjud: " + request.getCode());

        if (!country.getName().equalsIgnoreCase(request.getName())
                && countryRepository.existsByNameIgnoreCase(request.getName()))
            throw new AlreadyExistsException("Bu nom allaqachon mavjud: " + request.getName());

        country.setName(request.getName());
        country.setCode(request.getCode().toUpperCase());

        return toResponse(countryRepository.save(country));
    }


    @Transactional
    public void delete(UUID id) {
        if (!countryRepository.existsById(id))
            throw new GenericNotFoundException("Mamlakat topilmadi: " + id);
        countryRepository.deleteById(id);
    }


    private Country findById(UUID id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Mamlakat topilmadi: " + id));
    }

    private CountryResponse toResponse(Country c) {
        CountryResponse res = new CountryResponse();
        res.setId(c.getId());
        res.setName(c.getName());
        res.setCode(c.getCode());
        return res;
    }
}
