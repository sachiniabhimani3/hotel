

-- Table for customer details
CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(15),
    address TEXT
);

-- Table for room details
CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(50) NOT NULL UNIQUE,
    room_type VARCHAR(255) NOT NULL,
    size VARCHAR(50),
    amenities TEXT,
    rate DECIMAL(10, 2) NOT NULL
);

-- Table for bookings
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    number_of_guests INT,
    status ENUM('booked', 'checked_in', 'checked_out', 'cancelled') NOT NULL,
    special_requests TEXT
);

-- Table for payments
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method ENUM('cash', 'card', 'online') NOT NULL,
    status ENUM('pending', 'completed', 'refunded') NOT NULL
);
