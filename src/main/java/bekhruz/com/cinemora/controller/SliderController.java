package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.dto.slider.SliderRequest;
import bekhruz.com.cinemora.dto.slider.SliderResponse;
import bekhruz.com.cinemora.service.SliderService;
import org.springframework.http.ResponseEntity;
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

    // ── 1. FAOL SLIDERLAR (hamma ko'ra oladi) ────────────
    // GET /api/v1/sliders
    // Hero slider uchun — sortOrder bo'yicha tartiblangan
    @GetMapping
    public ResponseEntity<List<SliderResponse>> getActive() {
        return ResponseEntity.ok(sliderService.getActive());
    }

    // ── 2. BARCHASI (Admin uchun) ──────────────────────────
    // GET /api/v1/sliders/all
    @GetMapping("/all")
    public ResponseEntity<List<SliderResponse>> getAll() {
        return ResponseEntity.ok(sliderService.getAll());
    }

    // ── 3. ID ORQALI ─────────────────────────────────────
    // GET /api/v1/sliders/2
    @GetMapping("/{id}")
    public ResponseEntity<SliderResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(sliderService.getById(id));
    }

    // ── 4. YANGI SLIDER QO'SHISH (Admin) ──────────────────
    // POST /api/v1/sliders
    // multipart/form-data:
    //   - data (JSON): { title, description, contentId, externalUrl, sortOrder }
    //   - image (file): slider rasmi → Minio ga ketadi
    @PostMapping()
    public ResponseEntity<SliderResponse> create(
            @RequestPart("data") SliderRequest request
    ) {
        return ResponseEntity.status(201).body(sliderService.create(request));
    }

    // ── 5. SLIDER TAHRIRLASH (Admin) ──────────────────────
    // PUT /api/v1/sliders/2
    @PutMapping(value = "/{id}")
    public ResponseEntity<SliderResponse> update(
            @PathVariable UUID id,
            @RequestPart("data") SliderRequest request) {
        return ResponseEntity.ok(sliderService.update(id, request));
    }

    // ── 6. SLIDER O'CHIRISH (Admin) ───────────────────────
    // DELETE /api/v1/sliders/2
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        sliderService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "Slider o'chirildi"));
    }

    // ── 7. FAOL/NOFAOL QILISH (Admin) ─────────────────────
    // PATCH /api/v1/sliders/2/toggle-active
    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ApiResponse> toggleActive(@PathVariable UUID id) {
        boolean active = sliderService.toggleActive(id);
        return ResponseEntity.ok(new ApiResponse(true, active ? "Faollashtirildi" : "O'chirildi"));
    }

    // ── 8. TARTIBNI O'ZGARTIRISH (Admin) ──────────────────
    // PATCH /api/v1/sliders/2/sort?order=3
    @PatchMapping("/{id}/sort")
    public ResponseEntity<ApiResponse> updateSort(
            @PathVariable UUID id,
            @RequestParam Integer order
    ) {
        sliderService.updateSort(id, order);
        return ResponseEntity.ok(new ApiResponse(true, "Tartib yangilandi"));
    }
}
