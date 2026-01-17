package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.db.ConnectionManager;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.exception.BookingNotFoundException;
import by.slava_borisov.hoteladmin.exception.GuestNotFoundException;
import by.slava_borisov.hoteladmin.exception.InvalidDateRangeException;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class BookingManager {

    @Inject
    private ConnectionManager cm;

    @Inject
    private RoomDao roomDao;

    @Inject
    private BookingDao bookingDao;

    @Inject
    private GuestDao guestDao;

    @Inject
    private AmenityDao amenityDao;

    @Inject
    private AmenityUsageDao amenityUsageDao;

    private static final Logger log = LoggerFactory.getLogger(BookingManager.class);


    public Result<Booking> checkIn(Guest guest, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
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

            cm.beginTransaction();
            log.debug("Транзакция начата для заселения гостя {}", guest.getFullName());

            Optional<Room> roomOpt = roomDao.findById(roomId);
            if (roomOpt.isEmpty()) {
                cm.rollback();
                log.error("Ошибка транзакции заселения для гостя {}: комната id={} не найдена",
                        guest.getFullName(), roomId);
                return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }

            if (!roomDao.findAvailableOnDate(checkInDate).stream().anyMatch(r -> r.getId() == roomId)) {
                cm.rollback();
                log.error("Ошибка транзакции заселения для гостя {}: комната id={} занята на дату {}",
                        guest.getFullName(), roomId, checkInDate);
                return Result.failure(String.format(Messages.ROOM_OCCUPIED_ON_DATE, roomId, checkInDate));
            }

            if (guest.isNew()) {
                guest = guestDao.create(guest);
                log.debug("Создан новый гость с id={}", guest.getId());
            }

            Booking created = bookingDao.create(new Booking(
                    guest.getId(),
                    roomId,
                    checkInDate,
                    checkOutDate
            ));

            roomDao.updateStatus(roomId, RoomStatus.OCCUPIED);

            cm.commit();
            log.info("Транзакция заселения успешно завершена: гость {} заселён в комнату id={}, бронирование id={}",
                    guest.getFullName(), roomId, created.getId());
            return Result.success(created);
        } catch (InvalidDateRangeException e) {
            cm.rollback();
            log.error("Ошибка транзакции заселения для гостя {}: некорректный диапазон дат - {}",
                    guest != null ? guest.getFullName() : "null", e.getMessage());
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            cm.rollback();
            log.error("Ошибка БД при транзакции заселения для гостя {}: {}",
                    guest != null ? guest.getFullName() : "null", e.getMessage(), e);
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            cm.closeThreadConnection();
        }
    }


    public Result<Boolean> checkOut(int roomId) {
        log.info("Начало транзакции выселения из комнаты id={}", roomId);

        try {
            cm.beginTransaction();
            log.debug("Транзакция начата для выселения из комнаты id={}", roomId);

            Optional<Room> roomOpt = roomDao.findById(roomId);
            if (roomOpt.isEmpty()) {
                cm.rollback();
                log.error("Ошибка транзакции выселения: комната id={} не найдена", roomId);
                return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }

            LocalDate today = LocalDate.now();

            Optional<Booking> activeOpt = bookingDao.findActiveByRoomId(roomId, today);
            if (activeOpt.isEmpty()) {
                cm.rollback();
                log.error("Ошибка транзакции выселения: активное бронирование для комнаты id={} не найдено", roomId);
                throw new BookingNotFoundException(roomId);
            }

            Booking activeBooking = activeOpt.get();
            log.debug("Найдено активное бронирование id={} для комнаты id={}", activeBooking.getId(), roomId);

            bookingDao.updateActualCheckOutDate(activeBooking.getId(), today);
            roomDao.updateStatus(roomId, RoomStatus.AVAILABLE);

            cm.commit();
            log.info("Транзакция выселения успешно завершена: комната id={} освобождена, бронирование id={} закрыто",
                    roomId, activeBooking.getId());
            return Result.success(true);
        } catch (BookingNotFoundException e) {
            cm.rollback();
            log.error("Ошибка транзакции выселения из комнаты id={}: {}", roomId, e.getMessage());
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            cm.rollback();
            log.error("Ошибка БД при транзакции выселения из комнаты id={}: {}", roomId, e.getMessage(), e);
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            cm.closeThreadConnection();
        }
    }


    public Result<AmenityUsage> addAmenityToGuest(int guestId, int amenityId, LocalDate usageDate, int quantity) {
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

            cm.beginTransaction();
            log.debug("Транзакция начата для добавления услуги id={} гостю id={}", amenityId, guestId);

            Optional<Guest> guestOpt = guestDao.findById(guestId);
            if (guestOpt.isEmpty()) {
                cm.rollback();
                log.error("Ошибка транзакции добавления услуги: гость id={} не найден", guestId);
                throw new GuestNotFoundException(guestId);
            }

            Optional<Amenity> amenityOpt = amenityDao.findById(amenityId);
            if (amenityOpt.isEmpty()) {
                cm.rollback();
                log.error("Ошибка транзакции добавления услуги: услуга id={} не найдена", amenityId);
                throw new AmenityNotFoundException(amenityId);
            }

            LocalDate today = LocalDate.now();
            Optional<Booking> activeBookingOpt = bookingDao.findActiveByGuestId(guestId, today);
            if (activeBookingOpt.isEmpty()) {
                cm.rollback();
                log.error("Ошибка транзакции добавления услуги: активное бронирование для гостя id={} не найдено", guestId);
                return Result.failure(Messages.BOOKING_NOT_FOUND_EXCEPTION);
            }

            int bookingId = activeBookingOpt.get().getId();
            log.debug("Найдено активное бронирование id={} для гостя id={}", bookingId, guestId);

            AmenityUsage created = amenityUsageDao.create(new AmenityUsage(
                    amenityId,
                    bookingId,
                    usageDate,
                    quantity
            ));

            cm.commit();
            log.info("Транзакция добавления услуги успешно завершена: услуга id={} добавлена гостю id={}, usage id={}",
                    amenityId, guestId, created.getId());
            return Result.success(created);
        } catch (GuestNotFoundException | AmenityNotFoundException e) {
            cm.rollback();
            log.error("Ошибка транзакции добавления услуги id={} гостю id={}: {}",
                    amenityId, guestId, e.getMessage());
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            cm.rollback();
            log.error("Ошибка БД при транзакции добавления услуги id={} гостю id={}: {}",
                    amenityId, guestId, e.getMessage(), e);
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            cm.closeThreadConnection();
        }
    }
}
