--liquibase formatted sql

--changeset vyacheslav_borisov:insert_rooms
INSERT INTO rooms (number, price_per_night, status, capacity, stars)
    VALUES
        ('101', 3500.00, 'AVAILABLE', 2, 3),
        ('102', 3500.00, 'AVAILABLE', 2, 3),
        ('103', 3500.00, 'AVAILABLE', 2, 3),
        ('104', 3500.00, 'AVAILABLE', 2, 3),
        ('201', 4500.00, 'AVAILABLE', 3, 4),
        ('202', 4500.00, 'AVAILABLE', 3, 4),
        ('203', 4500.00, 'AVAILABLE', 3, 4),
        ('204', 4500.00, 'AVAILABLE', 3, 4),
        ('301', 5500.00, 'AVAILABLE', 4, 5),
        ('302', 5500.00, 'AVAILABLE', 4, 5),
        ('401', 6500.00, 'AVAILABLE', 5, 5),
        ('402', 6500.00, 'AVAILABLE', 5, 5);


--changeset vyacheslav_borisov:insert_guests
INSERT INTO guests (full_name, phone)
    VALUES
        ('Иван Иванов', '+752912345670'),
        ('Петр Петров', '+752976543219'),
        ('Мария Сидорова', '+753311223349'),
        ('Анна Ковальчук', '+753334567898'),
        ('Вячеслав Борисов', '+754411122338'),
        ('Елена Смирнова', '+754422233844'),
        ('Алексей Новиков', '+754455567677'),
        ('Ольга Морозова', '+754466677888');


--changeset vyacheslav_borisov:insert_amenities
INSERT INTO amenities (name, price, category)
    VALUES
         ('Завтрак', 25.00, 'Питание'),
         ('Обед', 35.00, 'Питание'),
         ('Ужин', 40.00, 'Питание'),
         ('Шведский стол', 55.00, 'Питание'),
         ('СПА', 100.00, 'Отдых'),
         ('Сауна', 80.00, 'Отдых'),
         ('Массаж', 120.00, 'Отдых'),
         ('Тренажерный зал', 50.00, 'Спорт'),
         ('Бассейн', 70.00, 'Спорт'),
         ('Трансфер', 50.00, 'Транспорт'),
         ('Парковка', 30.00, 'Транспорт'),
         ('Wi-Fi', 15.00, 'Другое'),
         ('Мини-бар', 45.00, 'Питание'),
         ('Химчистка', 60.00, 'Услуги'),
         ('Консьерж', 90.00, 'Услуги');


--changeset vyacheslav_borisov:insert_bookings
INSERT INTO bookings (guest_id, room_id, check_in_date, check_out_date, actual_check_out_date)
    VALUES
        (1, 1, '2026-02-10', '2026-02-15', NULL),
        (2, 5, '2026-02-11', '2026-02-18', NULL),
        (3, 9, '2026-02-09', '2026-02-14', '2026-02-14'),
        (4, 2, '2026-02-12', '2026-02-16', NULL),
        (5, 6, '2026-02-08', '2026-02-13', '2026-02-13'),
        (6, 10, '2026-02-13', '2026-02-20', NULL);


--changeset vyacheslav_borisov:insert_amenity_usages
INSERT INTO amenity_usages (amenity_id, booking_id, usage_date, quantity)
    VALUES
        (1, 1, '2026-02-11', 2),
        (2, 1, '2026-02-12', 2),
        (4, 2, '2026-02-12', 1),
        (5, 2, '2026-02-13', 2),
        (1, 3, '2026-02-10', 1),
        (8, 4, '2026-02-13', 1),
        (9, 4, '2026-02-13', 1),
        (10, 5, '2026-02-09', 1),
        (13, 6, '2026-02-14', 1);