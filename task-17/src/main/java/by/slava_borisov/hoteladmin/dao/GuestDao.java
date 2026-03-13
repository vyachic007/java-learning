package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Guest;

import java.util.Optional;
import java.util.List;

public interface GuestDao extends GenericDao<Guest, Long> {

    Optional<Guest> findByPhone(String phone);

    int countCurrentGuests();

    List<Guest> findAllSortedByName();

    List<Guest> findCurrentGuestsSortedByCheckOut();
}
