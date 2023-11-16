module com.example.hotel {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.hotel to javafx.fxml;
    exports com.example.hotel;
}