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
import by.slava_borisov.hoteladmin.db.HibernateUtil;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.exception.BookingNotFoundException;
import by.slava_borisov.hoteladmin.exception.GuestNotFoundException;
import by.slava_borisov.hoteladmin.exception.InvalidDateRangeException;
import by.slava_borisov.hoteladmin.exception.RoomNotFoundException;
import by.slava_borisov.hoteladmin.exception.RoomNotAvailableException;
import by.slava_borisov.hoteladmin.util.Messages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BookingManager {

    private static final Logger log = LoggerFactory.getLogger(BookingManager.class);

    private final RoomDao roomDao;
    private final BookingDao bookingDao;
    private final GuestDao guestDao;
    private final AmenityDao amenityDao;
    private final AmenityUsageDao amenityUsageDao;


    public Result<Booking> checkIn(Guest guest, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        log.info("Начало транзакции заселения: гость={}, комната id={}, даты {}-{}",
                guest != null ? guest.getFullName() : "null", roomId, checkInDate, checkOutDate);

        try {
            if (guest == null) {
                log.error("Ошибка транзакции заселения: гость не указан");
                return Result.failure(Messages.GUEST_NOT_FOUND_EXCEPTION);
            }

            if (checkInDate == null || checkOutDate == null) {
                log.error("Ошибка транзакции заселения для гостя {}: даты не указаны", guest.getFullName());
                throw new InvalidDateRangeException(Messages.INVALID_DATE_RANGE);
            }

            if (checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now())) {
                log.error("Ошибка транзакции заселения для гостя {}: некорректный диапазон дат {}-{}",
                        guest.getFullName(), checkInDate, checkOutDate);
                throw new InvalidDateRangeException(Messages.INVALID_DATE_RANGE);
            }

            HibernateUtil.beginTransaction();
            log.debug("Транзакция начата для заселения гостя {}", guest.getFullName());

            Optional<Room> roomOpt = roomDao.findById(roomId);
            if (roomOpt.isEmpty()) {
                HibernateUtil.rollback();
                log.error("Ошибка транзакции заселения для гостя {}: комната id={} не найдена",
                        guest.getFullName(), roomId);
                throw new RoomNotFoundException(roomId);
            }

            Room room = roomOpt.get();

            if (bookingDao.existsOverlapping(roomId, checkInDate, checkOutDate)) {
                HibernateUtil.rollback();
                log.error("Ошибка транзакции заселения для гостя {}: комната id={} занята на период {}-{}",
                        guest.getFullName(), roomId, checkInDate, checkOutDate);
                throw new RoomNotAvailableException(roomId);
            }

            if (guest.isNew()) {
                guest = guestDao.create(guest);
                log.debug("Создан новый гость с id={}", guest.getId());
            }

            Booking created = bookingDao.create(new Booking(
                    guest,
                    room,
                    checkInDate,
                    checkOutDate
            ));

            roomDao.updateStatus(roomId, RoomStatus.OCCUPIED);

            HibernateUtil.commit();
            log.info("Транзакция заселения успешно завершена: гость {} заселён в комнату id={}, бронирование id={}",
                    guest.getFullName(), roomId, created.getId());
            return Result.success(created);
        } catch (InvalidDateRangeException | RoomNotFoundException | RoomNotAvailableException e) {
            HibernateUtil.rollback();
            log.error("Ошибка транзакции заселения для гостя {}: {}",
                    guest != null ? guest.getFullName() : "null", e.getMessage());
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            HibernateUtil.rollback();
            log.error("Ошибка БД при транзакции заселения для гостя {}: {}",
                    guest != null ? guest.getFullName() : "null", e.getMessage(), e);
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            HibernateUtil.closeSession();
        }
    }


    public Result<Boolean> checkOut(Long roomId) {
        log.info("Начало транзакции выселения из комнаты id={}", roomId);

        try {
            HibernateUtil.beginTransaction();
            log.debug("Транзакция начата для выселения из комнаты id={}", roomId);

            Optional<Room> roomOpt = roomDao.findById(roomId);
            if (roomOpt.isEmpty()) {
                HibernateUtil.rollback();
                log.error("Ошибка транзакции выселения: комната id={} не найдена", roomId);
                throw new RoomNotFoundException(roomId);
            }

            LocalDate today = LocalDate.now();

            Optional<Booking> activeOpt = bookingDao.findActiveByRoomId(roomId, today);
            if (activeOpt.isEmpty()) {
                HibernateUtil.rollback();
                log.error("Ошибка транзакции выселения: активное бронирование для комнаты id={} не найдено", roomId);
                throw new BookingNotFoundException(roomId);
            }

            Booking activeBooking = activeOpt.get();
            log.debug("Найдено активное бронирование id={} для комнаты id={}", activeBooking.getId(), roomId);

            bookingDao.updateActualCheckOutDate(activeBooking.getId(), today);
            roomDao.updateStatus(roomId, RoomStatus.AVAILABLE);

            HibernateUtil.commit();
            log.info("Транзакция выселения успешно завершена: комната id={} освобождена, бронирование id={} закрыто",
                    roomId, activeBooking.getId());
            return Result.success(true);
        } catch (BookingNotFoundException | RoomNotFoundException e) {
            HibernateUtil.rollback();
            log.error("Ошибка транзакции выселения из комнаты id={}: {}", roomId, e.getMessage());
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            HibernateUtil.rollback();
            log.error("Ошибка БД при транзакции выселения из комнаты id={}: {}", roomId, e.getMessage(), e);
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            HibernateUtil.closeSession();
        }
    }


    public Result<AmenityUsage> addAmenityToGuest(Long guestId, Long amenityId, LocalDate usageDate, int quantity) {
        log.info("Начало транзакции добавления услуги id={} гостю id={}, количество={}, дата={}",
                amenityId, guestId, quantity, usageDate);

        try {
            if (usageDate == null) {
                usageDate = LocalDate.now();
                log.debug("Дата использования не указана, установлена текущая: {}", usageDate);
            }

            if (quantity <= 0) {
                log.error("Ошибка транзакции добавления услуги id={} гостю id={}: некорректное количество {}",
                        amenityId, guestId, quantity);
                return Result.failure(Messages.ERROR_PREFIX + Messages.QUANTITY_MUST_BE_POSITIVE);
            }

            HibernateUtil.beginTransaction();
            log.debug("Транзакция начата для добавления услуги id={} гостю id={}", amenityId, guestId);

            Optional<Guest> guestOpt = guestDao.findById(guestId);
            if (guestOpt.isEmpty()) {
                HibernateUtil.rollback();
                log.error("Ошибка транзакции добавления услуги: гость id={} не найден", guestId);
                throw new GuestNotFoundException(guestId);
            }

            Optional<Amenity> amenityOpt = amenityDao.findById(amenityId);
            if (amenityOpt.isEmpty()) {
                HibernateUtil.rollback();
                log.error("Ошибка транзакции добавления услуги: услуга id={} не найдена", amenityId);
                throw new AmenityNotFoundException(amenityId);
            }

            Amenity amenity = amenityOpt.get();

            LocalDate today = LocalDate.now();
            Optional<Booking> activeBookingOpt = bookingDao.findActiveByGuestId(guestId, today);
            if (activeBookingOpt.isEmpty()) {
                HibernateUtil.rollback();
                log.error("Ошибка транзакции добавления услуги: активное бронирование для гостя id={} не найдено", guestId);
                return Result.failure(Messages.BOOKING_NOT_FOUND_EXCEPTION);
            }

            Booking booking = activeBookingOpt.get();
            log.debug("Найдено активное бронирование id={} для гостя id={}", booking.getId(), guestId);

            AmenityUsage created = amenityUsageDao.create(new AmenityUsage(
                    usageDate,
                    quantity,
                    amenity,
                    booking
            ));

            HibernateUtil.commit();
            log.info("Транзакция добавления услуги успешно завершена: услуга id={} добавлена гостю id={}, usage id={}",
                    amenityId, guestId, created.getId());
            return Result.success(created);
        } catch (GuestNotFoundException | AmenityNotFoundException e) {
            HibernateUtil.rollback();
            log.error("Ошибка транзакции добавления услуги id={} гостю id={}: {}",
                    amenityId, guestId, e.getMessage());
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            HibernateUtil.rollback();
            log.error("Ошибка БД при транзакции добавления услуги id={} гостю id={}: {}",
                    amenityId, guestId, e.getMessage(), e);
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            HibernateUtil.closeSession();
        }
    }
}
