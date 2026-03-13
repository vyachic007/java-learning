package by.slava_borisov.hoteladmin.service;

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
import by.slava_borisov.hoteladmin.util.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookingManager {

    private final RoomDao roomDao;
    private final BookingDao bookingDao;
    private final GuestDao guestDao;
    private final AmenityDao amenityDao;
    private final AmenityUsageDao amenityUsageDao;


    public Booking checkIn(Guest guest, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        log.info("Начало заселения: гость={}, комната id={}, даты {}-{}",
                guest != null ? guest.getFullName() : "null", roomId, checkInDate, checkOutDate);

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

        Optional<Room> roomOpt = roomDao.findById(roomId);
        if (roomOpt.isEmpty()) {
            log.error("Ошибка заселения для гостя {}: комната id={} не найдена",
                    guest.getFullName(), roomId);
            throw new RoomNotFoundException(roomId);
        }
        Room room = roomOpt.get();

        if (bookingDao.existsOverlapping(roomId, checkInDate, checkOutDate)) {
            log.error("Ошибка заселения для гостя {}: комната id={} занята на период {}-{}",
                    guest.getFullName(), roomId, checkInDate, checkOutDate);
            throw new RoomNotAvailableException(roomId);
        }

        if (guest.isNew()) {
            guest = guestDao.create(guest);
            log.debug("Создан новый гость с id={}", guest.getId());
        }

        Booking created = bookingDao.create(new Booking(guest, room, checkInDate, checkOutDate));

        roomDao.updateStatus(roomId, RoomStatus.OCCUPIED);

        log.info("Гость {} заселён в комнату id={}, бронирование id={}",
                guest.getFullName(), roomId, created.getId());

        return created;
    }

    public Boolean checkOut(Long roomId) {
        log.info("Начало транзакции выселения из комнаты id={}", roomId);

        log.debug("Транзакция начата для выселения из комнаты id={}", roomId);

        Optional<Room> roomOpt = roomDao.findById(roomId);
        if (roomOpt.isEmpty()) {
            log.error("Ошибка транзакции выселения: комната id={} не найдена", roomId);
            throw new RoomNotFoundException(roomId);
        }

        LocalDate today = LocalDate.now();

        Optional<Booking> activeOpt = bookingDao.findActiveByRoomId(roomId, today);
        if (activeOpt.isEmpty()) {
            log.error("Ошибка транзакции выселения: активное бронирование для комнаты id={} не найдено", roomId);
            throw new BookingNotFoundException(roomId);
        }

        Booking activeBooking = activeOpt.get();
        log.debug("Найдено активное бронирование id={} для комнаты id={}", activeBooking.getId(), roomId);

        bookingDao.updateActualCheckOutDate(activeBooking.getId(), today);
        roomDao.updateStatus(roomId, RoomStatus.AVAILABLE);

        log.info("Транзакция выселения успешно завершена: комната id={} освобождена, бронирование id={} закрыто",
                roomId, activeBooking.getId());
        return true;
    }


    public AmenityUsage addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity) {
        log.info("Начало добавления услуги id={} гостю id={}, количество={}, дата={}",
                amenityId, guestId, quantity, usageDate);

        if (usageDate == null) {
            usageDate = LocalDate.now();
            log.debug("Дата использования не указана, установлена текущая: {}", usageDate);
        }

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

        log.debug("Найдено активное бронирование id={} для гостя id={}", booking.getId(), guestId);

        AmenityUsage created = amenityUsageDao.create(
                new AmenityUsage(usageDate, quantity, amenity, booking)
        );

        log.info("Услуга id={} добавлена гостю id={}, usage id={}",
                amenityId, guestId, created.getId());

        return created;
    }
}