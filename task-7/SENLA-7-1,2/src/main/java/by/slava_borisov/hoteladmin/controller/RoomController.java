package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.hoteladmin.config.ConfigManager;
import by.slava_borisov.hoteladmin.exception.DuplicateRoomNumberException;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.model.HotelSystem;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.RoomView;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomController {
    private final HotelFacade hotelFacade;
    @Getter
    private final RoomView roomView;
    private final ConsoleUI consoleUI;
    @Getter
    private final HotelSystem hotelSystem;
    private final ConfigManager configManager;

    public RoomController(HotelSystem hotelSystem, HotelFacade hotelFacade, RoomView roomView, ConsoleUI consoleUI, ConfigManager configManager) {
        this.hotelSystem = hotelSystem;
        this.hotelFacade = hotelFacade;
        this.roomView = roomView;
        this.consoleUI = consoleUI;
        this.configManager = configManager;
    }

    public void displayAllRooms(SortCriteria sortCriteria) {
        List<Room> rooms = hotelFacade.viewAllRoomsSortedBy(sortCriteria);
        roomView.displayRooms(rooms);
    }

    public void displayAvailableRoomsOnDate(LocalDate date) {
        List<Room> rooms = hotelFacade.getAvailableRoomsOnDate(date);
        roomView.displayRooms(rooms);
    }

    public void displayRoomDetails(int roomId) {
        Optional<Room> roomOpt = hotelFacade.findRoomById(roomId);
        if (roomOpt.isPresent()) {
            roomView.displayRoomDetails(roomOpt.get());
        } else {
            roomView.displayErrorMessage(String.format(Messages.ROOM_NOT_FOUND, roomId));
        }
    }

    public void addRoom(
            String number,
            double price,
            int capacity,
            int stars,
            RoomStatus status
    ) throws DuplicateRoomNumberException {
        Room room = new Room(
                number,
                price,
                status,
                null,
                capacity,
                stars,
                new ArrayList<>()
        );
        hotelFacade.addRoom(room);
        roomView.displayMessage(String.format(Messages.ROOM_ADDED, room.getId()));
    }

    public Room findRoomByNumber(String roomNumber) {
        return hotelFacade.findRoomByNumber(roomNumber);
    }

    public void changeRoomPriceByNumber(String roomNumber, double newPrice) {
        Room room = findRoomByNumber(roomNumber);
        if (room != null) {
            hotelFacade.changeRoomPrice(room.getId(), newPrice);
        } else {
            consoleUI.displayErrorMessage(Messages.ROOM_NOT_FOUND);
        }
    }

    public void setRoomStatus(int roomId, RoomStatus status) {
        if (!configManager.isAllowRoomStatusChange()) {
            roomView.displayErrorMessage(Messages.ROOM_STATUS_CHANGE_DISABLED);
            return;
        }

        Result<Boolean> result = hotelFacade.setRoomStatus(roomId, status);
        if (result.isSuccess()) {
            roomView.displayMessage(Messages.STATUS_CHANGED_SUCCESSFULLY);
        } else {
            roomView.displayErrorMessage(result.getErrorMessage());
        }
    }

    public List<Room> getRoomsSortedByPrice() {
        return hotelFacade.viewAllRoomsSortedBy(SortCriteria.BY_PRICE);
    }

}
