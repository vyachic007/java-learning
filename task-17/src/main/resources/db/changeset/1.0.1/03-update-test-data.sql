--liquibase formatted sql

--changeset vyacheslav_borisov:update-guests-phones
UPDATE guests SET phone = '79112234567' WHERE full_name = 'Иван Иванов';
UPDATE guests SET phone = '79127654321' WHERE full_name = 'Петр Петров';
UPDATE guests SET phone = '79131122334' WHERE full_name = 'Мария Сидорова';
UPDATE guests SET phone = '79133456789' WHERE full_name = 'Анна Ковальчук';
UPDATE guests SET phone = '79144112233' WHERE full_name = 'Вячеслав Борисов';
UPDATE guests SET phone = '79144223384' WHERE full_name = 'Елена Смирнова';
UPDATE guests SET phone = '79144556767' WHERE full_name = 'Алексей Новиков';
UPDATE guests SET phone = '79144667788' WHERE full_name = 'Ольга Морозова';
--rollback UPDATE guests SET phone = '+752912345670' WHERE full_name = 'Иван Иванов';
--rollback UPDATE guests SET phone = '+752976543219' WHERE full_name = 'Петр Петров';
--rollback UPDATE guests SET phone = '+753311223349' WHERE full_name = 'Мария Сидорова';
--rollback UPDATE guests SET phone = '+753334567898' WHERE full_name = 'Анна Ковальчук';
--rollback UPDATE guests SET phone = '+754411122338' WHERE full_name = 'Вячеслав Борисов';
--rollback UPDATE guests SET phone = '+754422233844' WHERE full_name = 'Елена Смирнова';
--rollback UPDATE guests SET phone = '+754455567677' WHERE full_name = 'Алексей Новиков';
--rollback UPDATE guests SET phone = '+754466677888' WHERE full_name = 'Ольга Морозова';