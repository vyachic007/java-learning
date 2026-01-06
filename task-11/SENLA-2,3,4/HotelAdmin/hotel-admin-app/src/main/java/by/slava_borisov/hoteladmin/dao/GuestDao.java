package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Guest;

import java.sql.SQLException;
import java.util.Optional;

public interface GuestDao extends GenericDao<Guest, Integer> {
    Optional<Guest> findByPhone(String phone) throws SQLException;
}
