package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Amenity;
import java.util.List;

public interface AmenityDao extends GenericDao<Amenity, Long> {

    void updatePrice(Long amenityId, double newPrice);

    List<Amenity> findAllSortedByPrice();

    List<Amenity> findAllSortedByCategory();
}
