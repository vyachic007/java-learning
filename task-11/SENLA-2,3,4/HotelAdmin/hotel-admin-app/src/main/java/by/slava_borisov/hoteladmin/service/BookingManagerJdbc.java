package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.di.Inject;
import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.db.ConnectionManager;
import by.slava_borisov.hoteladmin.db.SqlQueries;
import by.slava_borisov.hoteladmin.exception.InvalidDateRangeException;
import by.slava_borisov.hoteladmin.model.*;
import by.slava_borisov.hoteladmin.util.Messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class BookingManagerJdbc extends BookingManager {

    @Inject
    private ConnectionManager cm;

    @Inject
    private RoomDao roomDao;

    @Inject
    private BookingDao bookingDao;

    @Inject
    private AmenityDao amenityDao;

    @Inject
    private AmenityUsageDao amenityUsageDao;

    @Override
    public Result<AmenityUsage> addAmenityToGuest(int guestId, int amenityId, LocalDate usageDate, int quantity) {
        Connection c = null;
        try {
            if (usageDate == null) usageDate = LocalDate.now();
            if (quantity <= 0) return Result.failure(Messages.ERROR_PREFIX + Messages.QUANTITY_MUST_BE_POSITIVE);

            if (amenityDao.findById(amenityId).isEmpty()) {
                return Result.failure(String.format(Messages.AMENITY_NOT_FOUND, amenityId));
            }

            Optional<Booking> activeOpt = bookingDao.findActiveByGuestId(guestId, LocalDate.now());
            if (activeOpt.isEmpty()) {
                return Result.failure(Messages.BOOKING_NOT_FOUND_EXCEPTION);
            }

            int bookingId = activeOpt.get().getId();

            c = cm.getConnection();
            c.setAutoCommit(false);

            try (PreparedStatement ps = c.prepareStatement(
                    SqlQueries.AMENITY_USAGE_INSERT, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, amenityId);
                ps.setInt(2, bookingId);
                ps.setObject(3, usageDate);
                ps.setInt(4, quantity);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException(Messages.AMENITY_USAGE_INSERT_FAILED);
                    int id = keys.getInt(1);

                    c.commit();

                    AmenityUsage created = new AmenityUsage(id, amenityId, bookingId, usageDate, quantity);
                    return Result.success(created);
                }
            }

        } catch (SQLException e) {
            if (c != null) {
                try { c.rollback(); } catch (SQLException ignored) {}
            }
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            if (c != null) {
                try { c.setAutoCommit(true); } catch (SQLException ignored) {}
                try { c.close(); } catch (SQLException ignored) {}
            }
        }
    }




    @Override
    public Result<Booking> checkIn(Guest guest, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        if (guest == null) {
            return Result.failure(Messages.GUEST_NOT_FOUND_EXCEPTION);
        }

        try {
            if (checkInDate == null || checkOutDate == null) {
                throw new InvalidDateRangeException(Messages.INVALID_DATE_RANGE);
            }

            if (checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now())) {
                throw new InvalidDateRangeException(Messages.INVALID_DATE_RANGE);
            }

            Optional<Room> roomOpt = roomDao.findById(roomId);
            if (roomOpt.isEmpty()) {
                return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }

            Room room = roomOpt.get();
            if (room.getStatus() != RoomStatus.AVAILABLE) {
                return Result.failure(String.format(Messages.ROOM_OCCUPIED_ON_DATE, roomId, checkInDate));
            }

            if (bookingDao.existsOverlapping(roomId, checkInDate, checkOutDate)) {
                return Result.failure(String.format(Messages.ROOM_OCCUPIED_ON_DATE, roomId, checkInDate));
            }

            Connection c = null;
            try {
                c = cm.getConnection();
                c.setAutoCommit(false);

                int bookingId;

                try (PreparedStatement ps = c.prepareStatement(
                        SqlQueries.BOOKING_INSERT_TRANSACTION, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, guest.getId());
                    ps.setInt(2, roomId);
                    ps.setObject(3, checkInDate);
                    ps.setObject(4, checkOutDate);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException(Messages.BOOKING_INSERT_FAILED);
                        bookingId = keys.getInt(1);
                    }
                }

                try (PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_UPDATE_STATUS)) {
                    ps.setString(1, RoomStatus.OCCUPIED.name());
                    ps.setInt(2, roomId);
                    int updated = ps.executeUpdate();
                    if (updated == 0) throw new SQLException(String.format(Messages.ROOM_NOT_FOUND_ID, roomId));
                }

                c.commit();

                Booking created = new Booking(bookingId, guest.getId(), roomId, checkInDate, checkOutDate, null);
                return Result.success(created);

            } catch (SQLException e) {
                if (c != null) {
                    try { c.rollback(); } catch (SQLException ignored) {}
                }
                return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
            } finally {
                if (c != null) {
                    try { c.setAutoCommit(true); } catch (SQLException ignored) {}
                    try { c.close(); } catch (SQLException ignored) {}
                }
            }

        } catch (InvalidDateRangeException e) {
            return Result.failure(e.getMessage());
        } catch (SQLException e) {
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        }
    }


    @Override
    public Result<Boolean> checkOut(int roomId) {
        Connection c = null;
        try {
            Optional<Room> roomOpt = roomDao.findById(roomId);
            if (roomOpt.isEmpty()) {
                return Result.failure(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }

            LocalDate today = LocalDate.now();

            Optional<Booking> activeOpt = bookingDao.findActiveByRoomId(roomId, today);
            if (activeOpt.isEmpty()) {
                return Result.failure(Messages.BOOKING_NOT_FOUND_EXCEPTION);
            }

            int bookingId = activeOpt.get().getId();

            c = cm.getConnection();
            c.setAutoCommit(false);

            try (PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_UPDATE_ACTUAL_CHECK_OUT_DATE)) {
                ps.setObject(1, today);
                ps.setInt(2, bookingId);
                int updated = ps.executeUpdate();
                if (updated == 0) throw new SQLException(String.format(Messages.BOOKING_NOT_FOUND_ID, bookingId));
            }

            try (PreparedStatement ps = c.prepareStatement(SqlQueries.ROOM_UPDATE_STATUS)) {
                ps.setString(1, RoomStatus.AVAILABLE.name());
                ps.setInt(2, roomId);
                int updated = ps.executeUpdate();
                if (updated == 0) throw new SQLException(String.format(Messages.ROOM_NOT_FOUND_ID, roomId));
            }

            c.commit();
            return Result.success(true);

        } catch (SQLException e) {
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException ignored) {}
            }
            return Result.failure(Messages.DEFAULT_ERROR_MESSAGE);
        } finally {
            if (c != null) {
                try {
                    c.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
                try {
                    c.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
