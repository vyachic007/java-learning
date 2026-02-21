package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Guest;

import java.util.Optional;

public interface GuestDao extends GenericDao<Guest, Long> {

    Optional<Guest> findByPhone(String phone);
}
