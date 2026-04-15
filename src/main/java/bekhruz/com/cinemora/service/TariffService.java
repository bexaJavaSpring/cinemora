package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.tariff.TariffAttachRequest;
import bekhruz.com.cinemora.dto.tariff.TariffRequest;
import bekhruz.com.cinemora.dto.tariff.TariffResponse;
import bekhruz.com.cinemora.entity.Tariff;
import bekhruz.com.cinemora.entity.User;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.repository.TariffRepository;
import bekhruz.com.cinemora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TariffService {
    private final TariffRepository tariffRepository;
    private final UserRepository userRepository;

    public List<TariffResponse> getActive() {
        return tariffRepository.findByIsActiveTrueOrderBySortOrderAsc()
                .stream().map(this::toResponse).toList();
    }

    public List<TariffResponse> getAll() {
        return tariffRepository.findAllByOrderBySortOrderAsc()
                .stream().map(this::toResponse).toList();
    }

    public TariffResponse getById(UUID id) {
        return toResponse(findById(id));
    }


    @Transactional
    public TariffResponse create(TariffRequest req) {
        Tariff tariff = Tariff.builder()
                .name(req.getName())
                .description(req.getDescription())
                .priceMonthly(req.getPriceMonthly())
                .priceSixMonth(req.getPriceSixMonth())
                .priceYearly(req.getPriceYearly())
                .hdAccess(req.getHdAccess() != null ? req.getHdAccess() : true)
                .noAds(req.getNoAds() != null ? req.getNoAds() : false)
                .premiumContent(req.getPremiumContent() != null ? req.getPremiumContent() : false)
                .devicesAllowed(req.getDevicesAllowed() != null ? req.getDevicesAllowed() : 1)
                .isActive(true)
                .sortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0)
                .build();
        return toResponse(tariffRepository.save(tariff));
    }


    @Transactional
    public TariffResponse update(UUID id, TariffRequest req) {
        Tariff tariff = findById(id);
        tariff.setName(req.getName());
        tariff.setDescription(req.getDescription());
        tariff.setPriceMonthly(req.getPriceMonthly());
        tariff.setPriceSixMonth(req.getPriceSixMonth());
        tariff.setPriceYearly(req.getPriceYearly());
        tariff.setHdAccess(req.getHdAccess());
        tariff.setNoAds(req.getNoAds());
        tariff.setPremiumContent(req.getPremiumContent());
        tariff.setDevicesAllowed(req.getDevicesAllowed());
        if (req.getSortOrder() != null) tariff.setSortOrder(req.getSortOrder());
        return toResponse(tariffRepository.save(tariff));
    }


    @Transactional
    public void delete(UUID id) {
        if (!tariffRepository.existsById(id))
            throw new GenericNotFoundException("Tarif topilmadi: " + id);
        tariffRepository.deleteById(id);
    }

    // ── Faol/Nofaol ───────────────────────────────────────
    @Transactional
    public boolean toggleActive(UUID id) {
        Tariff tariff = findById(id);
        tariff.setIsActive(!tariff.getIsActive());
        return tariff.getIsActive();
    }

    // ── Admin: User ga tarif biriktirish ──────────────────
    @Transactional
    public void attachToUser(TariffAttachRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new GenericNotFoundException("Foydalanuvchi topilmadi: " + req.getUserId()));

        Tariff tariff = findById(req.getTariffId());

        // Davr bo'yicha tugash sanasini hisoblash
        LocalDateTime expiry = switch (req.getPeriod()) {
            case MONTHLY   -> LocalDateTime.now().plusMonths(1);
            case SIX_MONTH -> LocalDateTime.now().plusMonths(6);
            case YEARLY    -> LocalDateTime.now().plusYears(1);
        };

        user.setTariff(tariff);
        user.setTariffExpireAt(expiry);
        userRepository.save(user);
    }

    // ── Admin: User dan tarif olib tashlash ───────────────
    @Transactional
    public void detachFromUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GenericNotFoundException("Foydalanuvchi topilmadi: " + userId));
        user.setTariff(null);
        user.setTariffExpireAt(null);
        userRepository.save(user);
    }


    private Tariff findById(UUID id) {
        return tariffRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Tarif topilmadi: " + id));
    }

    public TariffResponse toResponse(Tariff t) {
        TariffResponse res = new TariffResponse();
        res.setId(t.getId());
        res.setName(t.getName());
        res.setDescription(t.getDescription());
        res.setPriceMonthly(t.getPriceMonthly());
        res.setPriceSixMonth(t.getPriceSixMonth());
        res.setPriceYearly(t.getPriceYearly());
        res.setHdAccess(t.getHdAccess());
        res.setNoAds(t.getNoAds());
        res.setPremiumContent(t.getPremiumContent());
        res.setDevicesAllowed(t.getDevicesAllowed());
        res.setIsActive(t.getIsActive());
        res.setSortOrder(t.getSortOrder());
        return res;
    }
}
