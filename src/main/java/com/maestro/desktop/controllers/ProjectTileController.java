package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.utils.ComponentFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProjectTileController {
    @FXML
    private Label projectTitle;
    @FXML
    private Label description;
    @FXML
    private Label date;
    @FXML
    private HBox actors;
    @FXML
    private Label tasksLeft;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label percentage;

    private Project project;
    public void initialize(Project project) {
        this.project = project;
        this.projectTitle.setText(this.project.getName());
        this.description.setText(this.project.getDescription());
        var df = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);
        this.date.setText(this.project.getEndDate() != null ? df.format(this.project.getEndDate()) : "Not specified");
        this.tasksLeft.setText(this.getTasksLeft());
        this.percentage.setText(this.getPercentage() + "%");
        this.progressBar.setProgress(this.getPercentage() / 100.0);
        ComponentFactory.getInstance().displayActors(this.actors, 4, this.project.getActors());
    }

    private String getTasksLeft() {
        if (this.project.getTasks().isEmpty()) {
            return "No tasks yet";
        }
        long tasksLeft = this.project.getTasks().stream().filter(task -> task.getStatus() != Task.Status.COMPLETED).count();
        if (tasksLeft == 0) {
            return "All tasks done";
        } else {
            return tasksLeft + " tasks left";
        }
    }

    private int getPercentage() {
        int totalTasks = this.project.getTasks().size();
        if (totalTasks == 0) {
            return 0;
        }
        long tasksLeft = this.project.getTasks().stream().filter(task -> task.getStatus() == Task.Status.COMPLETED).count();
        return (int) (100 * tasksLeft / totalTasks);
    }
}
