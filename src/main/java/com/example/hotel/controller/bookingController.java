package com.example.hotel.controller;

import com.example.hotel.model.bookingModel;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.time.LocalDate;

public class bookingController {

    @FXML private TextField customerIdField;
    @FXML private TextField roomIdField;
    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private TextField numberOfGuestsField;
    @FXML private TextArea specialRequestsArea;
    @FXML private TextField bookingIdField;

    private final bookingModel model = bookingModel.getInstance();

    @FXML
    protected void handleUpdateBookingAction() {
        if (validateBookingForm() && validateBookingId()) {
            // Call the model's update booking method
            int bookingId = Integer.parseInt(bookingIdField.getText());
            int customerId = Integer.parseInt(customerIdField.getText());
            int roomId = Integer.parseInt(roomIdField.getText());
            LocalDate checkInDate = checkInDatePicker.getValue();
            LocalDate checkOutDate = checkOutDatePicker.getValue();
            int numberOfGuests = Integer.parseInt(numberOfGuestsField.getText());
            String specialRequests = specialRequestsArea.getText();

            try {
                boolean isUpdated = model.updateBooking(bookingId, customerId, roomId, checkInDate, checkOutDate, numberOfGuests, specialRequests);
                if (isUpdated) {
                    showAlertDialog(AlertType.INFORMATION, "Success", "Booking updated successfully.");
                } else {
                    showAlertDialog(AlertType.ERROR, "Error", "Failed to update booking.");
                }
            } catch (SQLException e) {
                showAlertDialog(AlertType.ERROR, "Database Error", "An error occurred while updating the booking: " + e.getMessage());
            }
        }
    }

    @FXML
    protected void handleCancelBookingAction() {
        if (validateBookingId()) {
            // Call the model's cancel booking method
            int bookingId = Integer.parseInt(bookingIdField.getText());

            try {
                boolean isCancelled = model.cancelBooking(bookingId);
                if (isCancelled) {
                    showAlertDialog(AlertType.INFORMATION, "Success", "Booking canceled successfully.");
                } else {
                    showAlertDialog(AlertType.ERROR, "Error", "Failed to cancel booking.");
                }
            } catch (SQLException e) {
                showAlertDialog(AlertType.ERROR, "Database Error", "An error occurred while canceling the booking: " + e.getMessage());
            }
        }
    }

    private boolean validateBookingId() {
        if (bookingIdField.getText().isEmpty()) {
            showAlertDialog(AlertType.ERROR, "Validation Error", "Booking ID is required.");
            return false;
        }

        return true;
    }

    // ... other methods

    @FXML
    protected void handleAddBookingAction() {
        if (validateBookingForm()) {
            int customerId = Integer.parseInt(customerIdField.getText());
            int roomId = Integer.parseInt(roomIdField.getText());
            LocalDate checkInDate = checkInDatePicker.getValue();
            LocalDate checkOutDate = checkOutDatePicker.getValue();
            int numberOfGuests = Integer.parseInt(numberOfGuestsField.getText());
            String specialRequests = specialRequestsArea.getText();

            boolean isAdded = model.addBooking(customerId, roomId, checkInDate, checkOutDate, numberOfGuests, specialRequests);
            if (isAdded) {
                showAlertDialog(AlertType.INFORMATION, "Success", "Booking added successfully.");
                // Clear the form or update the UI as needed
            } else {
                showAlertDialog(AlertType.ERROR, "Error", "Failed to add booking.");
            }
        }
    }



    private boolean validateBookingForm() {
        // Implement your validation logic here
        // For example:
        if (customerIdField.getText().isEmpty() || roomIdField.getText().isEmpty() ||
                checkInDatePicker.getValue() == null || checkOutDatePicker.getValue() == null ||
                numberOfGuestsField.getText().isEmpty()) {
            showAlertDialog(AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return false;
        }
        // Add more validation as necessary
        return true;
    }

    private void showAlertDialog(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
