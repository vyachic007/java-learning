package by.slava_borisov.hoteladmin.dao.hibernate;

import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.model.Guest;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GuestDaoHibernate implements GuestDao {

    private final SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Optional<Guest> findByPhone(String phone) {
        TypedQuery<Guest> query = session().createQuery(
                "SELECT g FROM Guest g LEFT JOIN FETCH g.bookingHistory WHERE g.phone = :phone",
                Guest.class
        );
        query.setParameter("phone", phone);
        List<Guest> guests = query.getResultList();
        return guests.isEmpty() ? Optional.empty() : Optional.of(guests.get(0));
    }

    @Override
    public Guest create(Guest guest) {
        session().persist(guest);
        log.debug("Гость с телефоном {} создан, id={}", guest.getPhone(), guest.getId());
        return guest;
    }

    @Override
    public Optional<Guest> findById(Long guestId) {
        Guest guestById = session().find(Guest.class, guestId);
        return Optional.ofNullable(guestById);
    }

    @Override
    public List<Guest> findAll() {
        return session().createQuery("SELECT g FROM Guest g", Guest.class)
                .list();
    }

    @Override
    public Guest update(Guest guest) {
        Guest merged = session().merge(guest);
        log.debug("Гость id={} обновлен", guest.getId());
        return merged;
    }

    @Override
    public boolean deleteById(Long guestId) {
        Guest guestToDelete = session().find(Guest.class, guestId);
        if (guestToDelete != null) {
            session().remove(guestToDelete);
            log.debug("Гость id={} удален", guestId);
            return true;
        }
        return false;
    }
}
