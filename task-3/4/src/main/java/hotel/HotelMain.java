package hotel;

import hotel.logic.HotelSystem;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomStatus;
import hotel.model.Service;

public class HotelMain {
    public static void main(String[] args) {
        HotelSystem hotel = new HotelSystem();

        Room room1 = new Room(1, "777", 5000.0, RoomStatus.AVAILABLE, null);
        Room room2 = new Room(2, "666", 7500.0, RoomStatus.AVAILABLE, null);
        Room room3 = new Room(3, "007", 10000.0, RoomStatus.AVAILABLE, null);

        hotel.addRoom(room1);
        hotel.addRoom(room2);
        hotel.addRoom(room3);

        Service breakfast = new Service(1, "Завтрак", 800.0);
        Service cleaning = new Service(2, "Уборка", 500.0);
        Service spa = new Service(3, "СПА", 2000.0);

        hotel.addService(breakfast);
        hotel.addService(cleaning);
        hotel.addService(spa);

        Guest guest1 = new Guest(1, "Иван Иванов", "+7-999-123-45-67", 0);
        Guest guest2 = new Guest(2, "Борисов Вячеслав", "+7-999-765-43-21", 0);




        System.out.println("\n=== ТЕСТИРОВАНИЕ ФУНКЦИОНАЛА ===\n");


        System.out.println("1. ЗАСЕЛЕНИЕ:");
        hotel.checkIn(guest1, 1);
        hotel.checkIn(guest2, 2);

        System.out.println("\n2. СМЕНА СТАТУСА НОМЕРА:");
        hotel.setRoomStatus(3, RoomStatus.UNDER_MAINTENANCE);

        System.out.println("\n3. ИЗМЕНЕНИЕ ЦЕН:");
        hotel.updateRoomPrice(1, 5500.0);
        hotel.updateServicePrice(1, 900.0);


        System.out.println("\n4. ВЫСЕЛЕНИЕ:");
        hotel.checkOut(1);

        System.out.println("\n5. ИНФОРМАЦИЯ О НОМЕРАХ:");
        Room foundRoom1 = hotel.findRoomById(1);
        Room foundRoom2 = hotel.findRoomById(2);
        Room foundRoom3 = hotel.findRoomById(3);

        if (foundRoom1 != null) System.out.println(foundRoom1);
        if (foundRoom2 != null) System.out.println(foundRoom2);
        if (foundRoom3 != null) System.out.println(foundRoom3);

        System.out.println("\n6. ИНФОРМАЦИЯ О ГОСТЯХ:");
        System.out.println(guest1);
        System.out.println(guest2);

        System.out.println("\n7. ИНФОРМАЦИЯ О УСЛУГАХ:");
        Service foundService1 = hotel.findServiceById(1);
        Service foundService2 = hotel.findServiceById(2);
        Service foundService3 = hotel.findServiceById(3);

        if (foundService1 != null) System.out.println(foundService1);
        if (foundService2 != null) System.out.println(foundService2);
        if (foundService3 != null) System.out.println(foundService3);

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");

    }
}
