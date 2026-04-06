package by.slava_borisov.hoteladmin.dao.impl;

import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.exception.RoomNotFoundException;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class RoomDaoImpl extends AbstractHibernateDao<Room, Long> implements RoomDao {

    public RoomDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Room.class);
    }

    @Override
    public Optional<Room> findByNumber(Integer number) {
        TypedQuery<Room> query = session().createQuery(
                "SELECT r FROM Room r WHERE r.number = :number",
                Room.class
        );
        query.setParameter("number", number);

        try {
            Room room = query.getSingleResult();
            return Optional.of(room);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Room> findAvailableOnDate(LocalDate date) {
        return session().createQuery(
                        "SELECT r FROM Room r WHERE r.id NOT IN " +
                                "(SELECT b.room.id FROM Booking b " +
                                "WHERE :date BETWEEN b.checkInDate AND b.checkOutDate " +
                                "AND b.actualCheckOutDate IS NULL)",
                        Room.class
                )
                .setParameter("date", date)
                .list();
    }

    @Override
    public void updateStatus(Long roomId, RoomStatus status) {
        int updated = session().createMutationQuery(
                        "UPDATE Room r SET r.status = :status WHERE r.id = :id")
                .setParameter("id", roomId)
                .setParameter("status", status)
                .executeUpdate();

        if (updated == 0) {
            throw new RoomNotFoundException(roomId);
        }

        log.debug("Статус комнаты id={} обновлен на {}", roomId, status);
    }

    @Override
    public void updatePricePerNight(Long roomId, BigDecimal newPrice) {
        int updated = session().createMutationQuery(
                        "UPDATE Room r SET r.pricePerNight = :price WHERE r.id = :id")
                .setParameter("price", newPrice)
                .setParameter("id", roomId)
                .executeUpdate();

        if (updated == 0) {
            throw new RoomNotFoundException(roomId);
        }

        log.debug("Цена комнаты id={} обновлена на {}", roomId, newPrice);
    }

    @Override
    public Room create(Room room) {
        Room created = super.create(room);
        log.debug("Комната с номером {} создана, id={}", room.getNumber(), room.getId());
        return created;
    }

    @Override
    public Room update(Room room) {
        Room merged = super.update(room);
        log.debug("Комната id={} обновлена", room.getId());
        return merged;
    }

    @Override
    public boolean deleteById(Long roomId) {
        boolean deleted = super.deleteById(roomId);
        if (deleted) {
            log.debug("Комната id={} удалена", roomId);
        }
        return deleted;
    }

    @Override
    public int countAvailable() {
        Long count = session().createQuery(
                "SELECT COUNT(r) FROM Room r WHERE r.status = 'AVAILABLE'",
                Long.class
        ).getSingleResult();

        return count.intValue();
    }

    @Override
    public List<Room> findAllSortedByPrice() {
        return session().createQuery(
                "SELECT r FROM Room r ORDER BY r.pricePerNight",
                Room.class
        ).list();
    }

    @Override
    public List<Room> findAllSortedByCapacity() {
        return session().createQuery(
                "SELECT r FROM Room r ORDER BY r.capacity",
                Room.class
        ).list();
    }

    @Override
    public List<Room> findAllSortedByStars() {
        return session().createQuery(
                "SELECT r FROM Room r ORDER BY r.stars",
                Room.class
        ).list();
    }
}