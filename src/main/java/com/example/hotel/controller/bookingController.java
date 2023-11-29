package com.example.hotel.controller;

import com.example.hotel.model.bookingModel;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;



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
    @FXML
    private ListView<bookingModel.Book> bookingListView;

    private final bookingModel model = bookingModel.getInstance();

    private void clearFields() {

        customerIdField.clear();
        roomIdField.clear();
        checkInDatePicker.setValue(null);
        checkOutDatePicker.setValue(null);
        numberOfGuestsField.clear();
        specialRequestsArea.clear();
        bookingIdField.clear();

    }

    @FXML
    private void refreshBookingList() {
        System.out.println("aaaa");
        ObservableList<bookingModel.Book> book = FXCollections.observableArrayList(bookingModel.getAllBooking());
        bookingListView.setCellFactory(new Callback<ListView<bookingModel.Book>, ListCell<bookingModel.Book>>() {

            @Override

            public ListCell<bookingModel.Book> call(ListView<bookingModel.Book> bookListView) {
                System.out.println("yyyy");
                return new ListCell<bookingModel.Book>() {
                    @Override
                    protected void updateItem(bookingModel.Book book, boolean empty) {
                        super.
                                updateItem(book, empty);
                        if (empty || book == null) {
                            setText("xxx");
                        } else {
                            System.out.println("bbb");
                            setText("ID: " + book.getBookingId() + "   |   Number: ");
                        }
                    }
                };
                //return null;
            }

//            @Override
//            public ListCell<roomModel.Room> call(ListView<roomModel.Room> listView) {
//                return new ListCell<roomModel.Room>() {
//                    @Override
//                    protected void updateItem(roomModel.Room room, boolean empty) {
//                        super.updateItem(room, empty);
//                        if (empty || room == null) {
//                            setText(null);
//                        } else {
//
//                            setText("ID: " + room.getRoomId() + "   |   Number: " + room.getRoomNumber() +
//                                    "   |   Type: " + room.getRoomType() + "    |   Amenities: " + room.getAmenities() +
//                                    "   |   Rate: " + room.getRate() + "    |   Status: " + room.getStatus());
//                        }
//                    }
//                };
//            }
        });
        //bookingListView.setItems(rooms);
    }

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
                boolean isUpdated = bookingModel.updateBooking(bookingId, customerId, roomId, checkInDate, checkOutDate, numberOfGuests, specialRequests);
                clearFields();
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
                boolean isCancelled = bookingModel.cancelBooking(bookingId);
                clearFields();
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



    @FXML
    protected void handleAddBookingAction() {
        if (validateBookingForm()) {
            int customerId = Integer.parseInt(customerIdField.getText());
            int roomId = Integer.parseInt(roomIdField.getText());
            LocalDate checkInDate = checkInDatePicker.getValue();
            LocalDate checkOutDate = checkOutDatePicker.getValue();
            int numberOfGuests = Integer.parseInt(numberOfGuestsField.getText());
            String specialRequests = specialRequestsArea.getText();


            boolean isAdded = bookingModel.addBooking(customerId, roomId, checkInDate, checkOutDate, numberOfGuests, specialRequests);
            clearFields();
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
