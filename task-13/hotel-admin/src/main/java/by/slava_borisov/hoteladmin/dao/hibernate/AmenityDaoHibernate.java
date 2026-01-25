package by.slava_borisov.hoteladmin.dao.hibernate;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.db.HibernateUtil;
import by.slava_borisov.hoteladmin.model.Amenity;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AmenityDaoHibernate implements AmenityDao {

    private static final Logger log = LoggerFactory.getLogger(AmenityDaoHibernate.class);


    @Override
    public void updatePrice(Long amenityId, double newPrice) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();

            int updated = session.createQuery("UPDATE Amenity a SET a.price = :price WHERE a.id = :id")
                    .setParameter("id", amenityId)
                    .setParameter("price", newPrice)
                    .executeUpdate();

            if (updated == 0) {
                HibernateUtil.rollback();
                throw new SQLException("Услуга с id=" + amenityId + " не найдена");
            }

            HibernateUtil.commit();
            log.debug("Цена услуги id={} обновлена на {}", amenityId, newPrice);
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при обновлении цены услуги id={}", amenityId, e);
            throw new SQLException("Ошибка при обновлении услуги: " + e.getMessage(), e);
        }
    }

    @Override
    public Amenity create(Amenity amenity) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            session.persist(amenity);
            HibernateUtil.commit();
            log.debug("Услуга '{}' создана, id={}", amenity.getName(), amenity.getId());
            return amenity;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при создании услуги", e);
            throw new SQLException("Ошибка при создании услуги: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Amenity> findById(Long amenityId) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            Amenity amenity = session.find(Amenity.class, amenityId);
            return Optional.ofNullable(amenity);
        } catch (Exception e) {
            log.error("Ошибка при поиске услуги по id={}", amenityId, e);
            throw new SQLException("Ошибка при поиске услуги: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Amenity> findAll() throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            return session.createQuery("SELECT a FROM Amenity a", Amenity.class).list();
        } catch (Exception e) {
            log.error("Ошибка при получении всех услуг", e);
            throw new SQLException("Ошибка при получении услуг: " + e.getMessage(), e);
        }
    }

    @Override
    public Amenity update(Amenity amenity) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            Amenity merged = session.merge(amenity);
            HibernateUtil.commit();
            log.debug("услуга id={} обновлена", amenity.getId());
            return merged;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при обновлении услуги id={}", amenity.getId(), e);
            throw new SQLException("Ошибка при обновлении услуги: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(Long amenityId) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            Amenity amenityToDelete = session.find(Amenity.class, amenityId);
            if (amenityToDelete != null) {
                session.remove(amenityToDelete);
                HibernateUtil.commit();
                log.debug("услуга id={} удалена", amenityId);
                return true;
            }
            HibernateUtil.commit();
            return false;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при удалении услуги id={}", amenityId, e);
            throw new SQLException("Ошибка при удалении услуги: " + e.getMessage(), e);
        }
    }
}
