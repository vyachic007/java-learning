package by.slava_borisov.hoteladmin.dao.hibernate;

import by.slava_borisov.hoteladmin.dao.GuestDao;
import by.slava_borisov.hoteladmin.db.HibernateUtil;
import by.slava_borisov.hoteladmin.model.Guest;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class GuestDaoHibernate implements GuestDao {

    private static final Logger log = LoggerFactory.getLogger(GuestDaoHibernate.class);


    @Override
    public Optional<Guest> findByPhone(String phone) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            TypedQuery<Guest> query = session.createQuery(
                    "SELECT g FROM Guest g WHERE g.phone = :phone", Guest.class);
            query.setParameter("phone", phone);

            try {
                Guest guest = query.getSingleResult();
                return Optional.of(guest);
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Ошибка при поиске гостя по телефону {}", phone, e);
            throw new SQLException("Ошибка при поиске гостя: " + e.getMessage(), e);
        }
    }

    @Override
    public Guest create(Guest guest) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            session.persist(guest);
            HibernateUtil.commit();
            log.debug("Гость с телефоном {} создан, id={}", guest.getPhone(), guest.getId());
            return guest;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при создании гостя", e);
            throw new SQLException("Ошибка при создании гостя: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Guest> findById(Long guestId) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            Guest guestById = session.find(Guest.class, guestId);
            return Optional.ofNullable(guestById);
        } catch (Exception e) {
            log.error("Ошибка при поиске гостя по id={}", guestId, e);
            throw new SQLException("Ошибка при поиске гостя: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Guest> findAll() throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            return session.createQuery("SELECT g FROM Guest g", Guest.class).list();
        } catch (Exception e) {
            log.error("Ошибка при получении всех гостей", e);
            throw new SQLException("Ошибка при получении гостей: " + e.getMessage(), e);
        }
    }

    @Override
    public Guest update(Guest guest) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            Guest merged = session.merge(guest);
            HibernateUtil.commit();
            log.debug("Гость id={} обновлен", guest.getId());
            return merged;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при обновлении гостя id={}", guest.getId(), e);
            throw new SQLException("Ошибка при обновлении гостя: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(Long guestId) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            Guest guestToDelete = session.find(Guest.class, guestId);
            if (guestToDelete != null) {
                session.remove(guestToDelete);
                HibernateUtil.commit();
                log.debug("Гость id={} удален", guestId);
                return true;
            }
            HibernateUtil.commit();
            return false;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при удалении гостя id={}", guestId, e);
            throw new SQLException("Ошибка при удалении гостя: " + e.getMessage(), e);
        }
    }
}
