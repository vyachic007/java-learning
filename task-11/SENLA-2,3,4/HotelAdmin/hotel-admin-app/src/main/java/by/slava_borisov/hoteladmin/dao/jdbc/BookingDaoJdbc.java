package by.slava_borisov.hoteladmin.dao.jdbc;

import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.db.ConnectionManager;
import by.slava_borisov.hoteladmin.db.SqlQueries;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.util.Messages;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDaoJdbc implements BookingDao {

    private final ConnectionManager cm;

    public BookingDaoJdbc(ConnectionManager cm) {
        this.cm = cm;
    }

    private Booking mapBooking(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int guestId = rs.getInt("guest_id");
        int roomId = rs.getInt("room_id");
        LocalDate checkIn = rs.getObject("check_in_date", LocalDate.class);
        LocalDate checkOut = rs.getObject("check_out_date", LocalDate.class);
        LocalDate actualCheckOut = rs.getObject("actual_check_out_date", LocalDate.class);
        return new Booking(id, guestId, roomId, checkIn, checkOut, actualCheckOut);
    }

    @Override
    public Booking create(Booking booking) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, booking.getGuestId());
            ps.setInt(2, booking.getRoomId());
            ps.setObject(3, booking.getCheckInDate());
            ps.setObject(4, booking.getCheckOutDate());
            ps.setObject(5, booking.getActualCheckOutDate());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new Booking(
                            id,
                            booking.getGuestId(),
                            booking.getRoomId(),
                            booking.getCheckInDate(),
                            booking.getCheckOutDate(),
                            booking.getActualCheckOutDate()
                    );
                }
            }
        }

        throw new SQLException(Messages.FAILED_TO_INSERT_BOOKING_NO_KEY);
    }

    @Override
    public Optional<Booking> findById(Integer id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_FIND_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapBooking(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Booking> findAll() throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<Booking> list = new ArrayList<>();
            while (rs.next()) list.add(mapBooking(rs));
            return list;
        }
    }

    @Override
    public Booking update(Booking booking) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_UPDATE)) {

            ps.setInt(1, booking.getGuestId());
            ps.setInt(2, booking.getRoomId());
            ps.setObject(3, booking.getCheckInDate());
            ps.setObject(4, booking.getCheckOutDate());
            ps.setObject(5, booking.getActualCheckOutDate());
            ps.setInt(6, booking.getId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException(String.format(Messages.BOOKING_NOT_FOUND, booking.getId()));
            }
        }

        return booking;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_DELETE_BY_ID)) {

            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        }
    }

    @Override
    public Optional<Booking> findActiveByRoomId(int roomId, LocalDate date) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_FIND_ACTIVE_BY_ROOM)) {

            ps.setInt(1, roomId);
            ps.setObject(2, date);
            ps.setObject(3, date);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapBooking(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<Booking> findActiveByGuestId(int guestId, LocalDate date) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_FIND_ACTIVE_BY_GUEST)) {

            ps.setInt(1, guestId);
            ps.setObject(2, date);
            ps.setObject(3, date);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapBooking(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean existsOverlapping(int roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_EXISTS_OVERLAPPING)) {

            ps.setInt(1, roomId);
            ps.setObject(2, checkOut);
            ps.setObject(3, checkIn);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Booking> findByRoomId(int roomId) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_FIND_BY_ROOM_ID)) {

            ps.setInt(1, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                List<Booking> list = new ArrayList<>();
                while (rs.next()) list.add(mapBooking(rs));
                return list;
            }
        }
    }

    @Override
    public void updateActualCheckOutDate(int bookingId, LocalDate actualCheckOutDate) throws SQLException {
        try (Connection c = cm.getConnection();
             PreparedStatement ps = c.prepareStatement(SqlQueries.BOOKING_UPDATE_ACTUAL_CHECK_OUT_DATE)) {

            ps.setObject(1, actualCheckOutDate);
            ps.setInt(2, bookingId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException(String.format(Messages.BOOKING_NOT_FOUND, bookingId));
            }
        }
    }

}
