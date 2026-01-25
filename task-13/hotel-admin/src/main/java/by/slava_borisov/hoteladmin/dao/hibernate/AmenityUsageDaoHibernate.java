package by.slava_borisov.hoteladmin.dao.hibernate;

import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.db.HibernateUtil;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class AmenityUsageDaoHibernate implements AmenityUsageDao {

    private static final Logger log = LoggerFactory.getLogger(AmenityUsageDaoHibernate.class);


    @Override
    public List<AmenityUsage> findByBookingId(Long bookingId) throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            return session.createQuery(
                            "SELECT au FROM AmenityUsage au " +
                                    "JOIN FETCH au.amenity " +
                                    "JOIN FETCH au.booking " +
                                    "WHERE au.booking.id = :bookingId", AmenityUsage.class)
                    .setParameter("bookingId", bookingId)
                    .list();
        } catch (Exception e) {
            log.error("Ошибка при получении использований услуг для бронирования id={}", bookingId, e);
            throw new SQLException("Ошибка при получении данных: " + e.getMessage(), e);
        }
    }

    @Override
    public AmenityUsage create(AmenityUsage amenityUsage) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            session.persist(amenityUsage);
            HibernateUtil.commit();
            log.debug("Использование услуги создано: bookingId={}, amenityId={}",
                    amenityUsage.getBooking().getId(), amenityUsage.getAmenity().getId());
            return amenityUsage;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при создании использования услуги", e);
            throw new SQLException("Ошибка при создании: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AmenityUsage> findById(Long id) throws SQLException {
        throw new UnsupportedOperationException(
                "AmenityUsage имеет составной ключ. Используйте findByBookingId(bookingId)");
    }


    @Override
    public List<AmenityUsage> findAll() throws SQLException {
        try {
            Session session = HibernateUtil.getSession();
            return session.createQuery("SELECT au FROM AmenityUsage au", AmenityUsage.class)
                    .list();
        } catch (Exception e) {
            log.error("Ошибка при получении всех использований услуг", e);
            throw new SQLException("Ошибка при получении данных: " + e.getMessage(), e);
        }
    }


    @Override
    public AmenityUsage update(AmenityUsage amenityUsage) throws SQLException {
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            AmenityUsage merged = session.merge(amenityUsage);
            HibernateUtil.commit();
            log.debug("Использование услуги обновлено: bookingId={}, amenityId={}",
                    amenityUsage.getBooking().getId(), amenityUsage.getAmenity().getId());
            return merged;
        } catch (Exception e) {
            HibernateUtil.rollback();
            log.error("Ошибка при обновлении использования услуги", e);
            throw new SQLException("Ошибка при обновлении: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        throw new UnsupportedOperationException(
                "AmenityUsage имеет составной ключ. Используйте deleteByBookingAndAmenity(bookingId, amenityId)");
    }
}
