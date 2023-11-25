package com.example.hotel.model;

import com.example.hotel.db.DBConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class bookingModel {

    public boolean isRoomAvailable(int roomId, LocalDate checkInDate, LocalDate checkOutDate, Integer bookingId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Bookings WHERE RoomID = ? AND NOT (? >= CheckOutDate OR ? <= CheckInDate)";
        if (bookingId != null) {
            sql += " AND BookingID != ?";
        }

        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            pstmt.setDate(2, java.sql.Date.valueOf(checkInDate));
            pstmt.setDate(3, java.sql.Date.valueOf(checkOutDate));
            if (bookingId != null) {
                pstmt.setInt(4, bookingId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0;
            }
        }
    }

    public void addBooking(int customerId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests, String specialRequests) throws SQLException {
        String sql = "INSERT INTO Bookings (CustomerID, RoomID, CheckInDate, CheckOutDate, NumberOfGuests, SpecialRequests) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            pstmt.setInt(2, roomId);
            pstmt.setDate(3, java.sql.Date.valueOf(checkInDate));
            pstmt.setDate(4, java.sql.Date.valueOf(checkOutDate));
            pstmt.setInt(5, numberOfGuests);
            pstmt.setString(6, specialRequests);
            pstmt.executeUpdate();
        }
    }

    public ResultSet getBookingsByCustomer(int customerId) throws SQLException {
        String sql = "SELECT * FROM Bookings WHERE CustomerID = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            return pstmt.executeQuery(); // Handle ResultSet in calling function
        }
    }

    public void updateBooking(int bookingId, int customerId, int roomId, LocalDate newCheckInDate, LocalDate newCheckOutDate, int numberOfGuests, String specialRequests) throws SQLException {
        String sql = "UPDATE Bookings SET CustomerID = ?, RoomID = ?, CheckInDate = ?, CheckOutDate = ?, NumberOfGuests = ?, SpecialRequests = ? WHERE BookingID = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            pstmt.setInt(2, roomId);
            pstmt.setDate(3, java.sql.Date.valueOf(newCheckInDate));
            pstmt.setDate(4, java.sql.Date.valueOf(newCheckOutDate));
            pstmt.setInt(5, numberOfGuests);
            pstmt.setString(6, specialRequests);
            pstmt.setInt(7, bookingId);
            pstmt.executeUpdate();
        }
    }

    public void cancelBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM Bookings WHERE BookingID = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookingId);
            pstmt.executeUpdate();
        }
    }
}
