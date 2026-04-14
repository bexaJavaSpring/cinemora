package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.country.CountryRequest;
import bekhruz.com.cinemora.dto.country.CountryResponse;
import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService service) {
        this.countryService = service;
    }

    @GetMapping
    public ResponseEntity<List<CountryResponse>> getAll() {
        return ResponseEntity.ok(countryService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(countryService.getById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CountryResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(countryService.getByCode(code));
    }


    @PostMapping
    public ResponseEntity<CountryResponse> create(@RequestBody CountryRequest request) {
        return ResponseEntity.status(201).body(countryService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> update(
            @PathVariable UUID id,
            @RequestBody CountryRequest request
    ) {
        return ResponseEntity.ok(countryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        countryService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Mamlakat o'chirildi"));
    }
}
