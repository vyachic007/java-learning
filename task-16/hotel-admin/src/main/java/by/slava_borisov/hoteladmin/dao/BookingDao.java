package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Booking;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public interface BookingDao extends GenericDao<Booking, Long> {

    Optional<Booking> findActiveByRoomId(Long roomId, LocalDate date) throws SQLException;

    Optional<Booking> findActiveByGuestId(Long guestId, LocalDate date) throws SQLException;

    boolean existsOverlapping(Long roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException;

    void updateActualCheckOutDate(Long bookingId, LocalDate actualCheckOutDate) throws SQLException;
}
