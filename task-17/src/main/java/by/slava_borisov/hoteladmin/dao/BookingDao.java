package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Booking;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface BookingDao extends GenericDao<Booking, Long> {

    Optional<Booking> findActiveByRoomId(Long roomId, LocalDate date);

    Optional<Booking> findActiveByGuestId(Long guestId, LocalDate date);

    boolean existsOverlapping(Long roomId, LocalDate checkIn, LocalDate checkOut);

    void updateActualCheckOutDate(Long bookingId, LocalDate actualCheckOutDate);

    List<Booking> findLastByRoomId(Long roomId, int limit);
}
