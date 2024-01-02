package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProjectController extends NavigationViewController {
    private Project project;

    @FXML
    private Label title;

    @Override
    public void initialize(Object data) {
        this.project = (Project) data;
        this.title.setText(this.project.getName());
    }
}
