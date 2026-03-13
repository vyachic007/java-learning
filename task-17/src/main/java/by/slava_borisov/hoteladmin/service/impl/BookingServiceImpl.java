package by.slava_borisov.hoteladmin.service.impl;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.exception.BookingNotFoundException;
import by.slava_borisov.hoteladmin.exception.GuestNotFoundException;
import by.slava_borisov.hoteladmin.exception.InvalidDateRangeException;
import by.slava_borisov.hoteladmin.exception.RoomNotFoundException;
import by.slava_borisov.hoteladmin.exception.RoomNotAvailableException;
import by.slava_borisov.hoteladmin.service.BookingService;
import by.slava_borisov.hoteladmin.util.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final RoomDao roomDao;
    private final BookingDao bookingDao;
    private final GuestDao guestDao;
    private final AmenityDao amenityDao;
    private final AmenityUsageDao amenityUsageDao;

    public Booking checkIn(Guest guest, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        log.info("Начало заселения: гость={}, комната id={}, даты {}-{}",
                guest != null ? guest.getFullName() : "null", roomId, checkInDate, checkOutDate);

        validateCheckInInput(guest, checkInDate, checkOutDate);
        Room room = findAndValidateRoom(guest, roomId, checkInDate, checkOutDate);
        guest = saveOrUpdateGuest(guest);
        Booking booking = createBooking(guest, room, checkInDate, checkOutDate);
        updateRoomStatusAfterCheckIn(roomId);

        log.info("Гость {} заселён в комнату id={}, бронирование id={}",
                guest.getFullName(), roomId, booking.getId());

        return booking;
    }

    private void validateCheckInInput(Guest guest, LocalDate checkInDate, LocalDate checkOutDate) {
        if (guest == null) {
            log.error("Ошибка заселения: гость не указан");
            throw new IllegalArgumentException(Messages.GUEST_NOT_FOUND_EXCEPTION);
        }

        if (checkInDate == null || checkOutDate == null) {
            log.error("Ошибка заселения для гостя {}: даты не указаны", guest.getFullName());
            throw new InvalidDateRangeException(Messages.INVALID_DATE_RANGE);
        }

        if (checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now())) {
            log.error("Ошибка заселения для гостя {}: некорректный диапазон дат {}-{}",
                    guest.getFullName(), checkInDate, checkOutDate);
            throw new InvalidDateRangeException(Messages.INVALID_DATE_RANGE);
        }
    }

    private Room findAndValidateRoom(Guest guest, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        Room room = roomDao.findById(roomId)
                .orElseThrow(() -> {
                    log.error("Ошибка заселения для гостя {}: комната id={} не найдена",
                            guest.getFullName(), roomId);
                    return new RoomNotFoundException(roomId);
                });

        if (bookingDao.isOverlappingReservationExists(roomId, checkInDate, checkOutDate)) {
            log.error("Ошибка заселения для гостя {}: комната id={} занята на период {}-{}",
                    guest.getFullName(), roomId, checkInDate, checkOutDate);
            throw new RoomNotAvailableException(roomId);
        }

        return room;
    }

    private Guest saveOrUpdateGuest(Guest guest) {
        if (guest.isNew()) {
            guest = guestDao.create(guest);
            log.debug("Создан новый гость с id={}", guest.getId());
        }
        return guest;
    }

    private Booking createBooking(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        return bookingDao.create(new Booking(guest, room, checkInDate, checkOutDate));
    }

    private void updateRoomStatusAfterCheckIn(Long roomId) {
        roomDao.updateStatus(roomId, RoomStatus.OCCUPIED);
    }

    public Boolean checkOut(Long roomId) {
        log.info("Начало выселения из комнаты id={}", roomId);

        Booking activeBooking = findActiveBookingForCheckOut(roomId);
        releaseRoom(roomId, activeBooking);

        return true;
    }


    private Booking findActiveBookingForCheckOut(Long roomId) {
        roomDao.findById(roomId)
                .orElseThrow(() -> {
                    log.error("Ошибка выселения: комната id={} не найдена", roomId);
                    return new RoomNotFoundException(roomId);
                });

        LocalDate today = LocalDate.now();
        return bookingDao.findActiveByRoomId(roomId, today)
                .orElseThrow(() -> {
                    log.error("Ошибка выселения: активное бронирование для комнаты id={} не найдено", roomId);
                    return new BookingNotFoundException(roomId);
                });
    }

    private void releaseRoom(Long roomId, Booking booking) {
        bookingDao.updateActualCheckOutDate(booking.getId(), LocalDate.now());
        roomDao.updateStatus(roomId, RoomStatus.AVAILABLE);

        log.info("Комната id={} освобождена, бронирование id={} закрыто",
                roomId, booking.getId());
    }

    public AmenityUsage addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity) {
        log.info("Начало добавления услуги id={} гостю id={}, количество={}, дата={}",
                amenityId, guestId, quantity, usageDate);

        LocalDate finalUsageDate = prepareUsageDate(usageDate);
        validateQuantity(guestId, amenityId, quantity);

        Guest guest = findGuestById(guestId);
        Amenity amenity = findAmenityById(amenityId);
        Booking booking = findActiveBookingForGuest(guestId);

        log.debug("Найдено активное бронирование id={} для гостя id={}", booking.getId(), guestId);

        return createAmenityUsage(finalUsageDate, quantity, amenity, booking);
    }

    private LocalDate prepareUsageDate(LocalDate usageDate) {
        if (usageDate == null) {
            LocalDate now = LocalDate.now();
            log.debug("Дата использования не указана, установлена текущая: {}", now);
            return now;
        }
        return usageDate;
    }

    private void validateQuantity(Long guestId, Long amenityId, int quantity) {
        if (quantity <= 0) {
            log.error("Ошибка добавления услуги id={} гостю id={}: некорректное количество {}",
                    amenityId, guestId, quantity);
            throw new IllegalArgumentException(Messages.QUANTITY_MUST_BE_POSITIVE);
        }
    }

    private Guest findGuestById(Long guestId) {
        return guestDao.findById(guestId)
                .orElseThrow(() -> {
                    log.error("Ошибка добавления услуги: гость id={} не найден", guestId);
                    return new GuestNotFoundException(guestId);
                });
    }

    private Amenity findAmenityById(Long amenityId) {
        return amenityDao.findById(amenityId)
                .orElseThrow(() -> {
                    log.error("Ошибка добавления услуги: услуга id={} не найдена", amenityId);
                    return new AmenityNotFoundException(amenityId);
                });
    }

    private Booking findActiveBookingForGuest(Long guestId) {
        return bookingDao.findActiveByGuestId(guestId, LocalDate.now())
                .orElseThrow(() -> {
                    log.error("Ошибка добавления услуги: активное бронирование для гостя id={} не найдено", guestId);
                    return new BookingNotFoundException(guestId);
                });
    }

    private AmenityUsage createAmenityUsage(LocalDate usageDate, int quantity, Amenity amenity, Booking booking) {
        AmenityUsage created = amenityUsageDao.create(
                new AmenityUsage(usageDate, quantity, amenity, booking)
        );

        log.info("Услуга id={} добавлена гостю id={}, usage id={}",
                amenity.getId(), booking.getGuest().getId(), created.getId());

        return created;
    }
}