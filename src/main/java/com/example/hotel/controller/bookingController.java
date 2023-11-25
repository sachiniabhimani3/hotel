package com.example.hotel.controller;

import com.example.hotel.model.bookingModel;
import java.sql.SQLException;
import java.time.LocalDate;

public class bookingController {

    private final bookingModel bookingModel;

    public bookingController() {
        this.bookingModel = new bookingModel();
    }

    public void makeBooking(int customerId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests, String specialRequests) {
        try {
            if (bookingModel.isRoomAvailable(roomId, checkInDate, checkOutDate, null)) {
                bookingModel.addBooking(customerId, roomId, checkInDate, checkOutDate, numberOfGuests, specialRequests);
            } else {
                throw new IllegalStateException("Room is not available for the selected dates.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Database error occurred", ex);
        }
    }

    public void viewBookings(int customerId) {
        try {
            bookingModel.getBookingsByCustomer(customerId);
            // Process ResultSet or convert to a list of booking objects to return or use in the view
        } catch (SQLException ex) {
            throw new RuntimeException("Database error occurred", ex);
        }
    }

    public void updateBooking(int bookingId, int customerId, int roomId, LocalDate newCheckInDate, LocalDate newCheckOutDate, int numberOfGuests, String specialRequests) {
        try {
            if (bookingModel.isRoomAvailable(roomId, newCheckInDate, newCheckOutDate, bookingId)) {
                bookingModel.updateBooking(bookingId, customerId, roomId, newCheckInDate, newCheckOutDate, numberOfGuests, specialRequests);
            } else {
                throw new IllegalStateException("Room is not available for the updated dates.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Database error occurred", ex);
        }
    }

    public void cancelBooking(int bookingId) {
        try {
            bookingModel.cancelBooking(bookingId);
        } catch (SQLException ex) {
            throw new RuntimeException("Database error occurred", ex);
        }
    }

}
