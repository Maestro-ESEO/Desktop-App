package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.List;

public class AllProjectsController extends NavigationViewController{

    @FXML
    private VBox projectContainer;

    private List<Project> projectList;

    @Override
    public void initialize(Object data) {
        if (data instanceof List<?>) {
            this.projectList = (List<Project>) data;
            for (Project project : this.projectList) {
                Button btn = new Button(project.getName());
                btn.setOnAction(event -> AppController.getInstance().navigateWithData(project));
                this.projectContainer.getChildren().add(btn);
            }
        } else {
            throw new Error("Could not process init data (Wrong type)");
        }
    }
}
