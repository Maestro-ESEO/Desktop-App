module com.maestro.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.maestro.desktop to javafx.fxml;
    opens com.maestro.desktop.controllers to javafx.fxml;
    exports com.maestro.desktop;
    exports com.maestro.desktop.controllers;
}