package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.di.Inject;
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

    public Result<Booking> checkIn(Guest guest, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        try {
            if (guest == null) {
                return Result.failure(Messages.GUEST_NOT_FOUND_EXCEPTION);
            }

            if (checkInDate == null || checkOutDate == null) {
                throw new InvalidDateRangeException(Messages.INVALID_DATE_RANGE);
            }

            if (checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now())) {
                throw new InvalidDateRangeException(Messages.INVALID_DATE_RANGE);
            }

            cm.beginTransaction();

            Optional<Room> roomOpt = roomDao.findById(roomId);
            if (roomOpt.isEmpty()) {
                cm.rollback();
                return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }

            if (!roomDao.findAvailableOnDate(checkInDate).stream().anyMatch(r -> r.getId() == roomId)) {
                cm.rollback();
                return Result.failure(String.format(Messages.ROOM_OCCUPIED_ON_DATE, roomId, checkInDate));
            }

            if (guest.isNew()) {
                guest = guestDao.create(guest);
            }

            Booking created = bookingDao.create(new Booking(
                    guest.getId(),
                    roomId,
                    checkInDate,
                    checkOutDate
            ));

            roomDao.updateStatus(roomId, RoomStatus.OCCUPIED);

            cm.commit();
            return Result.success(created);

        } catch (InvalidDateRangeException e) {
            cm.rollback();
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            cm.rollback();
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            cm.closeThreadConnection();
        }
    }


    public Result<Boolean> checkOut(int roomId) {
        try {
            cm.beginTransaction();

            Optional<Room> roomOpt = roomDao.findById(roomId);
            if (roomOpt.isEmpty()) {
                cm.rollback();
                return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }

            LocalDate today = LocalDate.now();

            Optional<Booking> activeOpt = bookingDao.findActiveByRoomId(roomId, today);
            if (activeOpt.isEmpty()) {
                cm.rollback();
                throw new BookingNotFoundException(roomId);
            }

            Booking activeBooking = activeOpt.get();

            bookingDao.updateActualCheckOutDate(activeBooking.getId(), today);

            roomDao.updateStatus(roomId, RoomStatus.AVAILABLE);

            cm.commit();
            return Result.success(true);

        } catch (BookingNotFoundException e) {
            cm.rollback();
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            cm.rollback();
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            cm.closeThreadConnection();
        }
    }



    public Result<AmenityUsage> addAmenityToGuest(int guestId, int amenityId, LocalDate usageDate, int quantity) {
        try {
            if (usageDate == null) {
                usageDate = LocalDate.now();
            }
            if (quantity <= 0) {
                return Result.failure(Messages.ERROR_PREFIX + Messages.QUANTITY_MUST_BE_POSITIVE);
            }

            cm.beginTransaction();

            Optional<Guest> guestOpt = guestDao.findById(guestId);
            if (guestOpt.isEmpty()) {
                cm.rollback();
                throw new GuestNotFoundException(guestId);
            }

            Optional<Amenity> amenityOpt = amenityDao.findById(amenityId);
            if (amenityOpt.isEmpty()) {
                cm.rollback();
                throw new AmenityNotFoundException(amenityId);
            }

            LocalDate today = LocalDate.now();
            Optional<Booking> activeBookingOpt = bookingDao.findActiveByGuestId(guestId, today);
            if (activeBookingOpt.isEmpty()) {
                cm.rollback();
                return Result.failure(Messages.BOOKING_NOT_FOUND_EXCEPTION);
            }

            int bookingId = activeBookingOpt.get().getId();

            AmenityUsage created = amenityUsageDao.create(new AmenityUsage(
                    amenityId,
                    bookingId,
                    usageDate,
                    quantity
            ));

            cm.commit();
            return Result.success(created);

        } catch (GuestNotFoundException | AmenityNotFoundException e) {
            cm.rollback();
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            cm.rollback();
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            cm.closeThreadConnection();
        }
    }
}
