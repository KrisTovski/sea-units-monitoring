CREATE TABLE IF NOT EXISTS TOKEN (
                                    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                                    create_time DATETIME,
                                    access_token VARCHAR(1100)
);