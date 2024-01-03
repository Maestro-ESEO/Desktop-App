package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button loginButton;

    public void handleLoginButton(ActionEvent e) {
        // Create test user with test projects
        User loggedInUser = new User("John Doe", "jdoe@mail.com", "1234");
        loggedInUser.addProject(new Project("Project1"));
        loggedInUser.addProject(new Project("Project2"));
        loggedInUser.addProject(new Project("Project3"));
        loggedInUser.addProject(new Project("Project4"));
        loggedInUser.addProject(new Project("Project5"));
        loggedInUser.addProject(new Project("Project6"));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/app-view.fxml"));
            Parent load = loader.load();
            AppController appController = loader.getController();
            appController.initialize(loggedInUser);
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
