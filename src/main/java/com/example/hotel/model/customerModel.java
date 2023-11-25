package com.example.hotel.model;

import com.example.hotel.db.DBConnector;

import java.sql.*;



public class customerModel {

    private static customerModel instance;

    private customerModel() {
    }

    public static synchronized customerModel getInstance() {
        if (instance == null) {
            instance = new customerModel();
        }
        return instance;
    }


    public static class Customer {
        private int customerID; // Assuming there's an ID field in the database
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String email;
        private String address;
        private String paymentMethod;

        // Constructor
        public Customer(int customerID, String firstName, String lastName, String phoneNumber, String email, String address, String paymentMethod) {
            this.customerID = customerID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.address = address;
            this.paymentMethod = paymentMethod;
        }

        // Getters and setters
        public int getCustomerID() { return customerID; }
        public void setCustomerID(int customerID) { this.customerID = customerID; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }





    // Add a customer to the database
    public boolean addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO Customers (FirstName, LastName, PhoneNumber, Email, Address, PaymentMethod) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getPhoneNumber());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setString(5, customer.getAddress());
            preparedStatement.setString(6, customer.getPaymentMethod());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    // Update a customer's details in the database
    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE Customers SET FirstName = ?, LastName = ?, PhoneNumber = ?, Email = ?, Address = ?, PaymentMethod = ? WHERE CustomerID = ?";
        try (Connection connection = DBConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getPhoneNumber());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setString(5, customer.getAddress());
            preparedStatement.setString(6, customer.getPaymentMethod());
            preparedStatement.setInt(7, customer.getCustomerID());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    // Verify a customer's reservation
    public boolean verifyReservation(int customerId) throws SQLException {
        String sql = "SELECT * FROM Reservations WHERE CustomerID = ? AND Status = 'Active'";
        try (Connection connection = DBConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If a record is found, the reservation is verified
        }
    }

    // Assign a room to a customer
    public boolean assignRoom(int customerId, int roomId) throws SQLException {
        String sql = "UPDATE Rooms SET Status = 'Occupied', CustomerID = ? WHERE RoomID = ? AND Status = 'Available'";
        try (Connection connection = DBConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, roomId);
            return preparedStatement.executeUpdate() > 0; // Check if the room was successfully assigned
        }
    }

    // Process a payment for a customer
    public boolean processPayment(int customerId, double amount) throws SQLException {
        String sql = "INSERT INTO Payments (CustomerID, Amount, Date, Method, Status) VALUES (?, ?, ?, 'Cash', 'Completed')";
        try (Connection connection = DBConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setDate(3, new Date(System.currentTimeMillis()));
            return preparedStatement.executeUpdate() > 0;
        }
    }

    // Return a room key, effectively checking the customer out
    public boolean returnRoomKey(int customerId, int roomId) throws SQLException {
        String sql = "UPDATE Rooms SET Status = 'Available', CustomerID = NULL WHERE RoomID = ? AND CustomerID = ?";
        try (Connection connection = DBConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roomId);
            preparedStatement.setInt(2, customerId);
            return preparedStatement.executeUpdate() > 0; // Check if the key return was successful
        }
    }


    // Delete a customer from the database
    public boolean deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM Customers WHERE CustomerID = ?";
        try (Connection connection = DBConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

}
