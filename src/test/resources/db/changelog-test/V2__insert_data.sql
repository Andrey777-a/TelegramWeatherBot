--liquibase formatted sql

--changeset andrew:1
INSERT INTO cities (id, name)
VALUES (703448, 'Київ'),
       (706483,'Харків'),
       (698740,'Одеса'),
       (709930,'Дніпро'),
       (687700,'Запоріжжя'),
       (702550,'Львів'),
       (703845,'Кривий Ріг'),
       (700569,'Миколаїв'),
       (689558,'Вінниця'),
       (706448,'Херсон'),
       (696643,'Полтава'),
       (710735,'Чернігів'),
       (710791,'Черкаси'),
       (692194,'Суми');

--changeset andrew:2
INSERT INTO users (chat_id, firstname, lastname, username, city,
                   language, units, registered_at, last_weather_request, notification_time)
VALUES (678228966, 'James', 'Smith', 'fine_boy',
        'New York', 'en', 'metric', '2024-09-17 20:01:12.363025',
        null, '11:00:00'),
       (6782289625, 'Maria', 'Garcia', 'elton',
        'London', 'en', 'metric', '2024-09-16 15:14:15.363025',
        null, '12:00:00'),
       (678228745, 'Edward', 'Johnson', 'shark',
        'Toronto', 'en', 'metric', '2024-09-11 12:53:16.363025',
        null, '10:00:00');