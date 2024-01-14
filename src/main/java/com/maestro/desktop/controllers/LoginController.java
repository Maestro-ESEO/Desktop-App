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
import java.util.ArrayList;
import java.util.Date;

public class LoginController {
    @FXML
    private Button loginButton;

    public void handleLoginButton(ActionEvent e) {
        // Create test user with test projects
        User loggedInUser = new User(0, "John", "Doe", "mail@mail.com", "https://via.placeholder.com/480x480.png/0077ff?text=people+iusto", new Date());
        Project p1 = new Project("Project Cow", loggedInUser);
        p1.setUsers(new ArrayList<>(){
            {
                add(loggedInUser);
                add(loggedInUser);
                add(loggedInUser);
                add(loggedInUser);
                add(loggedInUser);
            }
        });
        loggedInUser.addProject(p1);
        loggedInUser.addProject(new Project("Project Rabbit", loggedInUser));
        loggedInUser.addProject(new Project("Project Duck", loggedInUser));
        loggedInUser.addProject(new Project("Project Pig", loggedInUser));
        loggedInUser.addProject(new Project("Project Squirrel", loggedInUser));
        loggedInUser.addProject(new Project("Project Donkey", loggedInUser));

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
