package by.slava_borisov.hoteladmin.dao.hibernate;

import by.slava_borisov.hoteladmin.dao.BookingDao;
import by.slava_borisov.hoteladmin.db.HibernateUtil;
import by.slava_borisov.hoteladmin.model.Booking;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class BookingDaoHibernate implements BookingDao {

    private static final Logger log = LoggerFactory.getLogger(BookingDaoHibernate.class);


    @Override
    public Optional<Booking> findActiveByRoomId(Long roomId, LocalDate date) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();

            TypedQuery<Booking> query = session.createQuery(
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
        } catch (Exception e) {
            log.error("Ошибка при поиске активного бронирования комнаты id={} на дату {}", roomId, date, e);
            throw new SQLException("Ошибка при поиске активного бронирования: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Booking> findActiveByGuestId(Long guestId, LocalDate date) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();

            TypedQuery<Booking> query = session.createQuery(
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
        } catch (Exception e) {
            log.error("Ошибка при поиске активного бронирования гостя id={} на дату {}", guestId, date, e);
            throw new SQLException("Ошибка при поиске активного бронирования: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsOverlapping(Long roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();

            Long count = session.createQuery(
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
        } catch (Exception e) {
            log.error("Ошибка при проверке пересекающихся бронирований для комнаты id={}", roomId, e);
            throw new SQLException("Ошибка при проверке бронирований: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateActualCheckOutDate(Long bookingId, LocalDate actualCheckOutDate) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            Booking booking = session.find(Booking.class, bookingId);
            if (booking == null) {
                HibernateUtil.rollback();
                throw new SQLException("Бронирование с id=" + bookingId + " не найдено");
            }

            booking.setActualCheckOutDate(actualCheckOutDate);
            session.merge(booking);

            HibernateUtil.commit();
            log.debug("Дата выезда бронирования id={} обновлена на {}", bookingId, actualCheckOutDate);
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при обновлении даты выезда бронирования id={}", bookingId, e);
            throw new SQLException("Ошибка при обновлении даты выезда: " + e.getMessage(), e);
        }
    }


    @Override
    public Booking create(Booking booking) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            session.persist(booking);
            HibernateUtil.commit();
            log.debug("Бронирование для гостя id={} в комнате id={} создано, id={}",
                    booking.getGuest().getId(), booking.getRoom().getId(), booking.getId());
            return booking;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при создании бронирования", e);
            throw new SQLException("Ошибка при создании бронирования: " + e.getMessage(), e);
        }
    }


    @Override
    public Optional<Booking> findById(Long bookingId) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            Booking bookingById = session.find(Booking.class, bookingId);
            return Optional.ofNullable(bookingById);
        } catch (Exception e) {
            log.error("Ошибка при поиске бронирования по id={}", bookingId, e);
            throw new SQLException("Ошибка при поиске бронирования: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Booking> findAll() throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            return session.createQuery("SELECT b FROM Booking b", Booking.class)
                    .list();
        } catch (Exception e) {
            log.error("Ошибка при получении всех бронирований", e);
            throw new SQLException("Ошибка при получении бронирований: " + e.getMessage(), e);
        }
    }

    @Override
    public Booking update(Booking booking) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            Booking merged = session.merge(booking);
            HibernateUtil.commit();
            log.debug("Бронирование id={} обновлено", booking.getId());
            return merged;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при обновлении бронирования id={}", booking.getId(), e);
            throw new SQLException("Ошибка при обновлении бронирования: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(Long bookingId) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            Booking bookingForDelete = session.find(Booking.class, bookingId);
            if (bookingForDelete != null) {
                session.remove(bookingForDelete);
                HibernateUtil.commit();
                log.debug("Бронирование id={} удалено", bookingId);
                return true;
            }
            HibernateUtil.commit();
            return false;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при удалении бронирования id={}", bookingId, e);
            throw new SQLException("Ошибка при удалении бронирования: " + e.getMessage(), e);
        }
    }
}
