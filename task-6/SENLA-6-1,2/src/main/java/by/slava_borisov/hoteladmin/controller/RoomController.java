package by.slava_borisov.hoteladmin.controller;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomController {
    private final HotelFacade hotelFacade;
    private final RoomView roomView;
    private final ConsoleUI consoleUI;
    private final HotelSystem hotelSystem;

    public RoomController(HotelSystem hotelSystem, HotelFacade hotelFacade, RoomView roomView, ConsoleUI consoleUI) {
        this.hotelSystem = hotelSystem;
        this.hotelFacade = hotelFacade;
        this.roomView = roomView;
        this.consoleUI = consoleUI;
    }

    public HotelSystem getHotelSystem() {
        return hotelSystem;
    }

    public void displayAllRooms(SortCriteria sortCriteria) {
        List<Room> rooms = hotelFacade.viewAllRoomsSortedBy(sortCriteria);
        roomView.displayRooms(rooms);
    }

    public void displayAvailableRooms(SortCriteria sortCriteria) {
        List<Room> availableRooms = hotelFacade.viewAvailableRoomsSortedBy(sortCriteria);
        roomView.displayRooms(availableRooms);
    }

    public void changeRoomPrice(int roomId, double newPrice) {
        Result<Boolean> result = hotelFacade.changeRoomPrice(roomId, newPrice);

        if (result.isSuccess()) {
            String roomNumber = "";
            roomView.displayMessage(String.format(Messages.ROOM_PRICE_CHANGED, roomNumber, 0.0, newPrice));
        } else {
            roomView.displayErrorMessage(result.getErrorMessage());
        }
    }

    public void changeRoomStatus(int roomId, RoomStatus status) {
        Result<Boolean> result = hotelFacade.setRoomStatus(roomId, status);
        if (result.isSuccess()) {
            roomView.displayMessage(Messages.ROOM_PRICE_CHANGED);
        } else {
            roomView.displayErrorMessage(result.getErrorMessage());
        }
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
        hotelFacade.setRoomStatus(roomId, status);
    }
}
