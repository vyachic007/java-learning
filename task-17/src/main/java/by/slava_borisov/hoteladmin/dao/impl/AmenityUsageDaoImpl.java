package by.slava_borisov.hoteladmin.dao.impl;

import by.slava_borisov.hoteladmin.dao.AmenityUsageDao;
import by.slava_borisov.hoteladmin.model.AmenityUsage;
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
public class AmenityUsageDaoImpl implements AmenityUsageDao {

    private final SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
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
        session().persist(amenityUsage);
        log.debug("Использование услуги создано: bookingId={}, amenityId={}",
                amenityUsage.getBooking().getId(), amenityUsage.getAmenity().getId());
        return amenityUsage;
    }

    @Override
    public Optional<AmenityUsage> findById(Long id) {
        AmenityUsage amenityUsage = session().find(AmenityUsage.class, id);
        return Optional.ofNullable(amenityUsage);
    }

    @Override
    public List<AmenityUsage> findAll() {
        return session()
                .createQuery("SELECT au FROM AmenityUsage au", AmenityUsage.class)
                .list();
    }

    @Override
    public AmenityUsage update(AmenityUsage amenityUsage) {
        AmenityUsage merged = session().merge(amenityUsage);
        log.debug("Использование услуги обновлено: id={}", amenityUsage.getId());
        return merged;
    }

    @Override
    public boolean deleteById(Long id) {
        AmenityUsage amenityUsage = session().find(AmenityUsage.class, id);
        if (amenityUsage == null) {
            return false;
        }

        session().remove(amenityUsage);
        log.debug("Использование услуги удалено: id={}", id);
        return true;
    }
}