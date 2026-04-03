package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dto.AmenityDto;
import by.slava_borisov.hoteladmin.dto.AmenityUsageDto;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.exception.BookingNotFoundException;
import by.slava_borisov.hoteladmin.exception.GuestNotFoundException;
import by.slava_borisov.hoteladmin.mapper.entity.AmenityMapper;
import by.slava_borisov.hoteladmin.mapper.entity.AmenityUsageMapper;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.service.AmenityService;
import by.slava_borisov.hoteladmin.service.QueryService;
import by.slava_borisov.hoteladmin.util.Messages;
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
    private final GuestDao guestDao;
    private final QueryService queryService;
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
    public AmenityDto getAmenityById(Long amenityId) {
        log.info("Поиск услуги по id: {}", amenityId);

        AmenityDto result = amenityDao.findById(amenityId)
                .map(amenityMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Услуга с id={} не найдена", amenityId);
                    return new AmenityNotFoundException(amenityId);
                });

        log.info("Услуга с id={} найдена: {}", amenityId, result.name());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmenityDto> getAmenitiesSortedBy(SortCriteria criteria) {
        log.info("Получение списка услуг, сортировка: {}", criteria);

        List<AmenityDto> result = switch (criteria) {
            case BY_PRICE -> queryService.getAmenitiesSortedByPrice()
                    .stream()
                    .map(amenityMapper::toDto)
                    .toList();
            case BY_NAME -> queryService.getAmenitiesSortedByCategory()
                    .stream()
                    .map(amenityMapper::toDto)
                    .toList();
            case BY_ID -> amenityDao.findAll()
                    .stream()
                    .map(amenityMapper::toDto)
                    .toList();
            default -> throw new IllegalArgumentException("Неверный критерий сортировки: " + criteria);
        };

        log.info("Найдено {} услуг", result.size());
        return result;
    }

    @Override
    @Transactional
    public AmenityUsageDto addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity) {
        log.info("Добавление услуги id={} гостю id={}, количество={}", amenityId, guestId, quantity);

        LocalDate finalUsageDate = usageDate == null ? LocalDate.now() : usageDate;

        if (quantity <= 0) {
            log.error("Ошибка добавления услуги id={} гостю id={}: некорректное количество {}",
                    amenityId, guestId, quantity);
            throw new IllegalArgumentException(Messages.QUANTITY_MUST_BE_POSITIVE);
        }

        Guest guest = guestDao.findById(guestId)
                .orElseThrow(() -> {
                    log.error("Ошибка добавления услуги: гость id={} не найден", guestId);
                    return new GuestNotFoundException(guestId);
                });

        Amenity amenity = amenityDao.findById(amenityId)
                .orElseThrow(() -> {
                    log.error("Ошибка добавления услуги: услуга id={} не найдена", amenityId);
                    return new AmenityNotFoundException(amenityId);
                });

        Booking booking = bookingDao.findActiveByGuestId(guestId, LocalDate.now())
                .orElseThrow(() -> {
                    log.error("Ошибка добавления услуги: активное бронирование для гостя id={} не найдено", guestId);
                    return new BookingNotFoundException(guestId);
                });

        AmenityUsage usage = new AmenityUsage();
        usage.setUsageDate(finalUsageDate);
        usage.setQuantity(quantity);
        usage.setAmenity(amenity);
        usage.setBooking(booking);

        AmenityUsage created = amenityUsageDao.create(usage);

        log.info("Услуга id={} добавлена гостю id={}, usage id={}",
                amenity.getId(), guest.getId(), created.getId());

        return amenityUsageMapper.toDto(created);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmenityUsageDto> getGuestAmenities(Long guestId) {
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