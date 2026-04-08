--liquibase formatted sql

--changeset vyacheslav_borisov:1.0.2-create-users-table
CREATE TABLE users (
 id BIGSERIAL PRIMARY KEY,
 username VARCHAR(50) NOT NULL UNIQUE,
 password VARCHAR(100) NOT NULL,
 role VARCHAR(20) NOT NULL,
 guest_id BIGINT,
 FOREIGN KEY (guest_id) REFERENCES guests(id) ON DELETE SET NULL
);
--rollback DROP TABLE users;
