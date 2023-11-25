package com.example.hotel.model;

import com.example.hotel.db.DBConnector;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class bookingModel {


    public static class Booking {
        private int bookingId;
        private int customerId;
        private int roomId;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private int numberOfGuests;
        private String specialRequests;

        // Constructor
        public Booking(int bookingId, int customerId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests, String specialRequests) {
            this.bookingId = bookingId;
            this.customerId = customerId;
            this.roomId = roomId;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.numberOfGuests = numberOfGuests;
            this.specialRequests = specialRequests;
        }

        // Getters and setters
        public int getBookingId() {
            return bookingId;
        }

        public void setBookingId(int bookingId) {
            this.bookingId = bookingId;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public LocalDate getCheckInDate() {
            return checkInDate;
        }

        public void setCheckInDate(LocalDate checkInDate) {
            this.checkInDate = checkInDate;
        }

        public LocalDate getCheckOutDate() {
            return checkOutDate;
        }

        public void setCheckOutDate(LocalDate checkOutDate) {
            this.checkOutDate = checkOutDate;
        }

        public int getNumberOfGuests() {
            return numberOfGuests;
        }

        public void setNumberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
        }

        public String getSpecialRequests() {
            return specialRequests;
        }

        public void setSpecialRequests(String specialRequests) {
            this.specialRequests = specialRequests;
        }
    }


    private static bookingModel instance;

    private bookingModel() {
        // Private constructor to prevent direct instantiation.
    }

    public static synchronized bookingModel getInstance() {
        if (instance == null) {
            instance = new bookingModel();
        }
        return instance;
    }

    public boolean addBooking(int customerId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests, String specialRequests) {
        String sql = "INSERT INTO Bookings (CustomerID, RoomID, CheckInDate, CheckOutDate, NumberOfGuests, SpecialRequests) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, roomId);
            stmt.setDate(3, java.sql.Date.valueOf(checkInDate));
            stmt.setDate(4, java.sql.Date.valueOf(checkOutDate));
            stmt.setInt(5, numberOfGuests);
            stmt.setString(6, specialRequests);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // You should handle this properly
            return false;
        }
    }


    public boolean isRoomAvailable(int roomId, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        String checkAvailabilitySQL = "SELECT COUNT(*) FROM Bookings WHERE RoomID = ? AND NOT (CheckOutDate <= ? OR CheckInDate >= ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(checkAvailabilitySQL)) {
            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(checkInDate));
            stmt.setDate(3, Date.valueOf(checkOutDate));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false;
    }

    public void makeBooking(int customerId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests, String specialRequests) throws SQLException {
        String insertBookingSQL = "INSERT INTO Bookings (CustomerID, RoomID, CheckInDate, CheckOutDate, NumberOfGuests, SpecialRequests) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(insertBookingSQL)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, roomId);
            stmt.setDate(3, Date.valueOf(checkInDate));
            stmt.setDate(4, Date.valueOf(checkOutDate));
            stmt.setInt(5, numberOfGuests);
            stmt.setString(6, specialRequests);

            stmt.executeUpdate();
        }
    }

    public List<Booking> viewBookingsForCustomer(int customerId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String selectBookingsSQL = "SELECT * FROM Bookings WHERE CustomerID = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(selectBookingsSQL)) {
            stmt.setInt(1, customerId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("BookingID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("RoomID"),
                        rs.getDate("CheckInDate").toLocalDate(),
                        rs.getDate("CheckOutDate").toLocalDate(),
                        rs.getInt("NumberOfGuests"),
                        rs.getString("SpecialRequests")
                ));
            }
        }
        return bookings;
    }

    public boolean updateBooking(int bookingId, int roomId, int customerId, LocalDate newCheckInDate, LocalDate newCheckOutDate, int numberOfGuests, String specialRequests) throws SQLException {
        boolean bookingUpdated = false;

        // Statements to enable/disable foreign key checks
        String disableFKChecks = "SET FOREIGN_KEY_CHECKS=0";
        String enableFKChecks = "SET FOREIGN_KEY_CHECKS=1";

        Connection conn = null;
        Statement stmt = null;

        try {
            // Establish database connection
            conn = DBConnector.connect();

            // Disable auto-commit to handle transactions manually
            conn.setAutoCommit(false);

            // Disable foreign key checks
            stmt = conn.createStatement();
            stmt.execute(disableFKChecks);

            // Prepare the UPDATE statement
            String updateBookingSQL = "UPDATE Bookings SET RoomID = ?, CustomerID = ?, CheckInDate = ?, CheckOutDate = ?, NumberOfGuests = ?, SpecialRequests = ? WHERE BookingID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateBookingSQL)) {
                pstmt.setInt(1, roomId);
                pstmt.setInt(2, customerId);
                pstmt.setDate(3, java.sql.Date.valueOf(newCheckInDate));
                pstmt.setDate(4, java.sql.Date.valueOf(newCheckOutDate));
                pstmt.setInt(5, numberOfGuests);
                pstmt.setString(6, specialRequests);
                pstmt.setInt(7, bookingId);

                int affectedRows = pstmt.executeUpdate();
                bookingUpdated = (affectedRows > 0);
            }

            // Re-enable foreign key checks
            stmt.execute(enableFKChecks);

            // Commit the transaction
            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    // Rollback any changes if there were errors
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            throw e; // Or handle it as per your error handling policy
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    // Restore auto-commit mode
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return bookingUpdated;
    }


    public boolean cancelBooking(int bookingId) throws SQLException {
        // This flag will track if the booking was successfully cancelled
        boolean bookingCancelled = false;

        // Statements to enable/disable foreign key checks
        String disableFKChecks = "SET FOREIGN_KEY_CHECKS=0";
        String enableFKChecks = "SET FOREIGN_KEY_CHECKS=1";

        Connection conn = null;
        Statement stmt = null;

        try {
            // Establish database connection
            conn = DBConnector.connect();

            // Disable auto-commit to handle transactions manually
            conn.setAutoCommit(false);

            // Disable foreign key checks
            stmt = conn.createStatement();
            stmt.execute(disableFKChecks);

            // Delete the booking
            String deleteBookingSQL = "DELETE FROM Bookings WHERE BookingID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteBookingSQL)) {
                pstmt.setInt(1, bookingId);
                int affectedRows = pstmt.executeUpdate();
                bookingCancelled = (affectedRows > 0);
            }

            // Re-enable foreign key checks
            stmt.execute(enableFKChecks);

            // Commit the transaction
            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    // Rollback any changes if there were errors
                    conn.rollback();
                } catch (SQLException ex) {
                    // Log the rollback error if any
                    ex.printStackTrace();
                }
            }
            // Log the initial error
            e.printStackTrace();
            throw e; // Or handle it as per your error handling policy
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {

                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                // Log the error during resource cleanup
                ex.printStackTrace();
            }
        }

        return bookingCancelled;
    }

}
