package by.slava_borisov.hoteladmin.db;

public final class SqlQueries {
    private SqlQueries() {
    }

    public static final String AMENITY_INSERT = """
            INSERT INTO amenities (name, price, category)
            VALUES (?, ?, ?)
            """;

    public static final String AMENITY_FIND_BY_ID = """
            SELECT id, name, price, category
            FROM amenities
            WHERE id = ?
            """;

    public static final String AMENITY_FIND_ALL = """
            SELECT id, name, price, category
            FROM amenities
            ORDER BY id
            """;

    public static final String AMENITY_UPDATE = """
            UPDATE amenities
            SET name = ?, price = ?, category = ?
            WHERE id = ?
            """;

    public static final String AMENITY_DELETE_BY_ID = """
            DELETE FROM amenities
            WHERE id = ?
            """;

    public static final String AMENITY_FIND_ALL_SORTED_BY_PRICE = """
            SELECT id, name, price, category
            FROM amenities
            ORDER BY price
            """;

    public static final String AMENITY_FIND_ALL_SORTED_BY_CATEGORY = """
            SELECT id, name, price, category
            FROM amenities
            ORDER BY category, name
            """;

    public static final String AMENITY_UPDATE_PRICE = """
            UPDATE amenities 
            SET price = ? 
            WHERE id = ?
            """;

    public static final String AMENITY_USAGE_INSERT = """
            INSERT INTO amenity_usages (amenity_id, booking_id, usage_date, quantity)
            VALUES (?, ?, ?, ?)
            """;

    public static final String AMENITY_USAGE_FIND_BY_ID = """
            SELECT id, amenity_id, booking_id, usage_date, quantity
            FROM amenity_usages
            WHERE id = ?
            """;

    public static final String AMENITY_USAGE_FIND_ALL = """
            SELECT id, amenity_id, booking_id, usage_date, quantity
            FROM amenity_usages
            ORDER BY id
            """;

    public static final String AMENITY_USAGE_UPDATE = """
            UPDATE amenity_usages
            SET amenity_id = ?, booking_id = ?, usage_date = ?, quantity = ?
            WHERE id = ?
            """;

    public static final String AMENITY_USAGE_DELETE_BY_ID = """
            DELETE FROM amenity_usages
            WHERE id = ?
            """;

    public static final String AMENITY_USAGE_FIND_BY_BOOKING_ID = """
            SELECT id, amenity_id, booking_id, usage_date, quantity
            FROM amenity_usages
            WHERE booking_id = ?
            ORDER BY usage_date, id
            """;

    public static final String BOOKING_INSERT = """
            INSERT INTO bookings (guest_id, room_id, check_in_date, check_out_date, actual_check_out_date)
            VALUES (?, ?, ?, ?, ?)
            """;

    public static final String BOOKING_FIND_BY_ID = """
            SELECT id, guest_id, room_id, check_in_date, check_out_date, actual_check_out_date
            FROM bookings
            WHERE id = ?
            """;

    public static final String BOOKING_FIND_ALL = """
            SELECT id, guest_id, room_id, check_in_date, check_out_date, actual_check_out_date
            FROM bookings
            ORDER BY id
            """;

    public static final String BOOKING_UPDATE = """
            UPDATE bookings
            SET guest_id = ?,
                room_id = ?,
                check_in_date = ?,
                check_out_date = ?,
                actual_check_out_date = ?
            WHERE id = ?
            """;

    public static final String BOOKING_DELETE_BY_ID = """
            DELETE FROM bookings
            WHERE id = ?
            """;

    public static final String BOOKING_FIND_ACTIVE_BY_ROOM = """
            SELECT id, guest_id, room_id, check_in_date, check_out_date, actual_check_out_date
            FROM bookings
            WHERE room_id = ?
              AND check_in_date <= ?
              AND check_out_date > ?
              AND actual_check_out_date IS NULL
            ORDER BY id
            LIMIT 1
            """;

    public static final String BOOKING_FIND_ACTIVE_BY_GUEST = """
            SELECT id, guest_id, room_id, check_in_date, check_out_date, actual_check_out_date
            FROM bookings
            WHERE guest_id = ?
              AND check_in_date <= ?
              AND check_out_date > ?
              AND actual_check_out_date IS NULL
            ORDER BY id
            LIMIT 1
            """;

    public static final String BOOKING_EXISTS_OVERLAPPING = """
            SELECT 1
            FROM bookings
            WHERE room_id = ?
              AND actual_check_out_date IS NULL
              AND check_in_date < ?
              AND check_out_date > ?
            LIMIT 1
            """;

    public static final String BOOKING_FIND_BY_ROOM_ID = """
            SELECT id, guest_id, room_id, check_in_date, check_out_date, actual_check_out_date
            FROM bookings
            WHERE room_id = ?
            ORDER BY check_in_date
            """;

    public static final String BOOKING_UPDATE_ACTUAL_CHECK_OUT_DATE = """
            UPDATE bookings
            SET actual_check_out_date = ?
            WHERE id = ?
            """;

    public static final String BOOKING_INSERT_TRANSACTION = """
            INSERT INTO bookings (guest_id, room_id, check_in_date, check_out_date, actual_check_out_date)
            VALUES (?, ?, ?, ?, NULL)
            """;

    public static final String ROOM_INSERT = """
            INSERT INTO rooms (number, price_per_night, status, capacity, stars)
            VALUES (?, ?, ?, ?, ?)
            """;

    public static final String ROOM_FIND_BY_ID = """
            SELECT id, number, price_per_night, status, capacity, stars
            FROM rooms
            WHERE id = ?
            """;

    public static final String ROOM_FIND_ALL = """
            SELECT id, number, price_per_night, status, capacity, stars
            FROM rooms
            ORDER BY id
            """;

    public static final String ROOM_UPDATE = """
            UPDATE rooms
            SET number = ?,
                price_per_night = ?,
                status = ?,
                capacity = ?,
                stars = ?
            WHERE id = ?
            """;

    public static final String ROOM_DELETE_BY_ID = """
            DELETE FROM rooms
            WHERE id = ?
            """;

    public static final String ROOM_FIND_BY_NUMBER = """
            SELECT id, number, price_per_night, status, capacity, stars
            FROM rooms
            WHERE number = ?
            """;

    public static final String ROOM_FIND_AVAILABLE_ON_DATE = """
            SELECT r.id, r.number, r.price_per_night, r.status, r.capacity, r.stars
            FROM rooms r
            WHERE r.status = 'AVAILABLE'
              AND NOT EXISTS (
                SELECT 1
                FROM bookings b
                WHERE b.room_id = r.id
                  AND b.check_in_date <= ?
                  AND b.check_out_date > ?
                  AND b.actual_check_out_date IS NULL
              )
            ORDER BY r.id
            """;

    public static final String ROOM_UPDATE_STATUS = """
            UPDATE rooms
            SET status = ?
            WHERE id = ?
            """;

    public static final String ROOM_UPDATE_PRICE_PER_NIGHT = """
            UPDATE rooms
            SET price_per_night = ?
            WHERE id = ?
            """;

    public static final String GUEST_INSERT = """
            INSERT INTO guests (full_name, phone) 
            VALUES (?, ?)
            """;

    public static final String GUEST_FIND_BY_ID = """
            SELECT id, full_name, phone 
            FROM guests 
            WHERE id = ?
            """;

    public static final String GUEST_FIND_ALL = """
            SELECT id, full_name, phone 
            FROM guests 
            ORDER BY id
            """;

    public static final String GUEST_UPDATE = """
            UPDATE guests 
            SET full_name = ?, phone = ? 
            WHERE id = ?
            """;

    public static final String GUEST_DELETE_BY_ID = """
            DELETE FROM guests 
            WHERE id = ?
            """;

    public static final String QUERY_COUNT_AVAILABLE_ROOMS = """
            SELECT COUNT(*)
            FROM rooms
            WHERE status = 'AVAILABLE'
            """;

    public static final String QUERY_COUNT_CURRENT_GUESTS = """
            SELECT COUNT(DISTINCT guest_id)
            FROM bookings
            WHERE check_in_date <= CURRENT_DATE
              AND check_out_date > CURRENT_DATE
              AND actual_check_out_date IS NULL
            """;

    public static final String QUERY_LAST_BOOKINGS_BY_ROOM = """
            SELECT id, guest_id, room_id, check_in_date, check_out_date, actual_check_out_date
            FROM bookings
            WHERE room_id = ?
            ORDER BY check_in_date DESC
            LIMIT ?
            """;

    public static final String QUERY_GUEST_AMENITIES_COST = """
            SELECT COALESCE(SUM(a.price * u.quantity), 0)
            FROM bookings b
            JOIN amenity_usages u ON u.booking_id = b.id
            JOIN amenities a ON a.id = u.amenity_id
            WHERE b.guest_id = ?
            """;

    public static final String QUERY_GUEST_ROOM_COST_ACTIVE = """
            SELECT COALESCE(SUM(r.price_per_night * GREATEST(0, (CURRENT_DATE - b.check_in_date))), 0)
            FROM bookings b
            JOIN rooms r ON r.id = b.room_id
            WHERE b.guest_id = ?
              AND b.check_in_date <= CURRENT_DATE
              AND b.check_out_date > CURRENT_DATE
              AND b.actual_check_out_date IS NULL
            """;

    public static final String QUERY_ROOMS_SORTED_BY_PRICE = """
            SELECT id, number, price_per_night, status, capacity, stars
            FROM rooms
            ORDER BY price_per_night
            """;

    public static final String QUERY_ROOMS_SORTED_BY_CAPACITY = """
            SELECT id, number, price_per_night, status, capacity, stars
            FROM rooms
            ORDER BY capacity
            """;

    public static final String QUERY_ROOMS_SORTED_BY_STARS = """
            SELECT id, number, price_per_night, status, capacity, stars
            FROM rooms
            ORDER BY stars
            """;

    public static final String QUERY_GUESTS_SORTED_BY_NAME = """
            SELECT id, full_name, phone
            FROM guests
            ORDER BY full_name
            """;

    public static final String QUERY_GUESTS_SORTED_BY_CHECK_OUT_DATE = """
            SELECT g.id, g.full_name, g.phone
            FROM guests g
            JOIN bookings b ON b.guest_id = g.id
            WHERE b.check_in_date <= CURRENT_DATE
              AND b.check_out_date > CURRENT_DATE
              AND b.actual_check_out_date IS NULL
            ORDER BY b.check_out_date, g.full_name
            """;

    public static final String QUERY_GUESTS_WITH_ROOMS = """
            SELECT
                g.id   AS g_id,
                g.full_name,
                g.phone,
                r.id   AS r_id,
                r.number,
                r.price_per_night,
                r.status,
                r.capacity,
                r.stars
            FROM guests g
            JOIN bookings b ON b.guest_id = g.id
            JOIN rooms r    ON r.id = b.room_id
            WHERE b.check_in_date <= CURRENT_DATE
              AND b.check_out_date > CURRENT_DATE
              AND b.actual_check_out_date IS NULL
            ORDER BY g.full_name
            """;

    public static final String QUERY_AMENITIES_SORTED_BY_PRICE = """
            SELECT id, name, price, category
            FROM amenities
            ORDER BY price
            """;

    public static final String QUERY_AMENITIES_SORTED_BY_CATEGORY = """
            SELECT id, name, price, category
            FROM amenities
            ORDER BY category, name
            """;

    public static final String GUEST_FIND_BY_PHONE =
            "SELECT id, full_name, phone FROM guests WHERE phone = ?";
}
