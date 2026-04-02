package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.mapper.AmenityMapper;
import by.slava_borisov.hoteladmin.mapper.AmenityUsageMapper;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.service.AmenityService;
import by.slava_borisov.hoteladmin.service.BookingService;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.util.SortCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmenityServiceImpl implements AmenityService {

    private final AmenityDao amenityDao;
    private final AmenityUsageDao amenityUsageDao;
    private final BookingDao bookingDao;
    private final BookingService bookingService;
    private final QueryService queryManager;
    private final AmenityMapper amenityMapper;
    private final AmenityUsageMapper amenityUsageMapper;

    @Override
    @Transactional
    public AmenityDto addAmenity(AmenityDto amenityDto) {
        log.info("Добавление новой услуги: {}", amenityDto.name());
        Amenity amenity = amenityMapper.toEntity(amenityDto);
        Amenity created = amenityDao.create(amenity);
        log.info("Услуга добавлена: id={}, name={}", created.getId(), created.getName());
        return amenityMapper.toDto(created);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmenityDto> getAllAmenities() {
        log.info("Получение списка всех услуг");
        List<AmenityDto> result = amenityDao.findAll()
                .stream()
                .map(amenityMapper::toDto)
                .toList();
        log.info("Найдено {} услуг", result.size());
        return result;
    }

    @Override
    @Transactional
    public void changeAmenityPrice(Long amenityId, BigDecimal newPrice) {
        log.info("Изменение цены услуги id={}, новая цена={}", amenityId, newPrice);
        if (amenityDao.findById(amenityId).isEmpty()) {
            log.error("Услуга с id={} не найдена", amenityId);
            throw new AmenityNotFoundException(amenityId);
        }
        amenityDao.updatePrice(amenityId, newPrice);
        log.info("Цена услуги id={} успешно изменена", amenityId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AmenityDto> findAmenityById(Long amenityId) {
        log.info("Поиск услуги по id: {}", amenityId);
        Optional<AmenityDto> result = amenityDao.findById(amenityId)
                .map(amenityMapper::toDto);

        if (result.isPresent()) {
            log.info("Услуга с id={} найдена: {}", amenityId, result.get().name());
        } else {
            log.warn("Услуга с id={} не найдена", amenityId);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmenityDto> getAmenitiesSortedBy(SortCriteria criteria) {
        log.info("Получение списка услуг, сортировка: {}", criteria);

        List<AmenityDto> result = switch (criteria) {
            case BY_PRICE -> queryManager.getAmenitiesSortedByPrice()
                    .stream()
                    .map(amenityMapper::toDto)
                    .distinct()
                    .toList();
            case BY_NAME -> queryManager.getAmenitiesSortedByCategory()
                    .stream()
                    .map(amenityMapper::toDto)
                    .distinct()
                    .toList();
            default -> amenityDao.findAll()
                    .stream()
                    .map(amenityMapper::toDto)
                    .distinct()
                    .toList();
        };

        log.info("Найдено {} услуг", result.size());
        return result;
    }


    @Override
    @Transactional
    public AmenityUsageDto addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity) {
        log.info("Добавление услуги id={} гостю id={}, количество={}", amenityId, guestId, quantity);
        AmenityUsage usage = bookingService.addAmenityToGuest(guestId, amenityId, usageDate, quantity);
        log.info("Услуга добавлена гостю, usage id={}", usage.getId());
        return amenityUsageMapper.toDto(usage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmenityUsageDto> viewGuestAmenities(Long guestId) {
        log.info("Просмотр услуг гостя id={}", guestId);

        Optional<Booking> activeBookingOpt = bookingDao.findActiveByGuestId(guestId, LocalDate.now());
        if (activeBookingOpt.isEmpty()) {
            log.info("У гостя id={} нет активного бронирования", guestId);
            return List.of();
        }

        Long bookingId = activeBookingOpt.get().getId();
        List<AmenityUsageDto> result = amenityUsageDao.findByBookingId(bookingId)
                .stream()
                .map(amenityUsageMapper::toDto)
                .toList();

        log.info("Найдено {} услуг у гостя id={}", result.size(), guestId);
        return result;
    }
}