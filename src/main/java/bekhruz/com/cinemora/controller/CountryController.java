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

    // ── 1. BARCHA MAMLAKATLAR ─────────────────────────────
    // GET /api/v1/countries
    @GetMapping
    public ResponseEntity<List<CountryResponse>> getAll() {
        return ResponseEntity.ok(countryService.getAll());
    }

    // ── 2. ID ORQALI ─────────────────────────────────────
    // GET /api/v1/countries/1
    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(countryService.getById(id));
    }

    // ── 3. CODE ORQALI ───────────────────────────────────
    // GET /api/v1/countries/code/US
    @GetMapping("/code/{code}")
    public ResponseEntity<CountryResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(countryService.getByCode(code));
    }

    // ── 4. YANGI MAMLAKAT QO'SHISH (Admin) ───────────────
    // POST /api/v1/countries
    // Body: { "name": "Amerika", "code": "US" }
    @PostMapping
    public ResponseEntity<CountryResponse> create(@RequestBody CountryRequest request) {
        return ResponseEntity.status(201).body(countryService.create(request));
    }

    // ── 5. MAMLAKAT TAHRIRLASH (Admin) ────────────────────
    // PUT /api/v1/countries/1
    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> update(
            @PathVariable UUID id,
            @RequestBody CountryRequest request
    ) {
        return ResponseEntity.ok(countryService.update(id, request));
    }

    // ── 6. MAMLAKAT O'CHIRISH (Admin) ─────────────────────
    // DELETE /api/v1/countries/1
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        countryService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Mamlakat o'chirildi"));
    }
}
