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
import java.util.Date;

public class LoginController {
    @FXML
    private Button loginButton;

    public void handleLoginButton(ActionEvent e) {
        // Create test user with test projects
        User loggedInUser = new User(0, "John", "Doe", "mail@mail.com", "https://via.placeholder.com/480x480.png/0077ff?text=people+iusto", new Date());
        loggedInUser.addProject(new Project("Project Cow"));
//        loggedInUser.addProject(new Project("Project Rabbit"));
//        loggedInUser.addProject(new Project("Project Duck"));
//        loggedInUser.addProject(new Project("Project Pig"));
//        loggedInUser.addProject(new Project("Project Squirrel"));
//        loggedInUser.addProject(new Project("Project Donkey"));

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
