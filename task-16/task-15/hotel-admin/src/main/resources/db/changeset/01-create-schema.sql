--liquibase formatted sql

--changeset vyacheslav_borisov:create_rooms_table
CREATE TABLE rooms (
 id BIGSERIAL PRIMARY KEY,
 number VARCHAR(50) NOT NULL UNIQUE,
 price_per_night DOUBLE PRECISION NOT NULL,
 status VARCHAR(20) NOT NULL,
 capacity INT NOT NULL,
 stars INT NOT NULL
);

--changeset vyacheslav_borisov:create_guests_table
CREATE TABLE guests (
 id BIGSERIAL PRIMARY KEY,
 full_name VARCHAR(255) NOT NULL,
 phone VARCHAR(20) NOT NULL
);

--changeset vyacheslav_borisov:create_amenities_table
CREATE TABLE amenities (
 id BIGSERIAL PRIMARY KEY,
 name VARCHAR(255) NOT NULL,
 price DOUBLE PRECISION NOT NULL,
 category VARCHAR(100) NOT NULL
);

--changeset vyacheslav_borisov:create_bookings_table
CREATE TABLE bookings (
 id BIGSERIAL PRIMARY KEY,
 guest_id BIGINT NOT NULL,
 room_id BIGINT NOT NULL,
 check_in_date DATE NOT NULL,
 check_out_date DATE NOT NULL,
 actual_check_out_date DATE,
    FOREIGN KEY (guest_id) REFERENCES guests(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

--changeset vyacheslav_borisov:create_amenity_usages_table
CREATE TABLE amenity_usages (
 id BIGSERIAL PRIMARY KEY,
 amenity_id BIGINT NOT NULL,
 booking_id BIGINT NOT NULL,
 usage_date DATE NOT NULL,
 quantity INT NOT NULL,
    FOREIGN KEY (amenity_id) REFERENCES amenities(id),
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);
