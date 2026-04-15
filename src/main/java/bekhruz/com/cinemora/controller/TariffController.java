package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.dto.tariff.TariffAttachRequest;
import bekhruz.com.cinemora.dto.tariff.TariffRequest;
import bekhruz.com.cinemora.dto.tariff.TariffResponse;
import bekhruz.com.cinemora.service.TariffService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tariffs")
public class TariffController {

    private final TariffService tariffService;

    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @GetMapping
    public ResponseEntity<List<TariffResponse>> getActive() {
        return ResponseEntity.ok(tariffService.getActive());
    }

    @GetMapping("/all")
    public ResponseEntity<List<TariffResponse>> getAll() {
        return ResponseEntity.ok(tariffService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(tariffService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TariffResponse> create(@Valid @RequestBody TariffRequest request) {
        return ResponseEntity.status(201).body(tariffService.create(request));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TariffResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody TariffRequest request
    ) {
        return ResponseEntity.ok(tariffService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        tariffService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Tarif o'chirildi"));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ApiResponse> toggleActive(@PathVariable UUID id) {
        boolean active = tariffService.toggleActive(id);
        return ResponseEntity.ok(new ApiResponse(true,
                active ? "Tarif faollashtirildi" : "Tarif o'chirildi"));
    }

    @PatchMapping("/attach")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> attachToUser(
            @RequestBody TariffAttachRequest request
    ) {
        tariffService.attachToUser(request);
        return ResponseEntity.ok(new ApiResponse(true, "Tarif foydalanuvchiga biriktirildi"));
    }


    @PatchMapping("/detach/{userId}")
    public ResponseEntity<ApiResponse> detachFromUser(@PathVariable UUID userId) {
        tariffService.detachFromUser(userId);
        return ResponseEntity.ok(new ApiResponse(true, "Tarif foydalanuvchidan olib tashlandi"));
    }
}
