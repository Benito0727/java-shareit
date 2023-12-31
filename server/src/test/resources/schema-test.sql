
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
owner BIGINT NOT NULL REFERENCES USERS(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
last_booking_id BIGINT,
next_booking_id BIGINT
);

CREATE TABLE IF NOT EXISTS bookings (
booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
start_date TIMESTAMP NOT NULL,
end_date TIMESTAMP NOT NULL,
status_id INTEGER NOT NULL,
booker BIGINT NOT NULL REFERENCES USERS(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
item_id BIGINT NOT NULL REFERENCES ITEMS(item_id) ON UPDATE CASCADE ON DELETE CASCADE
);