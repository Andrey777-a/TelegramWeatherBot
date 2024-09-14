--liquibase formatted sql

--changeset andrew:1
CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT,
    firstname VARCHAR(64),
    lastname VARCHAR(64),
    username VARCHAR(64),
    city VARCHAR(64),
    language VARCHAR(4),
    units VARCHAR(10),
    registered_at TIMESTAMP,
    last_weather_request TIMESTAMP,
    notification_time TIMESTAMP
    );

--changeset andrew:2
CREATE TABLE cities
(
    id   BIGINT PRIMARY KEY,
    name VARCHAR(20)
);
