package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.config.CustomUserDetails;
import bekhruz.com.cinemora.config.UserSession;
import bekhruz.com.cinemora.dto.payment.*;
import bekhruz.com.cinemora.entity.*;
import bekhruz.com.cinemora.exception.CustomBadRequestException;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final SmsVerificationRepository smsVerificationRepository;
    private final TariffRepository tariffRepository;
    private final UserRepository userRepository;
    private final SmsService smsService;
    private final UserSession userSession;

    // ── 1. TO'LOV BOSHLASH ────────────────────────────────
    @Transactional
    public PaymentResponse init(PaymentInitRequest req) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        Tariff tariff = tariffRepository.findById(req.getTariffId())
                .orElseThrow(() -> new GenericNotFoundException("Tarif topilmadi"));

        if (!tariff.getIsActive())
            throw new CustomBadRequestException("Bu tarif hozirda mavjud emas");

        // Davr bo'yicha summa
        BigDecimal amount = switch (req.getPeriod()) {
            case MONTHLY -> tariff.getPriceMonthly();
            case SIX_MONTH -> tariff.getPriceSixMonth();
            case YEARLY -> tariff.getPriceYearly();
        };

        // Payment yaratish
        Payment payment = Payment.builder()
                .user(user)
                .tariff(tariff)
                .period(req.getPeriod())
                .provider(req.getProvider())
                .amount(amount)
                .paymentStatus(Payment.PaymentStatus.PENDING)
                .cardLastFour(req.getCardNumber()
                        .replaceAll("\\s", "")
                        .substring(req.getCardNumber().replaceAll("\\s", "").length() - 4))
                .cardHolder(req.getCardHolder())
                .phoneNumber(req.getPhoneNumber())
                .build();

        paymentRepository.save(payment);

        // PaymentHistory ga PENDING yozish
        saveHistory(payment, "To'lov boshlandi");

        // SMS kod yaratish va yuborish
        sendSmsCode(payment, req.getPhoneNumber());

        log.info("To'lov boshlandi: userId={}, tariff={}, amount={}",
                user.getId(), tariff.getName(), amount);

        return toPaymentResponse(payment);
    }

    // ── 2. SMS TASDIQLASH ─────────────────────────────────
    @Transactional
    public void confirm(PaymentConfirmRequest req) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        Payment payment = findPayment(req.getPaymentId());

        // Faqat o'z to'lovi
        if (!payment.getUser().getId().equals(user.getId()))
            throw new CustomBadRequestException("Bu to'lov sizniki emas");

        if (payment.getPaymentStatus() != Payment.PaymentStatus.PENDING)
            throw new CustomBadRequestException(
                    "Bu to'lov " + payment.getStatus() + " holatida, tasdiqlab bo'lmaydi");

        // SMS tekshirish
        SmsVerification sms = smsVerificationRepository
                .findByPayment_IdAndIsUsedFalse(payment.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        "Faol SMS kod topilmadi. Qaytadan so'rang"));

        // Muddati o'tganmi?
        if (sms.getExpiresAt().isBefore(LocalDateTime.now())) {
            failPayment(payment, "SMS kod muddati o'tgan");
            throw new CustomBadRequestException(
                    "SMS kod muddati o'tgan. Yangi to'lov boshlang");
        }

        // Urinishlar soni
        if (sms.getAttempts() >= 3) {
            failPayment(payment, "3 martadan ko'p noto'g'ri urinish");
            throw new CustomBadRequestException(
                    "Juda ko'p noto'g'ri urinish. Yangi to'lov boshlang");
        }

        // Kod noto'g'rimi?
        if (!sms.getCode().equals(req.getSmsCode())) {
            sms.setAttempts(sms.getAttempts() + 1);
            smsVerificationRepository.save(sms);
            int left = 3 - sms.getAttempts();
            throw new CustomBadRequestException(
                    "SMS kod noto'g'ri. Yana " + left + " urinish qoldi");
        }

        // ✅ Kod to'g'ri
        sms.setIsUsed(true);
        smsVerificationRepository.save(sms);

        // Obuna muddatini hisoblash
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = switch (payment.getPeriod()) {
            case MONTHLY -> now.plusMonths(1);
            case SIX_MONTH -> now.plusMonths(6);
            case YEARLY -> now.plusYears(1);
        };

        // Payment ni yangilash
        payment.setPaymentStatus(Payment.PaymentStatus.CONFIRMED);
        payment.setSubscriptionStart(now);
        payment.setSubscriptionEnd(expiry);
        paymentRepository.save(payment);

        // User ga tarif berish
        User dbUser = userRepository.findById(user.getId()).orElseThrow();
        dbUser.setTariff(payment.getTariff());
        dbUser.setTariffExpireAt(expiry);
        userRepository.save(dbUser);

        // PaymentHistory ga CONFIRMED yozish
        saveHistory(payment, "To'lov muvaffaqiyatli tasdiqlandi");

        log.info("To'lov tasdiqlandi: userId={}, tariff={}, expiry={}",
                user.getId(), payment.getTariff().getName(), expiry);
    }

    // ── 3. SMS QAYTA YUBORISH ─────────────────────────────
    @Transactional
    public void resendSms(UUID paymentId) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        Payment payment = findPayment(paymentId);

        if (!payment.getUser().getId().equals(user.getId()))
            throw new CustomBadRequestException("Bu to'lov sizniki emas");

        if (payment.getPaymentStatus() != Payment.PaymentStatus.PENDING)
            throw new CustomBadRequestException("Bu to'lov PENDING holatida emas");

        // Eski SMSni bekor qilish
        smsVerificationRepository
                .findByPayment_IdAndIsUsedFalse(payment.getId())
                .ifPresent(sms -> {
                    sms.setIsUsed(true);
                    smsVerificationRepository.save(sms);
                });

        // Yangi kod yuborish
        sendSmsCode(payment, payment.getPhoneNumber());
    }

    // ── 4. BEKOR QILISH ───────────────────────────────────
    @Transactional
    public void cancel(UUID paymentId) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        Payment payment = findPayment(paymentId);

        if (!payment.getUser().getId().equals(user.getId()))
            throw new CustomBadRequestException("Bu to'lov sizniki emas");

        if (payment.getPaymentStatus() != Payment.PaymentStatus.PENDING)
            throw new CustomBadRequestException("Faqat PENDING to'lovlarni bekor qilish mumkin");

        payment.setPaymentStatus(Payment.PaymentStatus.CANCELLED);
        payment.setFailureReason("Foydalanuvchi tomonidan bekor qilindi");
        paymentRepository.save(payment);

        saveHistory(payment, "Foydalanuvchi bekor qildi");
    }

    // ── 5. TO'LOV HOLATI ──────────────────────────────────
    public PaymentResponse getStatus(UUID paymentId) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        User user = currentUser.getUser();
        Payment payment = findPayment(paymentId);
        if (!payment.getUser().getId().equals(user.getId()))
            throw new CustomBadRequestException("Bu to'lov sizniki emas");
        return toPaymentResponse(payment);
    }

    // ── 6. FAOL OBUNA ─────────────────────────────────────
    public PaymentResponse getMySubscription() {
        CustomUserDetails currentUser = userSession.getCurrentUser();

        Payment payment = paymentRepository
                .findTopByUser_IdAndPaymentStatusOrderByCreatedAtDesc(
                        currentUser.getUserId(), Payment.PaymentStatus.CONFIRMED)
                .orElseThrow(() -> new GenericNotFoundException("Faol obuna topilmadi"));

        // Muddati o'tganmi — tekshirish
        if (payment.getSubscriptionEnd() != null &&
                payment.getSubscriptionEnd().isBefore(LocalDateTime.now())) {
            // Tarif muddati o'tgan — user dan olib tashlash
            User dbUser = userRepository.findById(currentUser.getUserId()).orElseThrow();
            dbUser.setTariff(null);
            dbUser.setTariffExpireAt(null);
            userRepository.save(dbUser);
            throw new GenericNotFoundException("Obuna muddati tugagan");
        }

        return toPaymentResponse(payment);
    }

    // ── 7. O'Z TO'LOV TARIXI ──────────────────────────────
    public Page<PaymentHistoryResponse> getMyHistory(UUID userId, Pageable pageable) {
        return paymentHistoryRepository
                .findByUser_Id(userId, pageable)
                .map(this::toHistoryResponse);
    }

    public Page<PaymentHistoryResponse> getMyHistory(Pageable pageable) {
        CustomUserDetails currentUser = userSession.getCurrentUser();
        return paymentHistoryRepository
                .findByUser_Id(currentUser.getUserId(), pageable)
                .map(this::toHistoryResponse);
    }

    // ── 8. ADMIN: BARCHASI ────────────────────────────────
    public Page<PaymentHistoryResponse> getAllPayments(
            String status, String provider, Pageable pageable) {
        return paymentHistoryRepository
                .findAllWithFilters(status, provider, pageable)
                .map(this::toHistoryResponse);
    }

    // ── 9. ADMIN: STATISTIKA ──────────────────────────────
    public PaymentStatsResponse getStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        PaymentStatsResponse stats = new PaymentStatsResponse();
        stats.setTotalRevenue(paymentHistoryRepository.sumByStatus("CONFIRMED"));
        stats.setTodayRevenue(paymentHistoryRepository.sumTodayByStatus("CONFIRMED", start, end));
        stats.setMonthRevenue(paymentHistoryRepository.sumMonthByStatus("CONFIRMED"));
        stats.setTotalConfirmed(paymentHistoryRepository.countByStatus("CONFIRMED"));
        stats.setTotalPending(paymentHistoryRepository.countByStatus("PENDING"));
        stats.setTotalFailed(paymentHistoryRepository.countByStatus("FAILED"));
        stats.setActiveSubscribers(userRepository.countByTariffIsNotNullAndTariffExpireAtAfter(LocalDateTime.now()));
        return stats;
    }

    // ── Helper: SMS kod yaratish va yuborish ──────────────
    private void sendSmsCode(Payment payment, String phone) {
        String code = String.format("%06d", new Random().nextInt(999999));

        SmsVerification sms = SmsVerification.builder()
                .payment(payment)
                .phoneNumber(phone)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .isUsed(false)
                .attempts(0)
                .build();

        smsVerificationRepository.save(sms);

        smsService.send(phone,
                "FreeKino: Tasdiqlash kodi: " + code +
                        ". Kod 5 daqiqa amal qiladi. Hech kimga bermang!");
    }

    // ── Helper: To'lovni FAILED qilish ────────────────────
    private void failPayment(Payment payment, String reason) {
        payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
        payment.setFailureReason(reason);
        paymentRepository.save(payment);
        saveHistory(payment, reason);
    }

    // ── Helper: PaymentHistory yozish ─────────────────────
    private void saveHistory(Payment payment, String note) {
        UserPaymentHistory history = UserPaymentHistory.builder()
                .user(payment.getUser())
                .payment(payment)
                .tariff(payment.getTariff())
                .tariffName(payment.getTariff().getName())
                .amount(payment.getAmount())
                .provider(payment.getProvider())
                .paymentStatus(payment.getPaymentStatus())
                .period(payment.getPeriod())
                .subscriptionStart(payment.getSubscriptionStart())
                .subscriptionEnd(payment.getSubscriptionEnd())
                .note(note)
                .build();
        paymentHistoryRepository.save(history);
    }

    // ── Helper: Payment topish ────────────────────────────
    private Payment findPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new GenericNotFoundException("To'lov topilmadi: " + paymentId));
    }

    // ── Mapper: Payment → Response ────────────────────────
    private PaymentResponse toPaymentResponse(Payment p) {
        PaymentResponse res = new PaymentResponse();
        res.setId(p.getId());
        res.setTariffId(p.getTariff().getId());
        res.setTariffName(p.getTariff().getName());
        res.setAmount(p.getAmount());
        res.setPeriod(p.getPeriod().name());
        res.setProvider(p.getProvider().name());
        res.setStatus(p.getStatus().name());
        res.setCardLastFour(p.getCardLastFour());
        res.setPhoneNumber(maskPhone(p.getPhoneNumber()));
        res.setSubscriptionStart(p.getSubscriptionStart());
        res.setSubscriptionEnd(p.getSubscriptionEnd());
        res.setCreatedAt(p.getCreatedAt());
        return res;
    }

    // ── Mapper: PaymentHistory → Response ─────────────────
    private PaymentHistoryResponse toHistoryResponse(UserPaymentHistory h) {
        PaymentHistoryResponse res = new PaymentHistoryResponse();
        res.setId(h.getId());
        res.setUserId(h.getUser().getId());
        res.setUsername(h.getUser().getUsername());
        res.setTariffName(h.getTariffName());
        res.setAmount(h.getAmount());
        res.setPeriod(h.getPeriod().name());
        res.setProvider(h.getProvider().name());
        res.setStatus(h.getStatus().name());
        res.setSubscriptionStart(h.getSubscriptionStart());
        res.setSubscriptionEnd(h.getSubscriptionEnd());
        res.setNote(h.getNote());
        res.setCreatedAt(h.getCreatedAt());
        return res;
    }

    // ── Helper: Telefon raqamni yashirish ─────────────────
    // +998901234567 → +99890***4567
    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) return phone;
        return phone.substring(0, 6) + "***" + phone.substring(phone.length() - 4);
    }
}
