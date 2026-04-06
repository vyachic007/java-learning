package by.slava_borisov.hoteladmin.dao.impl;

import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.exception.BookingNotFoundException;
import by.slava_borisov.hoteladmin.model.Booking;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class BookingDaoImpl extends AbstractHibernateDao<Booking, Long> implements BookingDao {

    public BookingDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Booking.class);
    }

    @Override
    public Optional<Booking> findActiveByRoomId(Long roomId, LocalDate date) {
        TypedQuery<Booking> query = session().createQuery(
                "SELECT b FROM Booking b " +
                        "WHERE b.room.id = :roomId " +
                        "AND :date BETWEEN b.checkInDate AND b.checkOutDate " +
                        "AND b.actualCheckOutDate IS NULL",
                Booking.class
        );
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
                Booking.class
        );
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
        log.debug("Дата выезда бронирования id={} обновлена на {}", bookingId, actualCheckOutDate);
    }

    @Override
    public Booking create(Booking booking) {
        Booking created = super.create(booking);
        log.debug("Бронирование для гостя id={} в комнате id={} создано, id={}",
                booking.getGuest().getId(), booking.getRoom().getId(), booking.getId());
        return created;
    }

    @Override
    public Booking update(Booking booking) {
        Booking merged = super.update(booking);
        log.debug("Бронирование id={} обновлено", booking.getId());
        return merged;
    }

    @Override
    public boolean deleteById(Long bookingId) {
        boolean deleted = super.deleteById(bookingId);
        if (deleted) {
            log.debug("Бронирование id={} удалено", bookingId);
        }
        return deleted;
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