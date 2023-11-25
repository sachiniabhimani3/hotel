package com.example.hotel.controller;
import com.example.hotel.booking;
import com.example.hotel.customer;
import com.example.hotel.payment;
import com.example.hotel.room;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class launcherController {

    @FXML
    private Button roomViewButton;

    @FXML
    private Button paymentViewButton;

    @FXML
    private Button customerViewButton;

    @FXML
    private Button bookingViewButton;

    @FXML
    private VBox contentArea;

    // Initialize method to set up any necessary state or configurations
    @FXML
    public void initialize() {
        // You might want to load the default view here, for example, the room view.
        //loadView("main-view.fxml");
    }

    @FXML
    private void handleRoomViewAction() {
        loadView("room-view.fxml");
    }

    @FXML
    private void handlePaymentViewAction() {
        loadView("payment-view.fxml");
    }

    @FXML
    private void handleCustomerViewAction() {
        loadView("customer-view.fxml");
    }

    @FXML
    private void handleBookingViewAction() {
        loadView("booking-view.fxml");
    }


    @FXML
    private void runBooking() {
        booking booking = new booking();

    }

    @FXML
    private void runCustomer() {
        customer customer = new customer();

    }

    @FXML
    private void runPayment() {
        payment payment = new payment();

    }

    @FXML
    private void runRoom() {
        room room = new room();


    }
    // Utility method to load views
    private void loadView(String fxmlFile) {
        try {
            URL url = getClass().getResource("/com/example/hotel/" + fxmlFile);
            System.out.println("Loading FXML from: " + url);
            if (url == null) {
                throw new IOException("Cannot load resource: " + fxmlFile);
            }
            Node view = FXMLLoader.load(url);
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
           // e.printStackTrace();
        }
    }

}
