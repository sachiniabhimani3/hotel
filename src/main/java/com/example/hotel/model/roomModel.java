package com.example.hotel.model;

import com.example.hotel.db.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class roomModel {

    private static roomModel instance;

    // Private constructor
    private roomModel() {}

    // Public method to get the instance
    public static roomModel getInstance() {
        if (instance == null) {
            instance = new roomModel();
        }
        return instance;
    }

    // Room as a nested static class inside RoomModel
    public static class Room {
        private int roomId;
        private String roomNumber;
        private String roomType;
        private String amenities;
        private double rate;
        private String status;

        // Constructor
        public Room(int roomId, String roomNumber, String roomType, String amenities, double rate, String status) {
            this.roomId = roomId;
            this.roomNumber = roomNumber;
            this.roomType = roomType;
            this.amenities = amenities;
            this.rate = rate;
            this.status = status;
        }

        public static List<Room> getAllRooms() {
            List<Room> rooms = new ArrayList<>();
            String sql = "SELECT * FROM Rooms";
            try (Connection conn = DBConnector.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    rooms.add(new Room(
                            rs.getInt("RoomID"),
                            rs.getString("RoomNumber"),
                            rs.getString("RoomType"),
                            rs.getString("Amenities"),
                            rs.getDouble("Rate"),
                            rs.getString("Status")
                    ));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return rooms;
        }

        // Getters and Setters
        public int getRoomId() { return roomId; }
        public void setRoomId(int roomId) { this.roomId = roomId; }

        public String getRoomNumber() { return roomNumber; }
        public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

        public String getRoomType() { return roomType; }
        public void setRoomType(String roomType) { this.roomType = roomType; }

        public String getAmenities() { return amenities; }
        public void setAmenities(String amenities) { this.amenities = amenities; }

        public double getRate() { return rate; }
        public void setRate(double rate) { this.rate = rate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // CRUD Operations

    // CREATE
    public static boolean addRoom(Room room) {
        String sql = "INSERT INTO Rooms (RoomNumber, RoomType, Amenities, Rate, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setString(3, room.getAmenities());
            pstmt.setDouble(4, room.getRate());
            pstmt.setString(5, room.getStatus());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    // READ
    public static Room getRoomById(int roomId) {
        String sql = "SELECT * FROM Rooms WHERE RoomID = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Room(
                        rs.getInt("RoomID"),
                        rs.getString("RoomNumber"),
                        rs.getString("RoomType"),
                        rs.getString("Amenities"),
                        rs.getDouble("Rate"),
                        rs.getString("Status")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms";
        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("RoomID"),
                        rs.getString("RoomNumber"),
                        rs.getString("RoomType"),
                        rs.getString("Amenities"),
                        rs.getDouble("Rate"),
                        rs.getString("Status")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rooms;
    }

    // UPDATE
    public static boolean updateRoom(Room room) {
        String sql = "UPDATE Rooms SET RoomNumber = ?, RoomType = ?, Amenities = ?, Rate = ?, Status = ? WHERE RoomID = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setString(3, room.getAmenities());
            pstmt.setDouble(4, room.getRate());
            pstmt.setString(5, room.getStatus());
            pstmt.setInt(6, room.getRoomId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    // DELETE
    public static boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM Rooms WHERE RoomID = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
