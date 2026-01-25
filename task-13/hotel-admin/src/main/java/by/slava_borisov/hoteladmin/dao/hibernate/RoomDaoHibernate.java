package by.slava_borisov.hoteladmin.dao.hibernate;

import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.db.HibernateUtil;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.util.Messages;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RoomDaoHibernate implements RoomDao {

    private static final Logger log = LoggerFactory.getLogger(RoomDaoHibernate.class);


    @Override
    public Optional<Room> findByNumber(String number) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            TypedQuery<Room> query = session.createQuery("SELECT r FROM Room r WHERE r.number = :number", Room.class);
            query.setParameter("number", number);

            try {
                Room room = query.getSingleResult();
                return Optional.of(room);
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Ошибка при поиске комнаты по номеру {}", number, e);
            throw new SQLException("Ошибка при поиске комнаты: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Room> findAvailableOnDate(LocalDate date) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();

            return session.createQuery("SELECT r FROM Room r WHERE r.id NOT IN " +
                            "(SELECT b.room.id FROM Booking b WHERE :date BETWEEN b.checkInDate AND b.checkOutDate " +
                            "AND b.actualCheckOutDate IS NULL)", Room.class)
                    .setParameter("date", date).list();
        } catch (Exception e) {
            log.error("Ошибка при поиске свободных комнат на дату {}", date, e);
            throw new SQLException("Ошибка при поиске свободных комнат: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateStatus(Long roomId, RoomStatus status) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            int updated = session.createQuery(
                            "UPDATE Room r SET r.status = :status WHERE r.id = :id")
                    .setParameter("id", roomId)
                    .setParameter("status", status)
                    .executeUpdate();

            HibernateUtil.commit();

            if (updated == 0) {
                throw new SQLException(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }
            log.debug("Статус комнаты id={} обновлен на {}", roomId, status);
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при обновлении статуса комнаты id={}", roomId, e);
            throw new SQLException("Ошибка при обновлении статуса: " + e.getMessage(), e);
        }
    }

    @Override
    public void updatePricePerNight(Long roomId, double newPrice) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            int updated = session.createQuery("UPDATE Room r SET r.pricePerNight = :price WHERE r.id = :id")
                    .setParameter("price", newPrice)
                    .setParameter("id", roomId)
                    .executeUpdate();

            HibernateUtil.commit();

            if (updated == 0) {
                throw new SQLException(String.format(Messages.ROOM_NOT_FOUND, roomId));
            }
            log.debug("Цена комнаты id={} обновлена на {}", roomId, newPrice);
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при обновлении цены комнаты id={}", roomId, e);
            throw new SQLException("Ошибка при обновлении цены: " + e.getMessage(), e);
        }
    }

    @Override
    public Room create(Room room) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            session.persist(room);
            HibernateUtil.commit();
            log.debug("Комната с номером {} создана, id={}", room.getNumber(), room.getId());
            return room;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при создании комнаты", e);
            throw new SQLException("Ошибка при создании комнаты: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Room> findById(Long roomId) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            Room roomById = session.find(Room.class, roomId);
            return Optional.ofNullable(roomById);
        } catch (Exception e) {
            log.error("Ошибка при поиске комнаты по id={}", roomId, e);
            throw new SQLException("Ошибка при поиске комнаты: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Room> findAll() throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            return session.createQuery("SELECT r FROM Room r", Room.class).list();
        } catch (Exception e) {
            log.error("Ошибка при получении всех комнат", e);
            throw new SQLException("Ошибка при получении комнат: " + e.getMessage(), e);
        }
    }

    @Override
    public Room update(Room room) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            Room merged = session.merge(room);
            session.flush();
            HibernateUtil.commit();
            log.debug("Комната id={} обновлена", room.getId());
            return merged;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при обновлении комнаты id={}", room.getId(), e);
            throw new SQLException("Ошибка при обновлении комнаты: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(Long roomId) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            Room roomForDelete = session.find(Room.class, roomId);
            if (roomForDelete != null) {
                session.remove(roomForDelete);
                HibernateUtil.commit();
                log.debug("Комната id={} удалена", roomId);
                return true;
            }
            HibernateUtil.commit();
            return false;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при удалении комнаты id={}", roomId, e);
            throw new SQLException("Ошибка при удалении комнаты: " + e.getMessage(), e);
        }
    }
}
