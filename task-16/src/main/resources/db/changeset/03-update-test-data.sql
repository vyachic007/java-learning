--liquibase formatted sql

--changeset vyacheslav_borisov:03-update-guests-phones-20260220
UPDATE guests SET phone = '79112234567' WHERE full_name = 'Иван Иванов';
UPDATE guests SET phone = '79127654321' WHERE full_name = 'Петр Петров';
UPDATE guests SET phone = '79131122334' WHERE full_name = 'Мария Сидорова';
UPDATE guests SET phone = '79133456789' WHERE full_name = 'Анна Ковальчук';
UPDATE guests SET phone = '79144112233' WHERE full_name = 'Вячеслав Борисов';
UPDATE guests SET phone = '79144223384' WHERE full_name = 'Елена Смирнова';
UPDATE guests SET phone = '79144556767' WHERE full_name = 'Алексей Новиков';
UPDATE guests SET phone = '79144667788' WHERE full_name = 'Ольга Морозова';