package com.example.hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Assuming 'main-view.fxml' is the FXML file that contains the main layout with buttons to load other views
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = fxmlLoader.load(); // Load the main layout
        Scene scene = new Scene(root); // Create a new Scene with the loaded layout
        stage.setTitle("Hotel Management System"); // Set the title of the window
        stage.setResizable(false); // Make the stage non-resizable
        stage.setScene(scene); // Set the scene on the stage
        stage.show(); // Show the stage
    }

    public static void main(String[] args) {
        launch(args); // Launch the application
    }
}
