module com.maestro.maestrodesktopapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.maestro.maestrodesktopapp to javafx.fxml;
    exports com.maestro.maestrodesktopapp;
}