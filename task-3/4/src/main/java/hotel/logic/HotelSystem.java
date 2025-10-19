package hotel.logic;

import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomStatus;
import hotel.model.Service;

import java.util.ArrayList;
import java.util.List;

public class HotelSystem {

    private List<Room> rooms = new ArrayList<>();
    private List<Guest> guests = new ArrayList<>();
    private List<Service> services = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("Номер " + room.getNumber() + " добавлен в систему");
    }

    public void addService(Service service) {
        services.add(service);
        System.out.println("Услуга '" + service.getName() + "' добавлена в систему");
    }

    public Room findRoomById(int roomId) {
        return rooms.stream()
                .filter(r -> r.getId() == roomId)
                .findFirst()
                .orElse(null);
    }

    public Service findServiceById(int serviceId) {
        return services.stream()
                .filter(s -> s.getId() == serviceId)
                .findFirst()
                .orElse(null);
    }

    public void checkIn(Guest guest, int roomId) {
        Room room = findRoomById(roomId);
        if (room != null && room.isAvailable()) {
            room.assignGuest(guest);
            guest.setBookedRoomId(roomId);

            if (!guests.contains(guest)) {
                guests.add(guest);
            }
            System.out.println("Гость " + guest.getFullName() + " заселен в номер " + room.getNumber());
        } else {
            System.out.println("Ошибка: невозможно заселить в номер " + roomId);
        }
    }

    public void checkOut(int roomId) {
        Room room = findRoomById(roomId);
        if (room != null && room.getCurrentGuest() != null) {
            String guestName = room.getCurrentGuest().getFullName();
            room.removeGuest();
            System.out.println("Гость " + guestName + " выселен из номера " + room.getNumber());
        } else {
            System.out.println("Ошибка: номер " + roomId + " не занят");
        }
    }

    public void setRoomStatus(int roomId, RoomStatus status) {
        Room room = findRoomById(roomId);
        if (room != null) {
            RoomStatus oldStatus = room.getStatus();
            room.setStatus(status);
            System.out.println("Статус номера " + room.getNumber() + " изменен: " + oldStatus + " -> " + status);
        } else {
            System.out.println("Ошибка: номер " + roomId + " не найден");
        }
    }

    public void updateRoomPrice(int roomId, double price) {
        Room room = findRoomById(roomId);
        if (room != null) {
            double oldPrice = room.getPricePerNight();
            room.setPricePerNight(price);
            System.out.println("Цена номера " + room.getNumber() + " изменена: " + oldPrice + " -> " + price);
        } else {
            System.out.println("Ошибка: номер " + roomId + " не найден");
        }
    }

    public void updateServicePrice(int serviceId, double price) {
        Service service = findServiceById(serviceId);
        if (service != null) {
            double oldPrice = service.getPrice();
            service.setPrice(price);
            System.out.println("Цена услуги '" + service.getName() + "' изменена: " + oldPrice + " -> " + price);
        } else {
            System.out.println("Ошибка: услуга с ID " + serviceId + " не найдена");
        }
    }
}