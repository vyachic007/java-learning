package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Amenity;

import java.math.BigDecimal;
import java.util.List;

public interface AmenityDao extends GenericDao<Amenity, Long> {

    void updatePrice(Long amenityId, BigDecimal newPrice);

    List<Amenity> findAllSortedByPrice();

    List<Amenity> findAllSortedByCategory();
}
