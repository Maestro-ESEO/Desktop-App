package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class LoginController {
    @FXML
    private Button loginButton;
    private User loggedInUser;

    public void handleLoginButton(ActionEvent e) {

        String email = "alejandrin82@example.org";
        String password = "$2y$12$wOrGe.J.E4KNsEcYai1Nfe8dKM7TcWAB/7Qr2Ec1N7e/yPGIehgXS";
        try {
            this.loggedInUser = DatabaseConnection.getInstance().login(email, password);
        } catch (SQLException error) {
            error.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/app-view.fxml"));
            Parent load = loader.load();
            AppController appController = loader.getController();
            appController.initialize(this.loggedInUser);
            Stage appStage = new Stage();
            appStage.setScene(new Scene(load));
            appStage.setTitle("Maestro");
            appStage.show();
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();

        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
