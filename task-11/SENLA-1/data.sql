insert into rooms (number, price_per_night, status, capacity, stars) values
('101', 3500.00, 'available', 2, 3),
('102', 5200.00, 'available', 3, 4),
('201', 8000.00, 'cleaning', 2, 5),
('202', 2700.00, 'undermaintenance', 1, 2);

insert into guests (full_name, phone) values
('Иван Петров', '+7-900-111-22-33'),
('Борисов Вячеслав', '+7-900-222-33-44'),
('Алексей Иванов', '+7-900-333-44-55');

insert into amenities (name, price, category) values
('Завтрак', 800.00, 'food'),
('Мини-бар', 500.00, 'food'),
('Прачечная', 600.00, 'service'),
('Трансфер', 1500.00, 'service');


insert into bookings (guest_id, room_id, check_in_date, check_out_date, actual_check_out_date)
select g.id, r.id, date '2025-12-20', date '2025-12-25', null
from guests g, rooms r
where g.full_name = 'Иван Петров' and r.number = '101';

insert into bookings (guest_id, room_id, check_in_date, check_out_date, actual_check_out_date)
select g.id, r.id, date '2025-12-10', date '2025-12-12', date '2025-12-12'
from guests g, rooms r
where g.full_name = 'Борисов Вячеслав' and r.number = '102';


insert into amenity_usages (booking_id, amenity_id, usage_date, quantity)
select b.id, a.id, date '2025-12-21', 2
from bookings b
join guests g on g.id = b.guest_id
join rooms r on r.id = b.room_id
join amenities a on a.name = 'Завтрак'
where g.full_name = 'Иван Петров' and r.number = '101';

insert into amenity_usages (booking_id, amenity_id, usage_date, quantity)
select b.id, a.id, date '2025-12-22', 1
from bookings b
join guests g on g.id = b.guest_id
join rooms r on r.id = b.room_id
join amenities a on a.name = 'Трансфер'
where g.full_name = 'Иван Петров' and r.number = '101';