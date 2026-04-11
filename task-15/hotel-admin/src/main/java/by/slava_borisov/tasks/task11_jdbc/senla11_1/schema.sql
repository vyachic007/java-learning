create table if not exists rooms (
id serial primary key,
number varchar(20) not null unique,
price_per_night numeric(10,2) not null check (price_per_night >= 0),
status varchar(32) not null,
capacity integer not null check (capacity > 0),
stars integer not null check (stars between 1 and 5)
);

create table if not exists guests (
id serial primary key,
full_name varchar(200) not null,
phone varchar(50) not null
);

create table if not exists bookings (
id serial primary key,
guest_id integer not null,
room_id integer not null,
check_in_date date not null,
check_out_date date not null,
actual_check_out_date date,
foreign key (guest_id) references guests(id),
foreign key (room_id) references rooms(id),
check (check_in_date <= check_out_date)
);

create table if not exists amenities (
id serial primary key,
name varchar(200) not null,
price numeric(10,2) not null check (price >= 0),
category varchar(100) not null
);

create table if not exists amenity_usages (
id serial primary key,
booking_id integer not null,
amenity_id integer not null,
usage_date date not null,
quantity integer not null check (quantity > 0),
foreign key (booking_id) references bookings(id) on delete cascade,
foreign key (amenity_id) references amenities(id)
);