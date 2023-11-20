package com.example.hotel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;


import com.example.hotel.model.roomModel;
import com.example.hotel.model.roomModel.Room;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class roomController {

    public Button updateRoomButton;
    public Button addRoomButton;
    public Button deleteRoomButton;
    public Button refreshListButton;
    @FXML
    private TextField roomNumberField;
    @FXML
    private TextField roomTypeField;
    @FXML
    private TextField sizeField; // This field is not in your model, adjust as needed
    @FXML
    private TextField amenitiesField;
    @FXML
    private TextField rateField;

    // You would need some way to select a room in the UI to update or delete it
    // For simplicity, let's assume there's a TextField to input the Room ID for update and delete
    @FXML
    private TextField roomIdField;

    @FXML
    private void addRoom() {
        try {
            double rate = Double.parseDouble(rateField.getText());
            Room newRoom = new Room(0, roomNumberField.getText(), roomTypeField.getText(), amenitiesField.getText(), rate, "Available");
            boolean success = roomModel.addRoom(newRoom);
            if (success) {
                showAlert("Success", "Room added successfully!");
                clearFields();
            } else {
                showAlert("Error", "Failed to add room.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input for rate. Please enter a valid number.");
        }
    }

    @FXML
    private void updateRoom() {
        try {
            int roomId = Integer.parseInt(roomIdField.getText());
            double rate = Double.parseDouble(rateField.getText());
            Room updatedRoom = new Room(roomId, roomNumberField.getText(), roomTypeField.getText(), amenitiesField.getText(), rate, "Available");
            boolean success = roomModel.updateRoom(updatedRoom);
            if (success) {
                showAlert("Success", "Room updated successfully!");
                clearFields();
            } else {
                showAlert("Error", "Failed to update room.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input. Please enter a valid number for room ID and rate.");
        }
    }

    @FXML
    private void deleteRoom() {
        try {
            int roomId = Integer.parseInt(roomIdField.getText());
            boolean success = roomModel.deleteRoom(roomId);
            if (success) {
                showAlert("Success", "Room deleted successfully!");
                clearFields();
            } else {
                showAlert("Error", "Failed to delete room.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input for room ID. Please enter a valid number.");
        }
    }

    @FXML
    private void refreshRoomList() {
        // This method would typically update the UI with the latest list of rooms
        // The actual UI update logic will depend on how you're displaying the rooms
    }

    private void clearFields() {
        roomIdField.clear();
        roomNumberField.clear();
        roomTypeField.clear();
        sizeField.clear(); // Again, this field is not in your model, adjust as needed
        amenitiesField.clear();
        rateField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
