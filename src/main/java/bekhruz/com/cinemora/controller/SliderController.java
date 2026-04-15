package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.dto.slider.SliderRequest;
import bekhruz.com.cinemora.dto.slider.SliderResponse;
import bekhruz.com.cinemora.service.SliderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sliders")
public class SliderController {
    private final SliderService sliderService;

    public SliderController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @GetMapping
    public ResponseEntity<List<SliderResponse>> getActive() {
        return ResponseEntity.ok(sliderService.getActive());
    }


    @GetMapping("/all")
    public ResponseEntity<List<SliderResponse>> getAll() {
        return ResponseEntity.ok(sliderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SliderResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(sliderService.getById(id));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SliderResponse> create(
            @RequestPart("data") SliderRequest request
    ) {
        return ResponseEntity.status(201).body(sliderService.create(request));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SliderResponse> update(
            @PathVariable UUID id,
            @RequestPart("data") SliderRequest request) {
        return ResponseEntity.ok(sliderService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        sliderService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Slider o'chirildi"));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ApiResponse> toggleActive(@PathVariable UUID id) {
        boolean active = sliderService.toggleActive(id);
        return ResponseEntity.ok(new ApiResponse(true, active ? "Faollashtirildi" : "O'chirildi"));
    }

    @PatchMapping("/{id}/sort")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateSort(
            @PathVariable UUID id,
            @RequestParam Integer order
    ) {
        sliderService.updateSort(id, order);
        return ResponseEntity.ok(new ApiResponse(true, "Tartib yangilandi"));
    }
}
