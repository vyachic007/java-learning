package by.slava_borisov.hoteladmin.dao.impl;

import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.model.Guest;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class GuestDaoImpl extends AbstractHibernateDao<Guest, Long> implements GuestDao {

    public GuestDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Guest.class);
    }

    @Override
    public Optional<Guest> findByPhone(String phone) {
        TypedQuery<Guest> query = session().createQuery(
                "SELECT g FROM Guest g WHERE g.phone = :phone",
                Guest.class
        );
        query.setParameter("phone", phone);

        try {
            Guest guest = query.getSingleResult();
            return Optional.of(guest);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Guest create(Guest guest) {
        Guest created = super.create(guest);
        log.debug("Гость с телефоном {} создан, id={}", guest.getPhone(), guest.getId());
        return created;
    }

    @Override
    public Guest update(Guest guest) {
        Guest merged = super.update(guest);
        log.debug("Гость id={} обновлен", guest.getId());
        return merged;
    }

    @Override
    public boolean deleteById(Long guestId) {
        boolean deleted = super.deleteById(guestId);
        if (deleted) {
            log.debug("Гость id={} удален", guestId);
        }
        return deleted;
    }

    @Override
    public int countCurrentGuests() {
        Long count = session().createQuery("""
                    SELECT COUNT(DISTINCT b.guest.id)
                    FROM Booking b
                    WHERE b.checkInDate <= CURRENT_DATE
                      AND b.checkOutDate > CURRENT_DATE
                      AND b.actualCheckOutDate IS NULL
                """, Long.class)
                .getSingleResult();

        return count.intValue();
    }

    @Override
    public List<Guest> findAllSortedByName() {
        return session().createQuery(
                "SELECT g FROM Guest g ORDER BY g.fullName",
                Guest.class
        ).list();
    }

    @Override
    public List<Guest> findCurrentGuestsSortedByCheckOut() {
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
}