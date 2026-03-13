package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.dto.response.PriceResponse;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.util.SortCriteria;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    RoomDto addRoom(RoomDto roomDto);

    Optional<RoomDto> findRoomById(Long roomId);

    Optional<RoomDto> findRoomByNumber(String roomNumber);

    void setRoomStatus(Long roomId, RoomStatus status);

    void changeRoomPrice(Long roomId, double newPrice);

    List<RoomDto> getAvailableRoomsOnDate(LocalDate date);

    List<RoomDto> viewAllRoomsSortedBy(SortCriteria criteria);

    PriceResponse calculateRoomPrice(Long roomId, String checkInDate, String checkOutDate);
}