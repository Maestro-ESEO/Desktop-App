package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.utils.ComponentFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.List;
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
    @FXML
    private VBox inRevisionTasks;
    @FXML
    private VBox toDoTasks;
    @FXML
    private VBox inProgressTasks;
    @FXML
    private VBox completedTasks;

    @Override
    public void initialize(Object data) {
        this.project = (Project) data;
        this.title.setText(this.project.getName());
        this.description.setText(this.project.getDescription());
        var df = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);
        this.date.setText(this.project.getEndDate() != null ? df.format(this.project.getEndDate()) : "Not specified");
        ComponentFactory.getInstance().displayActors(this.actors, 10, this.project.getActors());
        this.displayTasks();
    }

    public void displayTasks() {
        List<Task> inRevisionList = this.project.getTasks().stream().filter(task -> task.getStatus() == Task.Status.IN_REVISION).toList();
        List<Task> toDoList = this.project.getTasks().stream().filter(task -> task.getStatus() == Task.Status.TO_DO).toList();
        List<Task> inProgressList = this.project.getTasks().stream().filter(task -> task.getStatus() == Task.Status.IN_PROGRESS).toList();
        List<Task> completedList = this.project.getTasks().stream().filter(task -> task.getStatus() == Task.Status.COMPLETED).toList();

        if (!inRevisionList.isEmpty()) {
            this.inRevisionTasks.getChildren().clear();
            for (Task task : inRevisionList) {
                HBox taskItem = ComponentFactory.getInstance().createTaskItem(task);
                this.inRevisionTasks.getChildren().add(taskItem);
            }
        }

        if (!toDoList.isEmpty()) {
            this.toDoTasks.getChildren().clear();
            for (Task task : toDoList) {
                HBox taskItem = ComponentFactory.getInstance().createTaskItem(task);
                this.toDoTasks.getChildren().add(taskItem);
            }
        }

        if (!inProgressList.isEmpty()) {
            this.inProgressTasks.getChildren().clear();
            for (Task task : inProgressList) {
                HBox taskItem = ComponentFactory.getInstance().createTaskItem(task);
                this.inProgressTasks.getChildren().add(taskItem);
            }
        }

        if (!completedList.isEmpty()) {
            this.completedTasks.getChildren().clear();
            for (Task task : completedList) {
                HBox taskItem = ComponentFactory.getInstance().createTaskItem(task);
                this.completedTasks.getChildren().add(taskItem);
            }
        }
    }


}
