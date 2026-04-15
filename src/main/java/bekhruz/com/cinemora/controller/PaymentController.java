package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.payment.*;
import bekhruz.com.cinemora.dto.response.ApiResponse;
import bekhruz.com.cinemora.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/init")
    public ResponseEntity<PaymentResponse> init(
            @Valid @RequestBody PaymentInitRequest request
    ) {
        return ResponseEntity.ok(paymentService.init(request));
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse> confirm(
            @Valid @RequestBody PaymentConfirmRequest request
    ) {
        paymentService.confirm(request);
        return ResponseEntity.ok(new ApiResponse(true,
                "To'lov muvaffaqiyatli amalga oshirildi! Tarif faollashtirildi."));
    }

    @PostMapping("/{paymentId}/resend-sms")
    public ResponseEntity<ApiResponse> resendSms(
            @PathVariable UUID paymentId
    ) {
        paymentService.resendSms(paymentId);
        return ResponseEntity.ok(new ApiResponse(true,
                "Yangi SMS kod yuborildi. 5 daqiqa amal qiladi."));
    }

    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponse> cancel(
            @PathVariable UUID paymentId
    ) {
        paymentService.cancel(paymentId);
        return ResponseEntity.ok(new ApiResponse(true, "To'lov bekor qilindi"));
    }

    @GetMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponse> getStatus(
            @PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.getStatus(paymentId));
    }


    @GetMapping("/my-subscription")
    public ResponseEntity<PaymentResponse> getMySubscription(
    ) {
        return ResponseEntity.ok(paymentService.getMySubscription());
    }

    @GetMapping("/my-history")
    public ResponseEntity<Page<PaymentHistoryResponse>> getMyHistory(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(paymentService.getMyHistory(pageable));
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<PaymentHistoryResponse>> getAllPayments(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false)    String status,
            @RequestParam(required = false)    String provider
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(paymentService.getAllPayments(status, provider, pageable));
    }

    @GetMapping("/admin/user/{userId}")
    public ResponseEntity<Page<PaymentHistoryResponse>> getUserPayments(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(paymentService.getMyHistory(userId, pageable));
    }

    @GetMapping("/admin/stats")
    public ResponseEntity<PaymentStatsResponse> getStats() {
        return ResponseEntity.ok(paymentService.getStats());
    }
}
