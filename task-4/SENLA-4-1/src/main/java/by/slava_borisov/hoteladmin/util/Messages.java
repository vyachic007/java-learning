package by.slava_borisov.hoteladmin.util;

public class Messages {
    private Messages() {
    }

    public static final String ROOM_ADDED = "Номер %s добавлен в систему%n";
    public static final String ROOM_NOT_FOUND = "Ошибка: номер %d не найден%n";
    public static final String ROOM_NOT_OCCUPIED = "Ошибка: номер %d не занят%n";
    public static final String CHECKIN_ERROR = "Ошибка: невозможно заселить в номер %d%n";
    public static final String CHECKIN_SUCCESS = "Гость %s заселен в номер %s%n";
    public static final String CHECKOUT_SUCCESS = "Гость %s выселен из номера %s%n";
    public static final String ROOM_STATUS_CHANGED = "Статус номера %s изменен: %s -> %s%n";
    public static final String ROOM_PRICE_CHANGED = "Цена номера %s изменена: %.2f -> %.2f%n";

    public static final String SERVICE_ADDED = "Услуга '%s' добавлена в систему%n";
    public static final String SERVICE_NOT_FOUND = "Ошибка: услуга с ID %d не найдена%n";
    public static final String SERVICE_PRICE_CHANGED = "Цена услуги '%s' изменена: %.2f -> %.2f%n";

    public static final String NOT_NEGATIVE_PRICE = "Ошибка: цена не может быть отрицательной";

    public static final String ROOM_DETAILS =
            "=== ДЕТАЛИ НОМЕРА ===\n" +
                    "ID: %d\n" +
                    "Номер: %s\n" +
                    "Цена за ночь: %.2f\n" +
                    "Вместимость: %d человек\n" +
                    "Звезды: %d\n" +
                    "Статус: %s\n" +
                    "Текущий гость: %s\n" +
                    "Всего бронирований: %d\n" +
                    "Последнее бронирование: %s";

    public static final String ROOM_NOT_FOUND_DETAILS = "Номер не найден";

    public static final String GUEST_OR_AMENITY_NOT_FOUND = "Ошибка: гость или услуга не найдены%n";
    public static final String NO_ACTIVE_BOOKING_FOR_GUEST = "Ошибка: нет активного бронирования у гостя%n";
    public static final String AMENITY_ADDED_TO_GUEST = "Услуга '%s' добавлена гостю %s. Стоимость: %.2f%n";

    public static final String GUEST_AMENITY_ADDED = "Услуга '%s' успешно добавлена гостю %s%n";
    public static final String INVALID_DATE_RANGE = "Ошибка: дата выселения не может быть раньше даты заселения%n";
    public static final String ROOM_OCCUPIED_ON_DATE = "Ошибка: номер %d уже занят на дату %s%n";
    public static final String NO_ACTIVE_BOOKING = "Ошибка: нет активного бронирования%n";
}
