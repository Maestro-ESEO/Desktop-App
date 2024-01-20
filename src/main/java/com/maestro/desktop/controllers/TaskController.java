package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Task;
import com.maestro.desktop.utils.ComponentFactory;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TaskController extends NavigationViewController{
    private Task task;

    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private Label date;
    @FXML
    private HBox actors;
    @FXML
    private MenuButton status;
    @FXML
    private Button accessProject;

    public void initialize(Object data){
        this.task = (Task) data;
        this.title.setText(this.task.getName());
        this.description.setText(this.task.getDescription());
        var df = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);
        this.date.setText(this.task.getDeadline() != null ? df.format(this.task.getDeadline()) : "Not specified");
        ComponentFactory.getInstance().displayActors(this.actors, 10, this.task.getActors());
        this.status.getItems().clear();
        this.status.setId(this.task.getStatus().name());
        this.status.setText(this.task.getStatus().getName());
        for (Task.Status status : Task.Status.values()) {
            var menuItem = new MenuItem(status.getName());
            menuItem.setOnAction(event -> {
                this.status.setText(menuItem.getText());
                try {
                    DatabaseConnection.getInstance().updateTaskStatus(this.task, status);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                this.task.setStatus(status);
                AppController.getInstance().navigateWithData(this.task);
            });
            this.status.getItems().add(menuItem);
        }
        this.accessProject.setText(this.task.getParentProject().getName());
        this.accessProject.setOnAction(event -> AppController.getInstance().navigateWithData(this.task.getParentProject()));
    }
}
