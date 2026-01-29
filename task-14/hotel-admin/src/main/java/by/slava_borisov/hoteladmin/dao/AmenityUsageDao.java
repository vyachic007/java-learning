package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.AmenityUsage;

import java.sql.SQLException;
import java.util.List;

public interface AmenityUsageDao extends GenericDao<AmenityUsage, Long> {

    List<AmenityUsage> findByBookingId(Long bookingId) throws SQLException;
}
