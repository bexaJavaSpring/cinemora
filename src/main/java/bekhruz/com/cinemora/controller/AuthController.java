package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.auth.AuthResponse;
import bekhruz.com.cinemora.dto.auth.MeResponse;
import bekhruz.com.cinemora.dto.auth.RegisterRequest;
import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(authService.login(username, password));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me() {
        return ResponseEntity.ok(authService.getMe());
    }

    // ── 4. LOGOUT ────────────────────────────────────────
    // POST /api/v1/auth/logout
    // JWT stateless bo'lgani uchun frontend token ni o'chiradi
    // Backend tomonda faqat 200 qaytaradi
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        return ResponseEntity.ok(new ApiResponse(true, "Muvaffaqiyatli chiqildi"));
    }
}
