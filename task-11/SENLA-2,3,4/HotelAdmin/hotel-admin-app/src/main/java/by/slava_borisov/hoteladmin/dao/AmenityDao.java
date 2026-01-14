package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Amenity;

import java.sql.SQLException;
import java.util.List;

public interface AmenityDao extends GenericDao<Amenity, Integer> {

    List<Amenity> findAllSortedByPrice() throws SQLException;

    List<Amenity> findAllSortedByCategory() throws SQLException;

    void updatePrice(int amenityId, double newPrice) throws SQLException;
}
