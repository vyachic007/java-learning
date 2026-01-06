package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Booking;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingDao extends GenericDao<Booking, Integer> {

    Optional<Booking> findActiveByRoomId(int roomId, LocalDate date) throws SQLException;

    Optional<Booking> findActiveByGuestId(int guestId, LocalDate date) throws SQLException;

    boolean existsOverlapping(int roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException;

    List<Booking> findByRoomId(int roomId) throws SQLException;

    void updateActualCheckOutDate(int bookingId, LocalDate actualCheckOutDate) throws SQLException;


}
