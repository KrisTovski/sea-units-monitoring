--liquibase formatted sql
--changeset kris:1
CREATE TABLE IF NOT EXISTS TOKEN (
                                    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                                    create_time DATETIME,
                                    access_token VARCHAR(1100)
);
--changeset kris:2
CREATE TABLE IF NOT EXISTS SEAUNIT (
                                       id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                                       created DATETIME,
                                       x DOUBLE,
                                       y DOUBLE,
                                       mmsi INTEGER,
                                       name VARCHAR(50),
                                       ship_type INTEGER,
                                       destination_y DOUBLE,
                                       destination_x DOUBLE
);
