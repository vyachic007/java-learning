package by.slava_borisov.hoteladmin.dao.impl;

import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.exception.BookingNotFoundException;
import by.slava_borisov.hoteladmin.model.Booking;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookingDaoImpl implements BookingDao {

    private final SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Optional<Booking> findActiveByRoomId(Long roomId, LocalDate date) {
        TypedQuery<Booking> query = session().createQuery(
                "SELECT b FROM Booking b " +
                        "WHERE b.room.id = :roomId " +
                        "AND :date BETWEEN b.checkInDate AND b.checkOutDate " +
                        "AND b.actualCheckOutDate IS NULL",
                Booking.class);
        query.setParameter("date", date);
        query.setParameter("roomId", roomId);
        try {
            Booking booking = query.getSingleResult();
            return Optional.of(booking);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Booking> findActiveByGuestId(Long guestId, LocalDate date) {
        TypedQuery<Booking> query = session().createQuery(
                "SELECT b FROM Booking b " +
                        "WHERE b.guest.id = :guestId " +
                        "AND :date BETWEEN b.checkInDate AND b.checkOutDate " +
                        "AND b.actualCheckOutDate IS NULL",
                Booking.class);
        query.setParameter("guestId", guestId);
        query.setParameter("date", date);

        try {
            Booking booking = query.getSingleResult();
            return Optional.of(booking);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isOverlappingReservationExists(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        Long count = session().createQuery(
                        "SELECT COUNT(b) FROM Booking b " +
                                "WHERE b.room.id = :roomId " +
                                "AND b.checkInDate < :checkOut " +
                                "AND b.checkOutDate > :checkIn " +
                                "AND b.actualCheckOutDate IS NULL",
                        Long.class
                )
                .setParameter("roomId", roomId)
                .setParameter("checkIn", checkIn)
                .setParameter("checkOut", checkOut)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public void updateActualCheckOutDate(Long bookingId, LocalDate actualCheckOutDate) {
        Booking booking = session().find(Booking.class, bookingId);
        if (booking == null) {
            throw new BookingNotFoundException(bookingId);
        }

        booking.setActualCheckOutDate(actualCheckOutDate);
        session().merge(booking);

        log.debug("Дата выезда бронирования id={} обновлена на {}", bookingId, actualCheckOutDate);
    }


    @Override
    public Booking create(Booking booking) {
        session().persist(booking);
        log.debug("Бронирование для гостя id={} в комнате id={} создано, id={}",
                booking.getGuest().getId(), booking.getRoom().getId(), booking.getId());
        return booking;
    }


    @Override
    public Optional<Booking> findById(Long bookingId) {
        Booking bookingById = session().find(Booking.class, bookingId);
        return Optional.ofNullable(bookingById);
    }

    @Override
    public List<Booking> findAll() {
        return session().createQuery("SELECT b FROM Booking b", Booking.class)
                .list();
    }

    @Override
    public Booking update(Booking booking) {
        Booking merged = session().merge(booking);
        log.debug("Бронирование id={} обновлено", booking.getId());
        return merged;
    }

    @Override
    public boolean deleteById(Long bookingId) {
        Booking bookingForDelete = session().find(Booking.class, bookingId);
        if (bookingForDelete != null) {
            session().remove(bookingForDelete);
            log.debug("Бронирование id={} удалено", bookingId);
            return true;
        }
        return false;
    }

    @Override
    public List<Booking> findLastByRoomId(Long roomId, int limit) {
        return session().createQuery("""
                    SELECT b FROM Booking b
                    WHERE b.room.id = :roomId
                    ORDER BY b.checkInDate DESC
                    """, Booking.class)
                .setParameter("roomId", roomId)
                .setMaxResults(Math.max(0, limit))
                .list();
    }
}
