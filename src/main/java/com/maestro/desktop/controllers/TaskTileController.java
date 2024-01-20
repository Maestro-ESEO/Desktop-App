package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.utils.ComponentFactory;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TaskTileController {

    @FXML
    private Label projectTitle;
    @FXML
    private Label description;
    @FXML
    private HBox actors;

    private Task task;

    public void initialize(Task task) {
        this.task = task;
        this.projectTitle.setText(this.task.getParentProject().getName());
        this.description.setText(this.task.getDescription());
        ComponentFactory.getInstance().displayActors(actors, 3, this.task.getActors());
    }
}
