--liquibase formatted sql

--changeset slava_borisov:1.0.1-insert-test-users
-- Пароли: admin/admin, user/user (BCrypt cost=10)
INSERT INTO users (username, password, role, guest_id)
 VALUES
     ('admin', '$2a$10$VVFcnBUYJoqrBSYqG2ZEJuiTuFTiuFse', 'ADMIN', NULL),
     ('user',  '$2a$10$BrUKvOT5xOPGhbPHLKbZdEUd', 'USER', 1);
--rollback DELETE FROM users WHERE username IN ('admin', 'user');
