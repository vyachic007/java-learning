package by.slava_borisov.hoteladmin.util;

public final class Messages {

    private Messages() {
    }

    /// COMMON / GENERAL
    public static final String DEFAULT_ERROR_MESSAGE = "Произошла ошибка. Повторите попытку.";
    public static final String FAILED_TO_INJECT_DEPENDENCIES = "Не удалось внедрить зависимости.";
    public static final String OPERATION_SUCCESS = "Операция выполнена успешно.";
    public static final String SUCCESS_OPERATION = "Операция успешно выполнена";
    public static final String ERROR_PREFIX = "Ошибка: ";
    public static final String NO_VALUE = "Нет";
    public static final String INVALID_DATE = "Неверный формат даты. Введите дату в формате YYYY-MM-DD.";
    public static final String INVALID_DATE_RANGE = "Дата заезда должна быть раньше даты выезда и не может быть в прошлом.";
    public static final String AVAILABLE = "СВОБОДНА";
    public static final String OCCUPIED = "ЗАНЯТА";
    public static final String MAINTENANCE = "РЕМОНТ";


    /// ROOM
    public static final String ROOM_NOT_FOUND = "Комната не найдена: id=%d";
    public static final String ROOM_NOT_FOUND_DETAILS = "Номер не найден";
    public static final String ROOM_NOT_FOUND_ID = "Room not found, id=%d";
    public static final String ROOM_NOT_FOUND_EXCEPTION = "Комната с номером %d не найдена.";
    public static final String ROOM_NOT_AVAILABLE = "Номер %d не доступен для заселения.";
    public static final String ROOM_NOT_AVAILABLE_EXCEPTION = "Номер %d не доступен для заселения.";
    public static final String ROOM_OCCUPIED_ON_DATE = "Номер %d уже занят на дату %s%n";
    public static final String ROOM_NOT_OCCUPIED = "Ошибка: номер %d не занят%n";
    public static final String ROOM_ADDED = "Номер %s добавлен в систему%n";
    public static final String ROOM_NUMBER_EXISTS = "Номер с таким номером уже существует";
    public static final String DUPLICATE_ROOM_NUMBER = "Номер комнаты уже существует: %s";
    public static final String DUPLICATE_ROOM_NUMBER_EXCEPTION = "Номер %s уже существует.";
    public static final String ROOM_STATUS_CHANGED = "Статус номера %s изменен: %s -> %s%n";
    public static final String ROOM_STATUS_CHANGE_DISABLED = "Изменение статуса номера отключено в настройках.";
    public static final String STATUS_CHANGED_SUCCESSFULLY = "Статус номера успешно изменён.";
    public static final String ROOM_PRICE_CHANGED = "Цена номера %s изменена: %.2f -> %.2f%n";
    public static final String ROOM_PAYMENT = "Сумма к оплате: %.2f руб.";
    public static final String ROOM_ID_MUST_BE_POSITIVE = "Room id must be positive";
    public static final String NOT_NEGATIVE_PRICE = "Ошибка: цена не может быть отрицательной";
    public static final String NOTNEGATIVEPRICEPERNIGHT = "Цена за ночь не может быть отрицательной.";
    public static final String FAILED_TO_INSERT_ROOM_NO_KEY = "Failed to insert room (no generated key)";


    /// ROOM STATUS
    public static final String STATUS_UNKNOWN = "Неизвестно";
    public static final String STATUS_AVAILABLE = "Свободен";
    public static final String STATUS_OCCUPIED = "Занят";
    public static final String STATUS_UNDER_MAINTENANCE = "Ремонт";
    public static final String STATUS_CLEANING = "Уборка";


    /// GUEST
    public static final String GUEST_NOT_FOUND = "Гость с ID %d не найден.";
    public static final String GUEST_NOT_FOUND_EXCEPTION = "Гость с ID %d не найден.";
    public static final String GUEST_OR_AMENITY_NOT_FOUND = "Гость или услуга не найдены%n";
    public static final String GUEST_ADDED = "Гость %s успешно добавлен%n";
    public static final String GUEST_DELETED = "Гость с ID %d успешно удалён%n";
    public static final String FAILED_TO_INSERT_GUEST_NO_KEY = "Failed to insert guest: no generated key";


    /// AMENITY
    public static final String AMENITY_NOT_FOUND = "Услуга не найдена: id=%d";
    public static final String AMENITY_NOT_FOUND_EXCEPTION = "Услуга с ID %d не найдена.";
    public static final String AMENITY_ADDED = "Услуга '%s' добавлена в систему%n";
    public static final String AMENITY_PRICE_CHANGED = "Цена услуги '%s' изменена: %.2f -> %.2f%n";
    public static final String AMENITY_PRICE_CHANGED_SIMPLE = "Цена услуги с ID %d изменена на %.2f%n";
    public static final String AMENITY_ADDED_TO_GUEST = "Услуга '%s' добавлена гостю %s. Стоимость: %.2f%n";
    public static final String GUEST_AMENITY_ADDED = "Услуга '%s' успешно добавлена гостю %s%n";
    public static final String SERVICE_NOT_FOUND = "Услуга с ID %d не найдена.";
    public static final String FAILED_TO_INSERT_AMENITY_NO_KEY = "Failed to insert amenity (no generated key)";


    /// AMENITY USAGE
    public static final String AMENITY_USAGE_NOT_FOUND = "AmenityUsage not found, id=%d";
    public static final String FAILED_TO_INSERT_AMENITY_USAGE_NO_KEY = "Failed to insert amenity usage (no generated key)";
    public static final String QUANTITY_MUST_BE_POSITIVE = "Quantity must be positive";
    public static final String AMENITY_USAGE_INSERT_FAILED = "Failed to insert amenity usage";


    /// BOOKING
    public static final String BOOKING_NOT_FOUND = "Бронирование с ID %d не найдено.";
    public static final String BOOKING_NOT_FOUND_ID = "Booking not found, id=%d";
    public static final String BOOKING_NOT_FOUND_EXCEPTION = "Бронирование с ID %d не найден.";
    public static final String NO_ACTIVE_BOOKING = "Нет активного бронирования%n";
    public static final String NO_ACTIVE_BOOKING_FOR_GUEST = "Нет активного бронирования у гостя%n";
    public static final String CHECKIN_ERROR = "Ошибка: невозможно заселить в номер %d%n";
    public static final String CHECKIN_SUCCESS = "Гость %s заселен в номер %s%n";
    public static final String CHECKOUT_SUCCESS = "Гость %s выселен из номера %s%n";
    public static final String CHECKOUT_SUCCESS_SIMPLE = "Гость успешно выселен из номера%n";
    public static final String FAILED_TO_INSERT_BOOKING_NO_KEY = "Failed to insert booking (no generated key)";
    public static final String BOOKING_INSERT_FAILED = "Failed to insert booking (no generated key)";


    /// BOOKING STATUS
    public static final String BOOKING_STATUS_ACTIVE = "Активно";
    public static final String BOOKING_STATUS_COMPLETED = "Завершено";


    /// CONSOLE UI
    public static final String DATE_FORMAT_ERROR = "Ошибка формата даты. Введите дату (YYYY-MM-DD): ";
    public static final String EXIT_MESSAGE = "Завершение работы...";
    public static final String INVALID_CHOICE = "Ошибка: неверный выбор!";
    public static final String NO_DATA_TO_DISPLAY = "Нет данных для отображения";


    /// CONSOLE VIEW
    public static final String HEADER_LEFT = "=========== ";
    public static final String HEADER_RIGHT = " ============";
    public static final String SEPARATOR_LINE = "--------------------------------";


    /// BOOKING VIEW
    public static final String BILL_HEADER = "Счёт к оплате";
    public static final String BILL_AMOUNT = "Сумма к оплате: %.2f руб.";
    public static final String NO_BOOKINGS = "Нет бронирований";
    public static final String BOOKINGS_HEADER = "История бронирований";
    public static final String BOOKING_LIST_FORMAT = "%d. Гость: %s | Номер: %d | %s - %s | Статус: %s%n";


    /// GUEST VIEW
    public static final String GUESTS_HEADER = "Список гостей";
    public static final String NO_GUESTS = "Нет гостей";
    public static final String GUEST_DETAILS_HEADER = "Детали гостя";
    public static final String GUEST_HISTORY_HEADER = "История гостя: %s";
    public static final String ALL_AMENITIES_HEADER = "Список услуг";
    public static final String AMENITIES_HEADER = "Услуги гостя";
    public static final String AMENITIES_SORTED_BY_PRICE_HEADER = "Список услуг, отсортированных по цене";
    public static final String AMENITIES_SORTED_BY_CATEGORY_HEADER = "Список услуг, отсортированных по категории";
    public static final String NO_AMENITIES = "Нет услуг";
    public static final String NO_BOOKING_HISTORY = "Нет истории бронирований";
    public static final String NO_SERVICES = "Нет услуг";


    /// REPORT VIEW
    public static final String REPORT_HEADER = "Отчёт по отелю";
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
    public static final String NAME_PREFIX = "Имя: ";
    public static final String PHONE_PREFIX = "Телефон: ";
    public static final String ROOM_PREFIX = "Номер: ";
    public static final String CHECK_IN_PREFIX = "Дата заезда: ";
    public static final String CHECK_OUT_PREFIX = "Дата выезда: ";
    public static final String CURRENT_BOOKING_PREFIX = "Текущее бронирование: ";
    public static final String ROOM_NUMBER_PREFIX = "Номер: ";
    public static final String PRICE_PER_NIGHT_FORMAT = "Цена за ночь: %.2f₽";
    public static final String CAPACITY_PREFIX = "Вместимость: ";
    public static final String STARS_PREFIX = "Звёзды: ";
    public static final String STATUS_PREFIX = "Статус: ";
    public static final String CURRENT_GUEST_PREFIX = "Текущий гость: ";
    public static final String TOTAL_BOOKINGS_PREFIX = "Всего бронирований: ";
    public static final String LAST_BOOKING_PREFIX = "Последнее бронирование: ";
    public static final String CATEGORY_PREFIX = "Категория: ";


    /// SUFFIXES
    public static final String PEOPLE_SUFFIX = " чел.";
    public static final String STAR_SYMBOL = "*";


    /// FORMATS
    public static final String GUEST_LIST_FORMAT = "%d. %s | Тел: %s | %s | %s%n";
    public static final String GUEST_LIST_ROW = "%d. %s | ID: %d | Тел: %s | Номер: %s | Выезд: %s%n";
    public static final String GUEST_LIST_ROW_SIMPLE = "%d) %s (ID=%d), phone=%s%n";
    public static final String GUEST_LIST_ITEM = "%d. %s | ID: %d\n";
    public static final String TOTAL_GUESTS_FORMAT = "Всего гостей: %d%n";
    public static final String GUEST_HISTORY_FORMAT = "%d. Номер %d | %s - %s | Статус: %s%n";
    public static final String AMENITY_LIST_FORMAT = "%d. %s | Цена: %.2f₽ | Категория: %s%n";
    public static final String AMENITY_USAGE_FORMAT = "%d. %s | Количество: %d | Цена: %.2f₽ | Дата: %s%n";
    public static final String AMENITY_PRICE_FORMAT = "%d. %s | Цена: %.2f₽%n";
    public static final String ROOM_INFO_FORMAT = "Номер: %d";
    public static final String GUEST_ROOM_INFO_FORMAT = "Номер: %d";
    public static final String CHECK_OUT_INFO_FORMAT = "Выезд: %s";
    public static final String PRICE_LIST_ROW = "%d. %s | ID: %d | Цена: %.2f₽ | Категория: %s%n";


    /// ROOM DETAILS
    public static final String ROOM_DETAILS =
            "=== ДЕТАЛИ НОМЕРА ===\nID: %d\nНомер: %s\nЦена за ночь: %.2f\nВместимость:" +
                    " %d человек\nЗвезды: %d\nСтатус: %s\nТекущий гость: %s\nВсего бронирований:" +
                    " %d\nПоследнее бронирование: %s";


    /// MENU STRUCTURE
    public static final String MENU_SEPARATOR = "\n";
    public static final String MENU_ITEM_BACK = "0. Назад";
    public static final String MENU_PROMPT = "Выберите действие: ";
    public static final String MENU_ENTER_NUMBER_EXC = "Ошибка: введите число";


    /// MENU TITLES
    public static final String MAIN_MENU_TITLE = "ГЛАВНОЕ МЕНЮ";
    public static final String BOOKINGS_MENU_TITLE = "МЕНЮ УПРАВЛЕНИЯ БРОНИРОВАНИЯМИ";
    public static final String GUESTS_MENU_TITLE = "МЕНЮ УПРАВЛЕНИЯ ГОСТЯМИ";
    public static final String PRICES_MENU_TITLE = "МЕНЮ УПРАВЛЕНИЯ ЦЕНАМИ И УСЛУГАМИ";
    public static final String REPORTS_MENU_TITLE = "МЕНЮ ОТЧЁТОВ";
    public static final String ROOMS_MENU_TITLE = "МЕНЮ УПРАВЛЕНИЯ НОМЕРАМИ";


    /// MENU ITEMS
    public static final String MENU_ITEM_CHECK_IN = "Поселить гостя";
    public static final String MENU_ITEM_CHECK_OUT = "Выселить гостя";
    public static final String MENU_ITEM_SHOW_GUEST_BILL = "Показать счёт гостя";
    public static final String MENU_ITEM_ADD_AMENITY_TO_GUEST = "Добавить услугу гостю";
    public static final String MENU_ITEM_BACK_TO_MAIN = "Назад в главное меню";
    public static final String MENU_ITEM_SHOW_ALL_GUESTS = "Показать всех гостей";
    public static final String MENU_ITEM_ADD_GUEST = "Добавить гостя";
    public static final String MENU_ITEM_FIND_GUEST = "Найти гостя по ID";
    public static final String MENU_ITEM_SHOW_GUEST_HISTORY = "Показать историю гостя";
    public static final String MENU_ITEM_DELETE_GUEST = "Удалить гостя";
    public static final String MENU_ITEM_CHECK_IN_MAIN = "Заселить гостя";
    public static final String MENU_ITEM_CHECK_OUT_MAIN = "Выселить гостя";
    public static final String MENU_ITEM_ADD_AMENITY_MAIN = "Добавить услугу гостю";
    public static final String MENU_ITEM_EXIT = "Выход";
    public static final String MENU_ITEM_SHOW_ALL_AMENITIES = "Показать все услуги";
    public static final String MENU_ITEM_ADD_AMENITY = "Добавить услугу";
    public static final String MENU_ITEM_CHANGE_AMENITY_PRICE = "Изменить цену услуги";
    public static final String MENU_ITEM_GENERAL_REPORT = "Общая информация (свободные номера, гостей)";
    public static final String MENU_ITEM_SHOW_ALL_ROOMS = "Показать все номера";
    public static final String MENU_ITEM_CHANGE_ROOM_PRICE = "Изменить цену номера";
    public static final String MENU_ITEM_SHOW_AVAILABLE_ROOMS = "Показать свободные номера";
    public static final String MENU_ITEM_ADD_ROOM = "Добавить номер";
    public static final String MENU_ITEM_CHANGE_ROOM_STATUS = "Изменить статус номера";
    public static final String MENU_ITEM_SHOW_ROOM_DETAILS = "Посмотреть детали номера";
    public static final String MENU_ITEM_AVAILABLE_ROOMS_COUNT = "Общее число свободных номеров";
    public static final String MENU_ITEM_GUESTS_COUNT = "Общее число постояльцев";
    public static final String MENU_ITEM_AVAILABLE_ROOMS_ON_DATE = "Список номеров по дате";
    public static final String MENU_ITEM_CALCULATE_GUEST_PAYMENT = "Сумма оплаты за номер";
    public static final String MENU_ITEM_CALCULATE_ROOM_PAYMENT = "Сумма оплаты за номер";
    public static final String MENU_ITEM_SHOW_LAST_BOOKINGS = "Последние постояльцы номера";
    public static final String MENU_ITEM_SHOW_GUEST_AMENITIES = "Список услуг постояльца";
    public static final String MENU_ITEM_SHOW_PRICES = "Цены услуг";
    public static final String MENU_ITEM_SAVE_GUESTS = "Сохранить гостей";
    public static final String MENU_ITEM_SAVE_ROOMS = "Сохранить номера";
    public static final String MENU_ITEM_SAVE_BOOKINGS = "Сохранить бронирования";
    public static final String MENU_ITEM_SAVE_AMENITIES = "Сохранить услуги";
    public static final String MENU_ITEM_LOAD_GUESTS = "Загрузить гостей";
    public static final String MENU_ITEM_LOAD_ROOMS = "Загрузить номера";
    public static final String MENU_ITEM_LOAD_BOOKINGS = "Загрузить бронирования";
    public static final String MENU_ITEM_LOAD_AMENITIES = "Загрузить услуги";
    public static final String MENU_ITEM_SHOW_AMENITIES_SORTED_BY_PRICE = "Сортировка услуг по цене";
    public static final String MENU_ITEM_SHOW_AMENITIES_SORTED_BY_CATEGORY = "Сортировка услуг по категории";
    public static final String MENU_ITEM_SHOW_ROOMS_SORTED_BY_PRICE = "Показать номера, отсортированные по цене";
    public static final String MENU_ITEM_SHOW_ROOMS_SORTED_BY_STARS = "Показать номера, отсортированные по количеству звёзд";
    public static final String MENU_ITEM_SAVE_AMENITY_USAGE = "Сохранить использование услуг";
    public static final String MENU_ITEM_LOAD_AMENITY_USAGE = "Загрузить использование услуг";


    /// ACTION HEADERS
    public static final String ADD_AMENITY_HEADER = "=== Добавить услугу ===";
    public static final String ADD_AMENITY_TO_GUEST_HEADER = "=== Добавление услуги гостю ===";
    public static final String ADD_GUEST_HEADER = "=== Добавить гостя ===";
    public static final String ADD_ROOM_HEADER = "=== Добавить номер ===";
    public static final String CHANGE_AMENITY_PRICE_HEADER = "=== Изменить цену услуги ===";
    public static final String CHANGE_ROOM_PRICE_HEADER = "=== Изменение цены номера ===";
    public static final String CHANGE_ROOM_STATUS_HEADER = "=== Изменить статус номера ===";
    public static final String CHECK_IN_HEADER = "=== Заселение гостя ===";
    public static final String CHECK_OUT_HEADER = "=== Выселение гостя ===";
    public static final String DELETE_GUEST_HEADER = "=== Удалить гостя ===";
    public static final String FIND_GUEST_BY_ID_HEADER = "=== Найти гостя по ID ===";
    public static final String SHOW_ALL_AMENITIES_HEADER = "=== Список всех услуг ===";
    public static final String SHOW_ALL_GUESTS_HEADER = "=== Список всех гостей ===";
    public static final String SHOW_AVAILABLE_ROOMS_HEADER = "=== Показать свободные номера ===";
    public static final String SHOW_AVAILABLE_ROOMS_ON_DATE_HEADER = "=== Номера, свободные на определённую дату ===";
    public static final String SHOW_GUEST_BILL_HEADER = "=== Счёт гостя ===";
    public static final String SHOW_GUEST_HISTORY_HEADER = "=== История гостя ===";
    public static final String SHOW_AMENITIES_SORTED_BY_PRICE_HEADER = "Услуги, отсортированные по цене";
    public static final String SHOW_AMENITIES_SORTED_BY_CATEGORY_HEADER = "Услуги, отсортированные по категории";
    public static final String SHOW_LAST_BOOKINGS_HEADER = "=== Последние 3 бронирования номера ===";
    public static final String SHOW_ROOM_DETAILS_HEADER = "=== Детали номера ===";
    public static final String SHOW_ROOMS_SORTED_BY_PRICE_HEADER = "Список номеров, отсортированных по цене";
    public static final String PRICE_LIST_HEADER = "=========== Список услуг ===========\n";
    public static final String SAVE_GUESTS_HEADER = "Сохранение гостей в файл";
    public static final String SAVE_ROOMS_HEADER = "Сохранение номеров в файл";
    public static final String SAVE_AMENITIES_HEADER = "Сохранение услуг в файл";
    public static final String SAVE_BOOKINGS_HEADER = "Сохранение бронирований в файл";
    public static final String SAVE_AMENITY_USAGE_HEADER = "Сохранение использования услуг в файл";
    public static final String LOAD_GUESTS_HEADER = "Загрузка гостей из файла";
    public static final String LOAD_BOOKINGS_HEADER = "Загрузка бронирований из файла";
    public static final String LOAD_ROOMS_HEADER = "Загрузка номеров из файла";
    public static final String LOAD_AMENITY_USAGE_HEADER = "Загрузка использования услуг из файла";
    public static final String LOAD_AMENITIES_HEADER = "Загрузка услуг из файла";


    /// INPUT PROMPTS
    public static final String ENTER_AMENITY_ID = "Введите ID услуги: ";
    public static final String ENTER_AMENITY_NAME = "Введите название услуги: ";
    public static final String ENTER_AMENITY_PRICE = "Введите цену услуги: ";
    public static final String ENTER_AMENITY_CATEGORY = "Введите категорию услуги: ";
    public static final String ENTER_GUEST_ID = "Введите ID гостя: ";
    public static final String ENTER_QUANTITY = "Количество: ";
    public static final String ENTER_GUEST_NAME = "Введите имя гостя: ";
    public static final String ENTER_GUEST_PHONE = "Введите телефон: ";
    public static final String ENTER_ROOM_ID = "Введите ID номера: ";
    public static final String ENTER_ROOM_NUMBER = "Введите номер комнаты: ";
    public static final String ENTER_ROOM_PRICE = "Введите цену за ночь: ";
    public static final String ENTER_ROOM_CAPACITY = "Введите вместимость: ";
    public static final String ENTER_ROOM_STARS = "Введите количество звёзд: ";
    public static final String ENTER_NEW_PRICE = "Введите новую цену: ";
    public static final String SELECT_NEW_STATUS = "Выберите новый статус:";
    public static final String STATUS_OPTION_AVAILABLE = "1. AVAILABLE";
    public static final String STATUS_OPTION_OCCUPIED = "2. OCCUPIED";
    public static final String STATUS_OPTION_MAINTENANCE = "3. MAINTENANCE";
    public static final String YOUR_CHOICE = "Выбор: ";
    public static final String ENTER_CHECK_IN_DATE = "Дата заезда (YYYY-MM-DD): ";
    public static final String ENTER_CHECK_OUT_DATE = "Дата выезда (YYYY-MM-DD): ";
    public static final String ENTER_DATE = "Введите дату (YYYY-MM-DD): ";
    public static final String ENTER_ROOM_STATUS = "Введите статус номера: ";
    public static final String CHOOSE_STATUS = "Выберите статус:";
    public static final String INVALID_STATUS = "Неверный выбор статуса";
    public static final String ENTER_BOOKING_ID = "Введите ID бронирования: ";
    public static final String CHOOSE_GUEST = "Выберите гостя:\n";
    public static final String ENTER_CSV_PATH = "Введите путь к файлу (например: guests.csv):";
    public static final String ENTER_CSV_PATH_ROOMS = "Введите путь к файлу (например: rooms.csv):";
    public static final String ENTER_CSV_PATH_AMENITIES = "Введите путь к файлу (например: amenities.csv):";
    public static final String ENTER_CSV_PATH_BOOKINGS = "Введите путь к файлу (например: bookings.csv):";
    public static final String ENTER_CSV_PATH_AMENITY_USAGE = "Введите путь к файлу (например: amenity_usage.csv):";
    public static final String ENTER_CSV_PATH_LOAD = "Введите путь к файлу:";


    /// CSV
    public static final String CSV_GUEST_HEADER = "id,fullName,phone,bookedRoomId";
    public static final String CSV_ROOM_HEADER = "id,number,price,status,capacity,stars";
    public static final String CSV_AMENITY_HEADER = "id,name,price,category";
    public static final String CSV_BOOKING_HEADER = "id,guestId,roomId,checkInDate,checkOutDate,actualCheckOutDate";
    public static final String CSV_AMENITY_USAGE_HEADER = "id,amenityId,bookingId,usageDate,quantity";
    public static final String ERROR_DIRECTORY_CREATE = "Не удалось создать директорию";
    public static final String ERROR_FILE_CREATE = "Не удалось создать файл";
    public static final String ERROR_PARSING_LINE = "Ошибка при парсинге строки: ";
    public static final String GUESTS_SAVED_SUCCESS = "Гости успешно сохранены!";
    public static final String GUESTS_SAVE_ERROR = "Ошибка при сохранении: ";
    public static final String ROOMS_SAVED_SUCCESS = "Номера успешно сохранены!";
    public static final String ROOMS_SAVE_ERROR = "Ошибка при сохранении номеров: ";
    public static final String AMENITIES_SAVED_SUCCESS = "Услуги успешно сохранены!";
    public static final String AMENITIES_SAVE_ERROR = "Ошибка при сохранении услуг: ";
    public static final String BOOKINGS_SAVED_SUCCESS = "Бронирования успешно сохранены!";
    public static final String BOOKINGS_SAVE_ERROR = "Ошибка при сохранении бронирований: ";
    public static final String AMENITY_USAGE_SAVED_SUCCESS = "Использования услуг успешно сохранены!";
    public static final String AMENITY_USAGE_SAVE_ERROR = "Ошибка при сохранении использования услуг: ";
    public static final String GUESTS_LOADED_SUCCESS = "Гости успешно загружены!";
    public static final String GUESTS_LOAD_ERROR = "Ошибка при загрузке гостей: ";
    public static final String BOOKINGS_LOADED_SUCCESS = "Бронирования успешно загружены!";
    public static final String BOOKINGS_LOAD_ERROR = "Ошибка при загрузке бронирований: ";
    public static final String ROOMS_LOADED_SUCCESS = "Номера успешно загружены!";
    public static final String ROOMS_LOAD_ERROR = "Ошибка при загрузке номеров: ";
    public static final String AMENITY_USAGE_LOADED_SUCCESS = "Использования услуг успешно загружены!";
    public static final String AMENITY_USAGE_LOAD_ERROR = "Ошибка при загрузке использования услуг: ";
    public static final String AMENITIES_LOADED_SUCCESS = "Услуги успешно загружены!";
    public static final String AMENITIES_LOAD_ERROR = "Ошибка при загрузке услуг: ";


    /// OTHER
    public static final String AMENITIES_BY_CATEGORY_HEADER = "Услуги по категориям";
    public static final String NO_AMENITIES_IN_CATEGORY = "Нет услуг в этой категории";
    public static final String UNKNOWN_PRICE_UPDATE_ERROR = "Неизвестная ошибка при обновлении цены";
    public static final String NOT_NEEDED_FOR_MENU_YET = "Not needed for menu yet";
    public static final String FAILED_TO_CREATE_MENU_ITEMS = "Failed to create menu items";


    /// DB-ONLY / NOT IMPLEMENTED
    public static final String DBONLY_NOTIMPLEMENTED_ADDROOM
            = "DB-only режим: реализуйте RoomDao.create(Room) и уникальность номера комнаты в БД.";
    public static final String DBONLY_NOTIMPLEMENTED_GUESTS
            = "DB-only режим: гости ещё не реализованы в БД (нужны таблица guests и GuestDaoJdbc).";
    public static final String DBONLY_NOTIMPLEMENTED_GUESTAMENITIES
            = "DB-only режим: отчет по услугам гостя требует SQL JOIN bookings + amenityusages.";
    public static final String DBONLY_NOTIMPLEMENTED_BOOKINGHISTORYBYGUEST
            = "DB-only режим: история бронирований гостя требует BookingDao.findByGuestId(...) или QueryServiceJPQL SQL.";


    public static final String BOOKING_INFO_FORMAT = "bookingId=%d, guestId=%d, roomId=%d, %s -> %s";
    public static final String AMENITY_ID_PREFIX = "amenityId=";
    public static final String CONSOLE_UI_CANNOT_BE_NULL = "ConsoleUI cannot be null";
    public static final String MENU_ITEM_FORMAT = "%d. %s";
}
