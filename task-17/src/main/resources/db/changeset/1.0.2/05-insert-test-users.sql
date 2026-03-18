--liquibase formatted sql

--changeset vyacheslav_borisov:1.0.2-insert-test-users-2
INSERT INTO users (username, password, role, guest_id)
VALUES
    ('admin', '$2a$10$GK7STBrjYbCHBPs8.QpXye3wB8s6YJuwZLhpam6y3gy6tLMGFeFeO', 'ADMIN', NULL),
    ('user',  '$2a$10$MD/Hgj8W7QeNvSnxJy4lsO/nfcQ7ru0wfY5F1m4FbCvKbhwIUNnYW', 'USER', 1);
--rollback DELETE FROM users WHERE username IN ('admin', 'user');