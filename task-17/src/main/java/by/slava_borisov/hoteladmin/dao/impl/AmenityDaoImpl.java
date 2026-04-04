package by.slava_borisov.hoteladmin.dao.impl;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.model.Amenity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Repository
public class AmenityDaoImpl extends AbstractHibernateDao<Amenity, Long> implements AmenityDao {

    public AmenityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Amenity.class);
    }

    @Override
    public void updatePrice(Long amenityId, BigDecimal newPrice) {
        int updated = session().createMutationQuery(
                        "UPDATE Amenity a SET a.price = :price WHERE a.id = :id")
                .setParameter("id", amenityId)
                .setParameter("price", newPrice)
                .executeUpdate();

        if (updated == 0) {
            throw new AmenityNotFoundException(amenityId);
        }

        log.debug("Цена услуги id={} обновлена на {}", amenityId, newPrice);
    }

    @Override
    public boolean deleteById(Long amenityId) {
        boolean deleted = super.deleteById(amenityId);
        if (!deleted) {
            throw new AmenityNotFoundException(amenityId);
        }

        log.info("Услуга с id={} удалена", amenityId);
        return true;
    }

    @Override
    public List<Amenity> findAllSortedByPrice() {
        return session().createQuery(
                "SELECT a FROM Amenity a ORDER BY a.price",
                Amenity.class
        ).list();
    }

    @Override
    public List<Amenity> findAllSortedByCategory() {
        return session().createQuery(
                "SELECT a FROM Amenity a ORDER BY a.category, a.name",
                Amenity.class
        ).list();
    }
}