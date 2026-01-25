package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Amenity;

import java.sql.SQLException;

public interface AmenityDao extends GenericDao<Amenity, Long> {

    void updatePrice(Long amenityId, double newPrice) throws SQLException;
}
