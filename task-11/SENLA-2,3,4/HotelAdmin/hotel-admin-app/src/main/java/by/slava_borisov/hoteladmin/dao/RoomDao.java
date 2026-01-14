package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomDao extends GenericDao<Room, Integer> {

    Optional<Room> findByNumber(String number) throws SQLException;

    List<Room> findAvailableOnDate(LocalDate date) throws SQLException;

    void updateStatus(int roomId, RoomStatus status) throws SQLException;

    void updatePricePerNight(int roomId, double newPrice) throws SQLException;
}
