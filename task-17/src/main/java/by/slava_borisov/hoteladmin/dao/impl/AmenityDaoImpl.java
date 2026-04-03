package by.slava_borisov.hoteladmin.dao.impl;

import by.slava_borisov.hoteladmin.dao.AmenityDao;
import by.slava_borisov.hoteladmin.exception.AmenityNotFoundException;
import by.slava_borisov.hoteladmin.model.Amenity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AmenityDaoImpl implements AmenityDao {

    private final SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
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
        return session().createSelectionQuery("FROM Amenity", Amenity.class).list();
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
        if (amenityToDelete == null) {
            throw new AmenityNotFoundException(amenityId);
        }

        session().remove(amenityToDelete);
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