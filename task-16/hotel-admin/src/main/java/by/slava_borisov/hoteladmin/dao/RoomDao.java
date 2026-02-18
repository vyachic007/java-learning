package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomDao extends GenericDao<Room, Long> {

    Optional<Room> findByNumber(String number) throws SQLException;

    List<Room> findAvailableOnDate(LocalDate date) throws SQLException;

    void updateStatus(Long roomId, RoomStatus status) throws SQLException;

    void updatePricePerNight(Long roomId, double newPrice) throws SQLException;
}
