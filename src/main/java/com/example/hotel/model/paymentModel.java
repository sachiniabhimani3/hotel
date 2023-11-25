package com.example.hotel.model;

import com.example.hotel.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class paymentModel {

    private static paymentModel instance;

    private paymentModel() {
    }

    public static synchronized paymentModel getInstance() {
        if (instance == null) {
            instance = new paymentModel();
        }
        return instance;
    }

    public static class Payment {
        private int paymentId;
        private int bookingId;
        private double amount;
        private LocalDate paymentDate;
        private String paymentMethod;
        private String status;

        public Payment(int paymentId, int bookingId, double amount, LocalDate paymentDate, String paymentMethod, String status) {
            this.paymentId = paymentId;
            this.bookingId = bookingId;
            this.amount = amount;
            this.paymentDate = paymentDate;
            this.paymentMethod = paymentMethod;
            this.status = status;
        }

        // Getters and Setters for each property
        public int getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(int paymentId) {
            this.paymentId = paymentId;
        }

        public int getBookingId() {
            return bookingId;
        }

        public void setBookingId(int bookingId) {
            this.bookingId = bookingId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
    public boolean addPayment(int bookingId, double amount, LocalDate paymentDate, String paymentMethod, String status) throws SQLException {
        String sql = "INSERT INTO payments (booking_id, amount, payment_date, payment_method, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            stmt.setDouble(2, amount);
            stmt.setDate(3, java.sql.Date.valueOf(paymentDate));
            stmt.setString(4, paymentMethod);
            stmt.setString(5, status);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public Payment getPayment(int paymentId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                            rs.getInt("payment_id"),
                            rs.getInt("booking_id"),
                            rs.getDouble("amount"),
                            rs.getDate("payment_date").toLocalDate(),
                            rs.getString("payment_method"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    public boolean updatePayment(int paymentId, int bookingId, double amount, LocalDate paymentDate, String paymentMethod, String status) throws SQLException {
        String sql = "UPDATE payments SET booking_id = ?, amount = ?, payment_date = ?, payment_method = ?, status = ? WHERE payment_id = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            stmt.setDouble(2, amount);
            stmt.setDate(3, java.sql.Date.valueOf(paymentDate));
            stmt.setString(4, paymentMethod);
            stmt.setString(5, status);
            stmt.setInt(6, paymentId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // ****
}
