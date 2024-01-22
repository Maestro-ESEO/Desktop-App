package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;

/**
 * NewTaskDialogController - Controller's methods related to the create task popup window.
 */
public class NewTaskDialogController {
    private Stage stage;
    private Project parentProject;

    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker deadline;
    @FXML
    private MenuButton status;
    @FXML
    private MenuButton priority;

    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;

    /**
     * initialize - Sets the stage and displays the different item from the page.
     * @param stage - Stage of the window.
     * @param parentProject - Parent project of the task.
     */
    public void initialize(Stage stage, Project parentProject) {
        this.parentProject = parentProject;
        this.stage = stage;
        this.deadline.setValue(new Date(this.parentProject.getEndDate().getTime()).toLocalDate());
        this.createButton.setDisable(true);
        this.name.setOnAction(event -> { this.createButton.setDisable(this.name.getText().isBlank()); });
        this.name.textProperty().addListener((observable, oldValue, newValue) -> { this.createButton.setDisable(this.name.getText().isBlank()); });
        this.createButton.setOnAction(event -> this.createTask());
        this.cancelButton.setOnAction(event -> this.stage.close());
        this.status.getItems().clear();
        for (Task.Status status : Task.Status.values()) {
            var menuItem = new MenuItem(status.getName());
            menuItem.setOnAction(event -> {
                this.status.setText(menuItem.getText());
                this.status.setUserData(status);
            });
            this.status.getItems().add(menuItem);
        }
        this.priority.getItems().clear();
        for (Task.Priority priority : Task.Priority.values()) {
            var menuItem = new MenuItem(priority.getName());
            menuItem.setOnAction(event -> {
                this.priority.setText(menuItem.getText());
                this.priority.setUserData(priority);
            });
            this.priority.getItems().add(menuItem);
        }
    }

    /**
     * createTask - Creates a new task and adds it in the database.
     */
    public void createTask() {
        if (name.getText().isBlank()) {
            return;
        }

        Task task = new Task(
                -1,
                this.name.getText(),
                this.description.getText(),
                Date.valueOf(this.deadline.getValue()),
                (Task.Status) this.status.getUserData(),
                (Task.Priority) this.priority.getUserData(),
                this.parentProject,
                new java.util.Date(),
                new java.util.Date()
        );
        try {
            DatabaseConnection.getInstance().insertTask(task);
            DatabaseConnection.getInstance().updateAllTasks(this.parentProject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AppController.getInstance().updateView(AppController.getInstance().getAccount());
        this.stage.close();
    }
}
