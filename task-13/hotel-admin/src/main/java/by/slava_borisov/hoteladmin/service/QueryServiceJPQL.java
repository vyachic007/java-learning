package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.db.HibernateUtil;
import by.slava_borisov.hoteladmin.model.Amenity;
import by.slava_borisov.hoteladmin.model.Booking;
import by.slava_borisov.hoteladmin.model.Guest;
import by.slava_borisov.hoteladmin.model.Room;
import org.hibernate.Session;

import java.util.List;

public class QueryServiceJPQL implements QueryService {

    @Override
    public int countAvailableRooms() {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            Long count = session.createQuery(
                    "SELECT COUNT(r) FROM Room r WHERE r.status = 'AVAILABLE'",
                    Long.class
            ).getSingleResult();

            HibernateUtil.commit();
            return count.intValue();
        } catch (Exception e) {
            HibernateUtil.rollback();
            return 0;
        }
    }

    @Override
    public int countCurrentGuests() {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            Long count = session.createQuery("""
                            SELECT COUNT(DISTINCT b.guest.id)
                            FROM Booking b
                            WHERE b.checkInDate <= CURRENT_DATE
                              AND b.checkOutDate > CURRENT_DATE
                              AND b.actualCheckOutDate IS NULL""",
                    Long.class
            ).getSingleResult();

            HibernateUtil.commit();
            return count.intValue();
        } catch (Exception e) {
            HibernateUtil.rollback();
            return 0;
        }
    }

    @Override
    public List<Booking> getLastBookings(Long roomId, int limit) {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            List<Booking> bookings = session.createQuery("""
                            SELECT b FROM Booking b
                            WHERE b.room.id = :roomId
                            ORDER BY b.checkInDate DESC
                            """, Booking.class)
                    .setParameter("roomId", roomId)
                    .setMaxResults(Math.max(0, limit))
                    .list();

            HibernateUtil.commit();
            return bookings;
        } catch (Exception e) {
            HibernateUtil.rollback();
            return List.of();
        }
    }

    @Override
    public double calculateGuestPayment(Long guestId) {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            Double amenitiesCost = session.createQuery("""
                            SELECT COALESCE(SUM(u.amenity.price * u.quantity), 0.0)
                            FROM AmenityUsage u
                            WHERE u.booking.guest.id = :guestId
                            """, Double.class)
                    .setParameter("guestId", guestId)
                    .getSingleResult();

            Double roomCost = session.createQuery("""
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

            HibernateUtil.commit();
            return amenitiesCost + roomCost;
        } catch (Exception e) {
            HibernateUtil.rollback();
            return 0.0;
        }
    }

    @Override
    public List<Room> getAllRoomsSortedByPrice() {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            List<Room> rooms = session.createQuery(
                    "SELECT r FROM Room r ORDER BY r.pricePerNight",
                    Room.class
            ).list();

            HibernateUtil.commit();
            return rooms;
        } catch (Exception e) {
            HibernateUtil.rollback();
            return List.of();
        }
    }

    @Override
    public List<Room> getAllRoomsSortedByCapacity() {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            List<Room> rooms = session.createQuery(
                    "SELECT r FROM Room r ORDER BY r.capacity",
                    Room.class
            ).list();

            HibernateUtil.commit();
            return rooms;
        } catch (Exception e) {
            HibernateUtil.rollback();
            return List.of();
        }
    }

    @Override
    public List<Room> getAllRoomsSortedByStars() {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            List<Room> rooms = session.createQuery(
                    "SELECT r FROM Room r ORDER BY r.stars",
                    Room.class
            ).list();

            HibernateUtil.commit();
            return rooms;
        } catch (Exception e) {
            HibernateUtil.rollback();
            return List.of();
        }
    }

    @Override
    public List<Guest> getGuestsSortedByName() {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            List<Guest> guests = session.createQuery(
                    "SELECT g FROM Guest g ORDER BY g.fullName",
                    Guest.class
            ).list();

            HibernateUtil.commit();
            return guests;
        } catch (Exception e) {
            HibernateUtil.rollback();
            return List.of();
        }
    }

    @Override
    public List<Guest> getGuestsSortedByCheckOutDate() {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            List<Guest> guests = session.createQuery("""
                            SELECT g FROM Guest g
                            JOIN g.bookingHistory b
                            WHERE b.checkInDate <= CURRENT_DATE
                              AND b.checkOutDate > CURRENT_DATE
                              AND b.actualCheckOutDate IS NULL
                            ORDER BY b.checkOutDate, g.fullName
                            """, Guest.class)
                    .list();

            HibernateUtil.commit();
            return guests;
        } catch (Exception e) {
            HibernateUtil.rollback();
            return List.of();
        }
    }



    @Override
    public List<Amenity> getAmenitiesSortedByPrice() {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            List<Amenity> amenities = session.createQuery(
                    "SELECT a FROM Amenity a ORDER BY a.price",
                    Amenity.class
            ).list();

            HibernateUtil.commit();
            return amenities;
        } catch (Exception e) {
            HibernateUtil.rollback();
            return List.of();
        }
    }

    @Override
    public List<Amenity> getAmenitiesSortedByCategory() {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            List<Amenity> amenities = session.createQuery(
                    "SELECT a FROM Amenity a ORDER BY a.category, a.name",
                    Amenity.class
            ).list();

            HibernateUtil.commit();
            return amenities;
        } catch (Exception e) {
            HibernateUtil.rollback();
            return List.of();
        }
    }
}
