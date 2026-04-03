package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.dto.PriceDto;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.util.SortCriteria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    RoomDto addRoom(RoomDto roomDto);

    RoomDto getRoomById(Long roomId);

    RoomDto getRoomByNumber(Integer roomNumber);

    void setRoomStatus(Long roomId, RoomStatus status);

    void changeRoomPrice(Long roomId, BigDecimal newPrice);

    List<RoomDto> getAvailableRoomsOnDate(LocalDate date);

    List<RoomDto> getRoomsSortedBy(SortCriteria criteria);

    PriceDto calculateRoomPrice(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
}