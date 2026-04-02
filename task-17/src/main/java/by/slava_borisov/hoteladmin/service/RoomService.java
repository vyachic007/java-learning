package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.dto.PriceDto;
import by.slava_borisov.hoteladmin.dto.RoomDto;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.util.SortCriteria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    RoomDto addRoom(RoomDto roomDto);

    Optional<RoomDto> findRoomById(Long roomId);

    Optional<RoomDto> findRoomByNumber(Integer roomNumber);

    void setRoomStatus(Long roomId, RoomStatus status);

    void changeRoomPrice(Long roomId, BigDecimal newPrice);

    List<RoomDto> getAvailableRoomsOnDate(LocalDate date);

    List<RoomDto> viewAllRoomsSortedBy(SortCriteria criteria);

    PriceDto calculateRoomPrice(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
}