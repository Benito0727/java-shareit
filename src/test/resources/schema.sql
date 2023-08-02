CREATE TABLE IF NOT EXISTS users (
user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
user_name VARCHAR(64) NOT NULL,
user_email VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS items (
item_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
item_name VARCHAR(64) NOT NULL,
item_description VARCHAR(512) NOT NULL,
is_available BOOLEAN,
owner BIGINT NOT NULL
);