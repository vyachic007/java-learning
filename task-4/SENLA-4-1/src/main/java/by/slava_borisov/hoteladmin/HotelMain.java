package by.slava_borisov.hoteladmin;

import by.slava_borisov.hoteladmin.logic.HotelSystem;
import by.slava_borisov.hoteladmin.logic.PriceManager;
import by.slava_borisov.hoteladmin.model.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class HotelMain {
    public static void main(String[] args) {
        HotelSystem hotel = new HotelSystem();
        PriceManager priceManager = new PriceManager();

        // Создаем номера
        Room room1 = new Room(1, "101", 5000.0, RoomStatus.AVAILABLE, null, 2, 4, new ArrayList<>());
        Room room2 = new Room(2, "102", 7500.0, RoomStatus.AVAILABLE, null, 3, 5, new ArrayList<>());
        Room room3 = new Room(3, "103", 10000.0, RoomStatus.AVAILABLE, null, 4, 3, new ArrayList<>());

        hotel.addRoom(room1);
        hotel.addRoom(room2);
        hotel.addRoom(room3);

        //Создаем услуги
        Amenity breakfast = new Amenity(1, "Завтрак", 800.0, "Еда");
        Amenity cleaning = new Amenity(2, "Уборка", 500.0, "Услуги");
        Amenity spa = new Amenity(3, "СПА", 2000.0, "Услуги");

        hotel.addAmenity(breakfast);
        hotel.addAmenity(cleaning);
        hotel.addAmenity(spa);

        //Создаем гостей и заселяем их в номера
        Guest guest1 = new Guest(1, "Иван Иванов", "+7-999-123-45-67", new ArrayList<>());
        Guest guest2 = new Guest(2, "Борисов Вячеслав", "+7-999-765-43-21", new ArrayList<>());

        System.out.println("\n===== ТЕСТИРОВАНИЕ ФУНКЦИОНАЛА ===\n");

        System.out.println("1. ЗАСЕЛЕНИЕ:");
        LocalDate checkIn1 = LocalDate.of(2025, 11, 1);
        LocalDate checkOut1 = LocalDate.of(2025, 11, 5);
        hotel.checkIn(guest1, 1, checkIn1, checkOut1);

        LocalDate checkIn2 = LocalDate.of(2025, 11, 2);
        LocalDate checkOut2 = LocalDate.of(2025, 11, 7);
        hotel.checkIn(guest2, 2, checkIn2, checkOut2);

        System.out.println("\n2. УПРАВЛЕНИЕ ЦЕНАМИ (PriceManager):");
        System.out.println("  Цена номера 101 ДО: " + room1.getPricePerNight() + " руб.");
        priceManager.updateRoomPrice(room1, 5500.0);
        System.out.println("  Цена номера 101 ПОСЛЕ: " + room1.getPricePerNight() + " руб.");

        System.out.println("  Цена услуги 'Завтрак' ДО: " + breakfast.getPrice() + " руб.");
        priceManager.updateAmenityPrice(breakfast, 900.0);
        System.out.println("  Цена услуги 'Завтрак' ПОСЛЕ: " + breakfast.getPrice() + " руб.");




        System.out.println("\n3. ТЕСТ ОШИБКИ - отрицательная цена:");
        try {
            priceManager.updateRoomPrice(room2, -1000.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка поймана: " + e.getMessage());
        }

        System.out.println("\n4. СМЕНА СТАТУСА НОМЕРА:");
        hotel.setRoomStatus(3, RoomStatus.UNDER_MAINTENANCE);

        System.out.println("\n5. ДОБАВЛЕНИЕ УСЛУГ ГОСТЯМ:");
        hotel.addAmenityToGuest(1, 1, LocalDate.now(), 2);  // Завтрак x2
        hotel.addAmenityToGuest(2, 3, LocalDate.now(), 1);  // СПА x1

        System.out.println("\n=== ПРОСМОТР ДАННЫХ ===");

        System.out.println("\n6. СПИСОК НОМЕРОВ ПО ЦЕНЕ:");
        hotel.viewAllRoomsSortedBy("price").forEach(r ->
                System.out.println("  " + r.getNumber() + " - " + r.getPricePerNight() + " руб."));

        System.out.println("\n7. СПИСОК НОМЕРОВ ПО ВМЕСТИМОСТИ:");
        hotel.viewAllRoomsSortedBy("capacity").forEach(r ->
                System.out.println("  " + r.getNumber() + " - вместимость: " + r.getCapacity()));

        System.out.println("\n8. СПИСОК НОМЕРОВ ПО ЗВЁЗДАМ:");
        hotel.viewAllRoomsSortedBy("stars").forEach(r ->
                System.out.println("  " + r.getNumber() + " - " + r.getStars() + " звёзд"));

        System.out.println("\n9. СВОБОДНЫЕ НОМЕРА ПО ЦЕНЕ:");
        hotel.viewAvailableRoomsSortedBy("price").forEach(r ->
                System.out.println("  " + r.getNumber() + " - " + r.getPricePerNight() + " руб."));

        System.out.println("\n10. СВОБОДНЫЕ НОМЕРА ПО ВМЕСТИМОСТИ:");
        hotel.viewAvailableRoomsSortedBy("capacity").forEach(r ->
                System.out.println("  " + r.getNumber() + " - вместимость: " + r.getCapacity()));

        System.out.println("\n11. СВОБОДНЫЕ НОМЕРА ПО ЗВЁЗДАМ:");
        hotel.viewAvailableRoomsSortedBy("stars").forEach(r ->
                System.out.println("  " + r.getNumber() + " - " + r.getStars() + " звёзд"));

        System.out.println("\n12. ГОСТИ ПО ИМЕНИ:");
        hotel.viewGuestsSortedBy("name").forEach(g ->
                System.out.println("  " + g.getFullName()));

        System.out.println("\n13. ГОСТИ ПО ДАТЕ ВЫСЕЛЕНИЯ:");
        hotel.viewGuestsSortedBy("checkoutdate").forEach(g ->
                System.out.println("  " + g.getFullName()));

        System.out.println("\n14. ОБЩЕЕ ЧИСЛО СВОБОДНЫХ НОМЕРОВ: " + hotel.getAvailableRoomsCount());

        System.out.println("\n15. ОБЩЕЕ ЧИСЛО ГОСТЕЙ: " + hotel.getGuestsCount());

        System.out.println("\n16. НОМЕРА СВОБОДНЫЕ НА ДАТУ 2025-11-10:");
        hotel.viewRoomsAvailableByDate(LocalDate.of(2025, 11, 10)).forEach(r ->
                System.out.println("  Номер " + r.getNumber() + " свободен"));

        System.out.println("\n17.СУММА К ОПЛАТЕ ДЛЯ ГОСТЯ 1:");
        double payment1 = hotel.calculateGuestPayment(1);
        System.out.println("  Сумма: " + payment1 + " руб.");

        System.out.println("\n18. 3 ПОСЛЕДНИХ БРОНИРОВАНИЯ НОМЕРА 1:");
        hotel.viewRoomHistory(1).forEach(b ->
                System.out.println("  " + b.getGuest().getFullName() +
                        " (" + b.getCheckInDate() + " - " + b.getCheckOutDate() + ")"));

        System.out.println("\n19. УСЛУГИ ГОСТЯ 1 ПО ЦЕНЕ:");
        hotel.viewGuestAmenities(1, "price").forEach(au ->
                System.out.println("  " + au.getAmenity().getName() + " - " + au.getTotalPrice() + " руб."));

        System.out.println("\n20. УСЛУГИ ГОСТЯ 1 ПО ДАТЕ:");
        hotel.viewGuestAmenities(1, "date").forEach(au ->
                System.out.println("  " + au.getAmenity().getName() + " - " + au.getUsageDate()));

        System.out.println("\n21. УСЛУГИ ПО КАТЕГОРИЯМ:");
        hotel.viewAmenitiesByCategory().forEach((category, amenities) -> {
            System.out.println("  " + category + ":");
            amenities.forEach(a -> System.out.println("    - " + a.getName() + " (" + a.getPrice() + " руб.)"));
        });

        System.out.println("\n22. ДЕТАЛИ НОМЕРА 1:");
        System.out.println(hotel.getRoomDetails(1));

        System.out.println("\n23. ВЫСЕЛЕНИЕ:");
        hotel.checkOut(1);

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===\n");
    }
}
