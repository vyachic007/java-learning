package by.slava_borisov.hoteladmin.controller;

import by.slava_borisov.reflection.di.Inject;
import by.slava_borisov.hoteladmin.config.ConfigManager;
import by.slava_borisov.hoteladmin.exception.DuplicateRoomNumberException;
import by.slava_borisov.hoteladmin.model.Room;
import by.slava_borisov.hoteladmin.model.RoomStatus;
import by.slava_borisov.hoteladmin.service.HotelFacade;
import by.slava_borisov.hoteladmin.service.Result;
import by.slava_borisov.hoteladmin.ui.ConsoleUI;
import by.slava_borisov.hoteladmin.ui.SortCriteria.SortCriteria;
import by.slava_borisov.hoteladmin.util.Messages;
import by.slava_borisov.hoteladmin.view.RoomView;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RoomController {

    @Inject
    private HotelFacade hotelFacade;

    @Inject
    @Getter
    private RoomView roomView;

    @Inject
    private ConsoleUI consoleUI;

    @Inject
    private ConfigManager configManager;

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);


    public void displayAllRooms(SortCriteria sortCriteria) {
        log.info("Начало обработки команды: вывести все комнаты, критерий сортировки {}", sortCriteria);
        List<Room> rooms = hotelFacade.viewAllRoomsSortedBy(sortCriteria);
        roomView.displayRooms(rooms);
        log.info("Комнаты успешно выведены и отсортированы по критерию {}", sortCriteria);
    }

    public void displayAvailableRoomsOnDate(LocalDate date) {
        log.info("Начало обработки команды: вывести все свободные номера на дату {}", date);
        List<Room> rooms = hotelFacade.getAvailableRoomsOnDate(date);
        roomView.displayRooms(rooms);
        log.info("Свободные номера на дату {} успешно выведены ({} номеров)", date, rooms.size());
    }

    public void displayRoomDetails(int roomId) {
        log.info("Начало обработки команды: вывести детали комнаты id={}", roomId);
        Optional<Room> roomOpt = hotelFacade.findRoomById(roomId);
        if (roomOpt.isPresent()) {
            roomView.displayRoomDetails(roomOpt.get());
            log.info("Детали комнаты id={} успешно выведены", roomId);
        } else {
            roomView.displayErrorMessage(String.format(Messages.ROOM_NOT_FOUND, roomId));
            log.error("Ошибка при выведении деталей комнаты id={}: комната не найдена", roomId);
        }
    }

    public void addRoom(
            String number,
            double price,
            int capacity,
            int stars,
            RoomStatus status
    ) throws DuplicateRoomNumberException {
        log.info("Начало обработки команды: добавление комнаты номер={}, цена={}, вместимость={}, звезд={}, статус={}",
                number, price, capacity, stars, status);
        Room room = new Room(number, price, status, capacity, stars);

        Result<Room> result = hotelFacade.addRoom(room);

        if (result.isSuccess() && result.getData() != null) {
            Room created = result.getData();
            roomView.displayMessage(String.format(Messages.ROOM_ADDED, created.getId()));
            log.info("Комната с номером {} успешно добавлена", number);
        } else {
            roomView.displayErrorMessage(result.getErrorMessage());
            log.error("Ошибка при добавлении комнаты с номером {}: {}", number, result.getErrorMessage());
        }
    }

    public Room findRoomByNumber(String roomNumber) {
        log.info("Начало обработки команды: поиск комнаты под номером {}", roomNumber);
        Room roomByNumber = hotelFacade.findRoomByNumber(roomNumber);

        if (roomByNumber != null) {
            log.info("Комната под номером {} успешно найдена", roomNumber);
        } else {
            log.info("Комната под номером {} не найдена", roomNumber);
        }

        return roomByNumber;
    }


    public void changeRoomPriceByNumber(String roomNumber, double newPrice) {
        log.info("Начало обработки команды: изменить цену комнаты под номером {}, новая цена {}", roomNumber, newPrice);
        Room room = findRoomByNumber(roomNumber);

        if (room == null) {
            consoleUI.displayErrorMessage(Messages.ROOM_NOT_FOUND);
            log.error("Ошибка при изменении цены комнаты. Комната с номером {} не найдена", roomNumber);
            return;
        }

        Result<Boolean> result = hotelFacade.changeRoomPrice(room.getId(), newPrice);
        if (result.isSuccess()) {
            roomView.displayMessage(Messages.OPERATION_SUCCESS);
            log.info("Цена комнаты под номером {} успешно обновлена на {}", roomNumber, newPrice);
        } else {
            roomView.displayErrorMessage(result.getErrorMessage());
            log.error("Ошибка при изменении цены комнаты под номером {}: {}", roomNumber, result.getErrorMessage());
        }
    }


    public void setRoomStatus(int roomId, RoomStatus status) {
        log.info("Начало обработки команды: установить статус {} комнате с id={}", status, roomId);

        if (!configManager.isAllowRoomStatusChange()) {
            roomView.displayErrorMessage(Messages.ROOM_STATUS_CHANGE_DISABLED);
            log.error("Ошибка при установке статуса комнате с id={}: изменение статусов отключено в конфигурации", roomId);
            return;
        }

        Result<Boolean> result = hotelFacade.setRoomStatus(roomId, status);
        if (result.isSuccess()) {
            roomView.displayMessage(Messages.STATUS_CHANGED_SUCCESSFULLY);
            log.info("Статус комнаты с id={} успешно обновлен на {}", roomId, status);
        } else {
            roomView.displayErrorMessage(result.getErrorMessage());
            log.error("Ошибка при установке статуса комнате с id={}: {}", roomId, result.getErrorMessage());
        }
    }


    public List<Room> getRoomsSortedByPrice() {
        log.info("Начало обработки команды: получить все комнаты отсортированные по цене");
        List<Room> roomsSortedByPrice = hotelFacade.viewAllRoomsSortedBy(SortCriteria.BY_PRICE);
        log.info("Комнаты отсортированные по цене успешно получены ({} комнат)", roomsSortedByPrice.size());
        return roomsSortedByPrice;
    }
}
