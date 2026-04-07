package by.slava_borisov.hoteladmin.dao.impl;

import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class AmenityUsageDaoImpl extends AbstractHibernateDao<AmenityUsage, Long> implements AmenityUsageDao {

    public AmenityUsageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, AmenityUsage.class);
    }

    @Override
    public List<AmenityUsage> findByBookingId(Long bookingId) {
        return session().createQuery(
                        "SELECT au FROM AmenityUsage au " +
                                "JOIN FETCH au.amenity " +
                                "JOIN FETCH au.booking " +
                                "WHERE au.booking.id = :bookingId",
                        AmenityUsage.class
                )
                .setParameter("bookingId", bookingId)
                .list();
    }

    @Override
    public AmenityUsage create(AmenityUsage amenityUsage) {
        AmenityUsage created = super.create(amenityUsage);
        log.debug("Использование услуги создано: bookingId={}, amenityId={}",
                amenityUsage.getBooking().getId(), amenityUsage.getAmenity().getId());
        return created;
    }

    @Override
    public AmenityUsage update(AmenityUsage amenityUsage) {
        AmenityUsage merged = super.update(amenityUsage);
        log.debug("Использование услуги обновлено: id={}", amenityUsage.getId());
        return merged;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleted = super.deleteById(id);
        if (deleted) {
            log.debug("Использование услуги удалено: id={}", id);
        }
        return deleted;
    }
}