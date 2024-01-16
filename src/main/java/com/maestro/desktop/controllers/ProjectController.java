package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.utils.ComponentFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProjectController extends NavigationViewController {
    private Project project;

    @FXML
    private Label title;
    @FXML
    private Label date;
    @FXML
    private Label description;
    @FXML
    private HBox actors;

    @Override
    public void initialize(Object data) {
        this.project = (Project) data;
        this.title.setText(this.project.getName());
        this.description.setText(this.project.getDescription());
        var df = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);
        this.date.setText(this.project.getEndDate() != null ? df.format(this.project.getEndDate()) : "Not specified");
        ComponentFactory.getInstance().displayActors(this.actors, 10, this.project.getActors());
    }
}
