package by.slava_borisov.hoteladmin.util;

public final class Messages {

    private Messages() {
    }

    /// COMMON / GENERAL
    public static final String DEFAULT_ERROR_MESSAGE = "Произошла ошибка. Повторите попытку.";
    public static final String OPERATION_SUCCESS = "Операция выполнена успешно.";
    public static final String SUCCESS_OPERATION = "Операция успешно выполнена";
    public static final String ERROR_PREFIX = "Ошибка: ";
    public static final String INVALID_DATE = "Неверный формат даты. Введите дату в формате YYYY-MM-DD.";
    public static final String INVALID_DATE_RANGE = "Дата заезда должна быть раньше даты выезда и не может быть в прошлом.";
    public static final String AVAILABLE = "СВОБОДНА";
    public static final String OCCUPIED = "ЗАНЯТА";
    public static final String UNDER_MAINTENANCE = "НА РЕМОНТЕ";
    public static final String CLEANING = "УБОРКА";



    /// ROOM
    public static final String ROOM_NOT_FOUND = "Комната не найдена: id=%d";
    public static final String ROOM_NOT_FOUND_DETAILS = "Номер не найден";
    public static final String ROOM_NOT_FOUND_EXCEPTION = "Комната с номером %d не найдена.";
    public static final String ROOM_NOT_AVAILABLE_EXCEPTION = "Номер %d не доступен для заселения.";
    public static final String ROOM_ADDED = "Номер %s добавлен в систему%n";
    public static final String DUPLICATE_ROOM_NUMBER_EXCEPTION = "Номер %s уже существует.";
    public static final String ROOM_STATUS_CHANGE_DISABLED = "Изменение статуса номера отключено в настройках.";
    public static final String STATUS_CHANGED_SUCCESSFULLY = "Статус номера успешно изменён.";
    public static final String ROOM_PAYMENT = "Сумма к оплате: %.2f руб.";
    public static final String NOT_NEGATIVE_PRICE = "Ошибка: цена не может быть отрицательной";
    public static final String ROOM_NOT_FOUND_BY_NUMBER = "Комната с номером %s не найдена";

    /// ROOM STATUS
    public static final String STATUS_UNKNOWN = "НЕИЗВЕСТНО";
    public static final String STATUS_AVAILABLE = "СВОБОДЕН";
    public static final String STATUS_OCCUPIED = "ЗАНЯТ";
    public static final String STATUS_CLEANING = "УБОРКА";


    /// GUEST
    public static final String GUEST_NOT_FOUND_EXCEPTION = "Гость с ID %d не найден.";
    public static final String GUEST_PHONE_NOT_FOUND_EXCEPTION = "Гость с телефоном %s не найден.";
    public static final String GUEST_OR_AMENITY_NOT_FOUND = "Гость или услуга не найдены%n";


    public static final String AMENITY_NOT_FOUND_EXCEPTION = "Услуга с ID %d не найдена.";
    public static final String AMENITY_ADDED = "Услуга '%s' добавлена в систему%n";
    public static final String AMENITY_PRICE_CHANGED_SIMPLE = "Цена услуги с ID %d изменена на %.2f%n";
    public static final String AMENITY_ADDED_TO_GUEST = "Услуга '%s' добавлена гостю %s. Стоимость: %.2f%n";


    /// AMENITY USAGE
    public static final String QUANTITY_MUST_BE_POSITIVE = "Quantity must be positive";


    /// BOOKING
    public static final String BOOKING_NOT_FOUND_EXCEPTION = "Бронирование с ID %d не найден.";
    public static final String CHECKIN_SUCCESS = "Гость %s заселен в номер %s%n";
    public static final String CHECKOUT_SUCCESS_SIMPLE = "Гость успешно выселен из номера%n";


    /// CONSOLE UI
    public static final String EXIT_MESSAGE = "Завершение работы...";
    public static final String INVALID_CHOICE = "Ошибка: неверный выбор!";


    /// CONSOLE VIEW
    public static final String HEADER_LEFT = "=========== ";
    public static final String HEADER_RIGHT = " ============";
    public static final String SEPARATOR_LINE = "--------------------------------";


    /// BOOKING VIEW
    public static final String NO_BOOKINGS = "Нет бронирований";
    public static final String BOOKINGS_HEADER = "История бронирований";


    /// GUEST VIEW
    public static final String AMENITIES_HEADER = "Услуги гостя";
    public static final String AMENITIES_SORTED_BY_PRICE_HEADER = "Список услуг, отсортированных по цене";
    public static final String AMENITIES_SORTED_BY_CATEGORY_HEADER = "Список услуг, отсортированных по категории";
    public static final String NO_AMENITIES = "Нет услуг";
    public static final String NO_SERVICES = "Нет услуг";


    /// REPORT VIEW
    public static final String AVAILABLE_ROOMS_COUNT = "Свободных номеров: %d";
    public static final String CURRENT_GUESTS_COUNT = "Гостей в отеле: %d";
    public static final String AVAILABLE_ROOMS_ON_DATE_HEADER = "Свободные номера на %s";
    public static final String NO_AVAILABLE_ROOMS = "Нет свободных номеров";
    public static final String TOTAL_AVAILABLE_ROOMS_FORMAT = "Всего доступных номеров: %d%n";


    /// ROOM VIEW
    public static final String ROOMS_HEADER = "Список номеров";
    public static final String NO_ROOMS = "Нет доступных номеров";
    public static final String ROOM_DETAILS_HEADER = "Детали номера";
    public static final String TOTAL_ROOMS_FORMAT = "Всего номеров: %d%n";
    public static final String ROOM_FULL_INFO_FORMAT =
            "%d. Номер %s | ID: %s | Цена: %.2f₽ | Вместимость: %d чел. | Звёзды: %d* | Статус: %s%n";


    /// PREFIXES
    public static final String ID_PREFIX = "ID: ";
    public static final String ROOM_NUMBER_PREFIX = "Номер: ";
    public static final String PRICE_PER_NIGHT_FORMAT = "Цена за ночь: %.2f₽";
    public static final String CAPACITY_PREFIX = "Вместимость: ";
    public static final String STARS_PREFIX = "Звёзды: ";
    public static final String STATUS_PREFIX = "Статус: ";


    /// SUFFIXES
    public static final String PEOPLE_SUFFIX = " чел.";
    public static final String STAR_SYMBOL = "*";


    /// FORMATS
    public static final String GUEST_LIST_ROW_SIMPLE = "%d) %s (ID=%d), phone=%s%n";
    public static final String GUEST_LIST_ITEM = "%d. %s | ID: %d\n";
    public static final String AMENITY_LIST_FORMAT = "%d. %s | Цена: %.2f₽ | Категория: %s%n";
    public static final String AMENITY_USAGE_FORMAT = "%d. %s | Количество: %d | Цена: %.2f₽ | Дата: %s%n";
    public static final String PRICE_LIST_ROW = "%d. %s | ID: %d | Цена: %.2f₽ | Категория: %s%n";


    /// MENU STRUCTURE
    public static final String MENU_SEPARATOR = "\n";
    public static final String MENU_PROMPT = "Выберите действие: ";
    public static final String MENU_ENTER_NUMBER_EXC = "Ошибка: введите число";


    /// MENU TITLES
    public static final String MAIN_MENU_TITLE = "ГЛАВНОЕ МЕНЮ";

    /// MENU ITEMS
    public static final String MENU_ITEM_ADD_AMENITY_TO_GUEST = "Добавить услугу гостю";
    public static final String MENU_ITEM_SHOW_ALL_GUESTS = "Показать всех гостей";
    public static final String MENU_ITEM_CHECK_IN_MAIN = "Заселить гостя";
    public static final String MENU_ITEM_CHECK_OUT_MAIN = "Выселить гостя";
    public static final String MENU_ITEM_EXIT = "Выход";
    public static final String MENU_ITEM_ADD_AMENITY = "Добавить услугу";
    public static final String MENU_ITEM_CHANGE_AMENITY_PRICE = "Изменить цену услуги";
    public static final String MENU_ITEM_SHOW_ALL_ROOMS = "Показать все номера";
    public static final String MENU_ITEM_CHANGE_ROOM_PRICE = "Изменить цену номера";
    public static final String MENU_ITEM_SHOW_AVAILABLE_ROOMS = "Показать свободные номера";
    public static final String MENU_ITEM_ADD_ROOM = "Добавить номер";
    public static final String MENU_ITEM_CHANGE_ROOM_STATUS = "Изменить статус номера";
    public static final String MENU_ITEM_SHOW_ROOM_DETAILS = "Посмотреть детали номера";
    public static final String MENU_ITEM_AVAILABLE_ROOMS_COUNT = "Общее число свободных номеров";
    public static final String MENU_ITEM_GUESTS_COUNT = "Общее число постояльцев";
    public static final String MENU_ITEM_AVAILABLE_ROOMS_ON_DATE = "Список номеров по дате";
    public static final String MENU_ITEM_CALCULATE_ROOM_PAYMENT = "Сумма оплаты за номер";
    public static final String MENU_ITEM_SHOW_LAST_BOOKINGS = "Последние постояльцы номера";
    public static final String MENU_ITEM_SHOW_GUEST_AMENITIES = "Список услуг постояльца";
    public static final String MENU_ITEM_SHOW_PRICES = "Цены услуг";
    public static final String MENU_ITEM_SHOW_AMENITIES_SORTED_BY_PRICE = "Сортировка услуг по цене";
    public static final String MENU_ITEM_SHOW_AMENITIES_SORTED_BY_CATEGORY = "Сортировка услуг по категории";
    public static final String MENU_ITEM_SHOW_ROOMS_SORTED_BY_PRICE = "Показать номера, отсортированные по цене";
    public static final String MENU_ITEM_SHOW_ROOMS_SORTED_BY_STARS = "Показать номера, отсортированные по количеству звёзд";

    /// ACTION HEADERS
    public static final String ADD_AMENITY_HEADER = "=== Добавить услугу ===";
    public static final String ADD_AMENITY_TO_GUEST_HEADER = "=== Добавление услуги гостю ===";
    public static final String ADD_ROOM_HEADER = "=== Добавить номер ===";
    public static final String CHANGE_AMENITY_PRICE_HEADER = "=== Изменить цену услуги ===";
    public static final String CHANGE_ROOM_PRICE_HEADER = "=== Изменение цены номера ===";
    public static final String CHECK_IN_HEADER = "=== Заселение гостя ===";
    public static final String CHECK_OUT_HEADER = "=== Выселение гостя ===";
    public static final String SHOW_ALL_GUESTS_HEADER = "=== Список всех гостей ===";
    public static final String SHOW_AVAILABLE_ROOMS_HEADER = "=== Показать свободные номера ===";
    public static final String SHOW_AVAILABLE_ROOMS_ON_DATE_HEADER = "=== Номера, свободные на определённую дату ===";
    public static final String SHOW_AMENITIES_SORTED_BY_PRICE_HEADER = "Услуги, отсортированные по цене";
    public static final String SHOW_AMENITIES_SORTED_BY_CATEGORY_HEADER = "Услуги, отсортированные по категории";
    public static final String SHOW_LAST_BOOKINGS_HEADER = "=== Последние 3 бронирования номера ===";
    public static final String SHOW_ROOMS_SORTED_BY_PRICE_HEADER = "Список номеров, отсортированных по цене";
    public static final String PRICE_LIST_HEADER = "=========== Список услуг ===========\n";


    /// INPUT PROMPTS
    public static final String ENTER_AMENITY_ID = "Введите ID услуги: ";
    public static final String ENTER_AMENITY_NAME = "Введите название услуги: ";
    public static final String ENTER_AMENITY_PRICE = "Введите цену услуги: ";
    public static final String ENTER_AMENITY_CATEGORY = "Введите категорию услуги: ";
    public static final String ENTER_GUEST_ID = "Введите ID гостя: ";
    public static final String ENTER_QUANTITY = "Количество: ";
    public static final String ENTER_GUEST_NAME = "Введите имя гостя: ";
    public static final String ENTER_GUEST_PHONE = "Введите телефон: ";
    public static final String ENTER_ROOM_NUMBER = "Введите номер комнаты: ";
    public static final String ENTER_ROOM_PRICE = "Введите цену за ночь: ";
    public static final String ENTER_ROOM_CAPACITY = "Введите вместимость: ";
    public static final String ENTER_ROOM_STARS = "Введите количество звёзд: ";
    public static final String ENTER_NEW_PRICE = "Введите новую цену: ";
    public static final String ENTER_CHECK_IN_DATE = "Дата заезда (YYYY-MM-DD): ";
    public static final String ENTER_CHECK_OUT_DATE = "Дата выезда (YYYY-MM-DD): ";
    public static final String ENTER_DATE = "Введите дату (YYYY-MM-DD): ";
    public static final String CHOOSE_STATUS = "Выберите статус:";
    public static final String INVALID_STATUS = "Неверный выбор статуса";
    public static final String CHOOSE_GUEST = "Выберите гостя:\n";

    /// LiquibaseRunner
    public static final String LIQUIBASE_CHANGELOG_PATH = "db/changelog-master.yaml";
    public static final String LIQUIBASE_MIGRATION_SUCCESS = "Liquibase: миграции выполнены";
    public static final String LIQUIBASE_MIGRATION_ERROR = "Ошибка Liquibase";

    public static final String BOOKING_INFO_FORMAT = "bookingId=%d, guestId=%d, roomId=%d, %s -> %s";
    public static final String AMENITY_ID_PREFIX = "amenityId=";
    public static final String CONSOLE_UI_CANNOT_BE_NULL = "ConsoleUI cannot be null";
    public static final String MENU_ITEM_FORMAT = "%d. %s";
}
