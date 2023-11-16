CREATE TABLE Rooms (
    RoomID INT PRIMARY KEY AUTO_INCREMENT,
    RoomNumber VARCHAR(10) NOT NULL,
    RoomType VARCHAR(50),
    Amenities TEXT,
    Rate DECIMAL(10, 2),
    Status VARCHAR(50)
);

CREATE TABLE Customers (
    CustomerID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50),
    LastName VARCHAR(50),
    PhoneNumber VARCHAR(20),
    Email VARCHAR(100),
    Address TEXT,
    PaymentMethod VARCHAR(50)
);

CREATE TABLE Bookings (
    BookingID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT,
    RoomID INT,
    CheckInDate DATE,
    CheckOutDate DATE,
    NumberOfGuests INT,
    SpecialRequests TEXT,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),
    FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID)
);

CREATE TABLE Payments (
    PaymentID INT PRIMARY KEY AUTO_INCREMENT,
    BookingID INT,
    Amount DECIMAL(10, 2),
    Date DATE,
    Method VARCHAR(50),
    Status VARCHAR(50),
    FOREIGN KEY (BookingID) REFERENCES Bookings(BookingID)
);
