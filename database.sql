CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20),
    password VARCHAR(255)
);

CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_type VARCHAR(100),
    price DECIMAL(10,2),
    availability BOOLEAN
);

CREATE TABLE reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    room_id INT,
    num_of_guests INT,
    status VARCHAR(50),
    check_in DATE,
    check_out DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    reservation_id INT,
    amount DECIMAL(10,2),
    payment_date DATE,
    payment_status VARCHAR(50),
    payment_method VARCHAR(50),
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
);

CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    payment_id INT,
    transaction_date DATE,
    transaction_status VARCHAR(50),
    FOREIGN KEY (payment_id) REFERENCES payments(payment_id)
);
