package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomDao extends GenericDao<Room, Long> {

    Optional<Room> findByNumber(Integer number);

    List<Room> findAvailableOnDate(LocalDate date);

    void updateStatus(Long roomId, RoomStatus status);

    void updatePricePerNight(Long roomId, BigDecimal newPrice);

    int countAvailable();

    List<Room> findAllSortedByPrice();

    List<Room> findAllSortedByCapacity();

    List<Room> findAllSortedByStars();
}