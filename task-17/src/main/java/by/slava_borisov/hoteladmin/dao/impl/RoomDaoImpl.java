package by.slava_borisov.hoteladmin.dao.impl;

import by.slava_borisov.hoteladmin.dao.RoomDao;
import by.slava_borisov.hoteladmin.exception.RoomNotFoundException;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
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
public class RoomDaoImpl implements RoomDao {

    private final SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Optional<Room> findByNumber(String number) {
        TypedQuery<Room> query = session().createQuery("SELECT r FROM Room r WHERE r.number = :number", Room.class);
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
        return session().createQuery("SELECT r FROM Room r WHERE r.id NOT IN " +
                        "(SELECT b.room.id FROM Booking b WHERE :date BETWEEN b.checkInDate AND b.checkOutDate " +
                        "AND b.actualCheckOutDate IS NULL)", Room.class)
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
    public void updatePricePerNight(Long roomId, double newPrice) {
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
        session().persist(room);
        log.debug("Комната с номером {} создана, id={}", room.getNumber(), room.getId());
        return room;
    }

    @Override
    public Optional<Room> findById(Long roomId) {
        Room roomById = session().find(Room.class, roomId);
        return Optional.ofNullable(roomById);
    }

    @Override
    public List<Room> findAll() {
        return session().createQuery("SELECT r FROM Room r", Room.class)
                .list();
    }

    @Override
    public Room update(Room room) {
        Room merged = session().merge(room);
        session().flush();
        log.debug("Комната id={} обновлена", room.getId());
        return merged;
    }

    @Override
    public boolean deleteById(Long roomId) {
        Room roomForDelete = session().find(Room.class, roomId);
        if (roomForDelete != null) {
            session().remove(roomForDelete);
            log.debug("Комната id={} удалена", roomId);
            return true;
        }
        return false;
    }
}
