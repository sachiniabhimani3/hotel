module com.example.hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.dlsc.formsfx;

    opens com.example.hotel to javafx.fxml;
    exports com.example.hotel;
    exports com.example.hotel.controller;
    opens com.example.hotel.controller to javafx.fxml;

}