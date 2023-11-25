package com.example.hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Launcher extends Application {
    private static final String BUTTON_STYLE = "-fx-background-color: #3949AB; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;";
    private static final double BUTTON_WIDTH = 200; // Set the preferred width for buttons

    @Override
    public void start(Stage stage) throws IOException {
        // Load the main layout
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("main-view.fxml"));
        Parent root = fxmlLoader.load();

        // Create buttons to navigate to other views
        Button bookingButton = createButton("Booking Management");
        Button customerButton = createButton("Customer Management");
        Button roomButton = createButton("Room Management");
        Button paymentButton = createButton("Payment Management");

        // Set the action for each button
        bookingButton.setOnAction(e -> loadBookingView());
        customerButton.setOnAction(e -> loadView("customer-view.fxml", "Customer Management"));
        roomButton.setOnAction(e -> loadView("room-view.fxml", "Room Management"));
        paymentButton.setOnAction(e -> loadView("payment-view.fxml", "Payment Management"));

        // Create a VBox layout to hold the buttons
        VBox buttonLayout = new VBox(10); // 10 is the spacing between buttons
        buttonLayout.setPadding(new Insets(20)); // Add padding around the VBox
        buttonLayout.setAlignment(Pos.CENTER); // Center the buttons in the VBox
        buttonLayout.getChildren().addAll(bookingButton, customerButton, roomButton, paymentButton);

        // Create a new scene with the button layout
        Scene scene = new Scene(buttonLayout, 400, 300); // Set the preferred width and height
        stage.setTitle("Hotel Management System"); // Set the title of the window
        stage.setScene(scene); // Set the scene on the stage
        stage.show(); // Show the stage
    }

    public static void main(String[] args) {
        launch(args); // Launch the application
    }

    // Method to create a styled button with a uniform size
    private Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle(BUTTON_STYLE);
        button.setPrefWidth(BUTTON_WIDTH);
        button.setMaxWidth(Double.MAX_VALUE); // Allow the button to grow in width
        return button;
    }

    // Method to load the booking view without password protection
    public static void loadBookingView() {
        loadView("booking-view.fxml", "Booking Management");
    }

    // Method to display a password dialog and check the password
    private static boolean showPasswordDialog(String title) {
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Authentication");
        passwordDialog.setHeaderText("Enter Password to Access " + title);
        passwordDialog.setContentText("Password:");
        PasswordField pwdField = new PasswordField();
        pwdField.setPromptText("Password");
        passwordDialog.getEditor().setVisible(false);
        passwordDialog.getDialogPane().setContent(pwdField);

        Optional<String> result = passwordDialog.showAndWait();
        if (result.isPresent()) {
            return checkPassword(pwdField.getText());
        }

        return false; // If the dialog is cancelled, deny access
    }

    // Simple password check (replace with a real password check in a real app)
    private static boolean checkPassword(String enteredPassword) {
        final String correctPassword = "test123"; // This should be securely stored and hashed
        return enteredPassword.equals(correctPassword);
    }

    // Method to load other views with password protection for certain views
    private static void loadView(String fxmlFile, String title) {
        // Check if the view needs password protection
        if (title.equals("Customer Management") || title.equals("Payment Management") || title.equals("Room Management")) {
            if (!showPasswordDialog(title)) {
                // If the password is incorrect, do not load the view and return
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Authentication Error");
                alert.setHeaderText("Access Denied");
                alert.setContentText("The password you entered is incorrect.");
                alert.showAndWait();
                return;
            }
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setTitle(title);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
