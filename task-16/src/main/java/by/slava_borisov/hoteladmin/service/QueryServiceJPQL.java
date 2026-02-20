package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class QueryServiceJPQL implements QueryService {

    private final SessionFactory sessionFactory;

    public QueryServiceJPQL(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public int countAvailableRooms() {
        Long count = session().createQuery(
                "SELECT COUNT(r) FROM Room r WHERE r.status = 'AVAILABLE'",
                Long.class
        ).getSingleResult();

        return count.intValue();
    }

    @Override
    public int countCurrentGuests() {
        Long count = session().createQuery("""
                        SELECT COUNT(DISTINCT b.guest.id)
                        FROM Booking b
                        WHERE b.checkInDate <= CURRENT_DATE
                          AND b.checkOutDate > CURRENT_DATE
                          AND b.actualCheckOutDate IS NULL""",
                Long.class
        ).getSingleResult();

        return count.intValue();
    }

    @Override
    public List<Booking> getLastBookings(Long roomId, int limit) {
        return session().createQuery("""
                        SELECT b FROM Booking b
                        WHERE b.room.id = :roomId
                        ORDER BY b.checkInDate DESC
                        """, Booking.class)
                .setParameter("roomId", roomId)
                .setMaxResults(Math.max(0, limit))
                .list();
    }

    @Override
    public double calculateGuestPayment(Long guestId) {
        Double amenitiesCost = session().createQuery("""
                        SELECT COALESCE(SUM(u.amenity.price * u.quantity), 0.0)
                        FROM AmenityUsage u
                        WHERE u.booking.guest.id = :guestId
                        """, Double.class)
                .setParameter("guestId", guestId)
                .getSingleResult();

        Double roomCost = session().createQuery("""
                        SELECT COALESCE(SUM(b.room.pricePerNight *
                               FUNCTION('GREATEST', 0,
                               FUNCTION('DATE_DIFF', 'DAY', b.checkInDate, CURRENT_DATE))), 0.0)
                        FROM Booking b
                        WHERE b.guest.id = :guestId
                          AND b.checkInDate <= CURRENT_DATE
                          AND b.checkOutDate > CURRENT_DATE
                          AND b.actualCheckOutDate IS NULL
                        """, Double.class)
                .setParameter("guestId", guestId)
                .getSingleResult();

        return amenitiesCost + roomCost;
    }

    @Override
    public List<Room> getAllRoomsSortedByPrice() {
        return session().createQuery(
                "SELECT r FROM Room r ORDER BY r.pricePerNight",
                Room.class
        ).list();
    }

    @Override
    public List<Room> getAllRoomsSortedByCapacity() {
        return session().createQuery(
                "SELECT r FROM Room r ORDER BY r.capacity",
                Room.class
        ).list();
    }

    @Override
    public List<Room> getAllRoomsSortedByStars() {
        return session().createQuery(
                "SELECT r FROM Room r ORDER BY r.stars",
                Room.class
        ).list();
    }

    @Override
    public List<Guest> getGuestsSortedByName() {
        return session().createQuery(
                "SELECT g FROM Guest g ORDER BY g.fullName",
                Guest.class
        ).list();
    }

    @Override
    public List<Guest> getGuestsSortedByCheckOutDate() {
        return session().createQuery("""
                        SELECT g FROM Guest g
                        JOIN g.bookingHistory b
                        WHERE b.checkInDate <= CURRENT_DATE
                          AND b.checkOutDate > CURRENT_DATE
                          AND b.actualCheckOutDate IS NULL
                        ORDER BY b.checkOutDate, g.fullName
                        """, Guest.class)
                .list();
    }


    @Override
    public List<Amenity> getAmenitiesSortedByPrice() {
        return session().createQuery(
                "SELECT a FROM Amenity a ORDER BY a.price",
                Amenity.class
        ).list();
    }

    @Override
    public List<Amenity> getAmenitiesSortedByCategory() {
        return session().createQuery(
                "SELECT a FROM Amenity a ORDER BY a.category, a.name",
                Amenity.class
        ).list();
    }
}
