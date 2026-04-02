package by.slava_borisov.hoteladmin.util;

public final class Messages {

    private Messages() {
    }

    public static final String DEFAULT_ERROR_MESSAGE = "Произошла ошибка. Повторите попытку.";
    public static final String INVALID_DATE_RANGE = "Дата заезда должна быть раньше даты выезда и не может быть в прошлом.";

    public static final String ROOM_NOT_FOUND_EXCEPTION = "Комната с номером %d не найдена.";
    public static final String ROOM_NOT_AVAILABLE_EXCEPTION = "Номер %d не доступен для заселения.";
    public static final String ROOM_STATUS_CHANGE_DISABLED = "Изменение статуса номера отключено в настройках.";
    public static final String NOT_NEGATIVE_PRICE = "Цена не может быть отрицательной";
    public static final String ROOM_NOT_FOUND_BY_NUMBER_EXCEPTION = "Комната с номером %s не найдена";

    public static final String DUPLICATE_ROOM_NUMBER = "Комната с номером %s уже существует";
    public static final String GUEST_NOT_FOUND_EXCEPTION = "Гость с ID %d не найден.";
    public static final String GUEST_PHONE_NOT_FOUND_EXCEPTION = "Гость с телефоном %s не найден.";

    public static final String AMENITY_NOT_FOUND_EXCEPTION = "Услуга с ID %d не найдена.";
    public static final String POSTGRESQL_DRIVER_NOT_FOUND = "PostgreSQL драйвер не найден";

    public static final String QUANTITY_MUST_BE_POSITIVE = "'Количество' должно быть положительным числом";

    public static final String BOOKING_NOT_FOUND_EXCEPTION = "Бронирование с ID %d не найден.";

    public static final String LIQUIBASE_CHANGELOG_PATH = "db/changelog-master.yaml";
    public static final String LIQUIBASE_MIGRATION_SUCCESS = "Liquibase: миграции выполнены";
    public static final String LIQUIBASE_MIGRATION_ERROR = "Ошибка Liquibase";
}
