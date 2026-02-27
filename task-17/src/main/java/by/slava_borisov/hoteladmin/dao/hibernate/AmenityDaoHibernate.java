package by.slava_borisov.hoteladmin.dao.hibernate;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.model.Amenity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AmenityDaoHibernate implements AmenityDao {

    private static final Logger log = LoggerFactory.getLogger(AmenityDaoHibernate.class);
    private final SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void updatePrice(Long amenityId, double newPrice) {
        try {
            Session session = sessionFactory.getCurrentSession();

            int updated = session.createMutationQuery("UPDATE Amenity a SET a.price = :price WHERE a.id = :id")
                    .setParameter("id", amenityId)
                    .setParameter("price", newPrice)
                    .executeUpdate();

            if (updated == 0) {
                throw new AmenityNotFoundException(amenityId);
            }

            log.debug("Цена услуги id={} обновлена на {}", amenityId, newPrice);
        } catch (Exception e) {
            log.error("Ошибка при обновлении цены услуги id={}", amenityId, e);
            throw new RuntimeException("Ошибка при обновлении услуги", e);
        }
    }

    @Override
    public Amenity create(Amenity amenity) {
        session().persist(amenity);
        log.debug("Услуга '{}' создана, id={}", amenity.getName(), amenity.getId());
        return amenity;
    }

    @Override
    public Optional<Amenity> findById(Long amenityId) {
        Amenity amenity = session().find(Amenity.class, amenityId);
        return Optional.ofNullable(amenity);
    }

    @Override
    public List<Amenity> findAll() {
        return session().createQuery("SELECT a FROM Amenity a", Amenity.class).list();
    }

    @Override
    public Amenity update(Amenity amenity) {
        Amenity merged = session().merge(amenity);
        log.debug("Услуга {} обновлена", amenity.getId());
        return merged;
    }

    @Override
    public boolean deleteById(Long amenityId) {
        Amenity amenityToDelete = session().find(Amenity.class, amenityId);
        if (amenityToDelete != null) {
            session().remove(amenityToDelete);
            log.info("Услуга с id={} удалена", amenityId);
            return true;
        }
        return false;
    }
}
