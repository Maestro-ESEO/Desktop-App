package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;

public class DashboardController {
    private User loggedInUser;

    @FXML
    private Label userName;

    @FXML
    private ListView projectList;

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        this.userName.setText(loggedInUser.getName());
        this.loadProjects();
    }

    public void loadProjects() {
        projectList.setCellFactory(list -> new TextFieldListCell<>(new StringConverter<Project>() {
            @Override
            public String toString(Project project) {
                return project != null ? project.getName() : "";
            }

            @Override
            public Project fromString(String string) {
                return new Project(string);
            }
        }));

        projectList.setOnEditCommit(event -> {
            ListView.EditEvent<Project> editEvent = (ListView.EditEvent<Project>) event;
            Project editedProject = editEvent.getNewValue();

            editedProject.setName(editedProject.getName());
            System.out.println(editedProject.getName());
            projectList.getItems().set(editEvent.getIndex(), editedProject);
        });

        projectList.getItems().addAll(loggedInUser.getProjects());
    }

    public void logOut(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/hello-view.fxml"));
            Stage currentStage = (Stage) projectList.getScene().getWindow();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loader.load()));
            newStage.show();
            currentStage.close();

        } catch(IOException error) {
            error.printStackTrace();
        }
    }

    public void addNewProject(ActionEvent e) {
        Project p = new Project("New Project");
        loggedInUser.addProject(p);
        projectList.getItems().clear();
        projectList.getItems().addAll(loggedInUser.getProjects());
    }
}
