package com.example.hotel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import com.example.hotel.model.roomModel;
import com.example.hotel.model.roomModel.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

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
    private TextField amenitiesField;
    @FXML
    private TextField rateField;
    @FXML
    private TextField roomIdField;
    @FXML
    private ListView<Room> roomListView; // The ListView to display rooms

    @FXML
    private void addRoom() {

        try {
            double rate = Double.parseDouble(rateField.getText());
            Room newRoom = new Room(0, roomNumberField.getText(), roomTypeField.getText(), amenitiesField.getText(), rate, "Available");
            boolean success = roomModel.addRoom(newRoom);
            if (success) {
                showAlert("Success", "Room added successfully!");
                refreshRoomList(); // Refresh the list to show the new room
            } else {
                showAlert("Error", "Failed to add room.");
            }
            clearFields();
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
                refreshRoomList(); // Refresh the list to show the updated room
            } else {
                showAlert("Error", "Failed to update room.");
            }
            clearFields();
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
                refreshRoomList(); // Refresh the list to show the updated room list
            } else {
                showAlert("Error", "Failed to delete room.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input for room ID. Please enter a valid number.");
        }
        clearFields();
    }

    @FXML
    private void refreshRoomList() {
        ObservableList<Room> rooms = FXCollections.observableArrayList(roomModel.getAllRooms());
        roomListView.setCellFactory(new Callback<ListView<Room>, ListCell<Room>>() {
            @Override
            public ListCell<Room> call(ListView<Room> listView) {
                return new ListCell<Room>() {
                    @Override
                    protected void updateItem(Room room, boolean empty) {
                        super.updateItem(room, empty);
                        if (empty || room == null) {
                            setText(null);
                        } else {

                            setText("ID: " + room.getRoomId() + "   |   Number: " + room.getRoomNumber() +
                                    "   |   Type: " + room.getRoomType() + "    |   Amenities: " + room.getAmenities() +
                                    "   |   Rate: " + room.getRate() + "    |   Status: " + room.getStatus());
                        }
                    }
                };
            }
        });
        roomListView.setItems(rooms);
    }




    private void clearFields() {
        roomIdField.clear();
        roomNumberField.clear();
        roomTypeField.clear();
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
