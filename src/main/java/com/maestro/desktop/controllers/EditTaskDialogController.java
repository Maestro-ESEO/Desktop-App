package com.maestro.desktop.controllers;

import com.maestro.desktop.App;
import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditTaskDialogController {

    private Stage stage;
    private Task task;
    private List<User> selectedUsers;
    private List<User> newActors;

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
    private MenuButton collaboratorMenu;

    @FXML
    private Button updateButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;

    public void initialize(Stage stage, Task task) {
        this.stage = stage;
        this.task = task;
        this.selectedUsers = new ArrayList<>(task.getActors());
        this.newActors = new ArrayList<>();
        this.collaboratorMenu.setText(task.getActors().size() + " user(s) selected");
        this.updateButton.setDisable(true);
        this.updateButton.setOnAction(event -> this.updateProject());
        this.cancelButton.setOnAction(event -> this.stage.close());
        this.deleteButton.setOnAction(event -> this.deleteTask());
        this.name.textProperty().addListener((observable, oldValue, newValue) -> { this.updateButton.setDisable(this.name.getText().isBlank()); });
        this.status.getItems().clear();
        this.status.setUserData(this.task.getStatus());
        this.status.setText(((Task.Status) this.status.getUserData()).getName());
        this.status.setId(((Task.Status) this.status.getUserData()).name());
        for (Task.Status status : Task.Status.values()) {
            var menuItem = new MenuItem(status.getName());
            menuItem.setOnAction(event -> {
                this.status.setText(menuItem.getText());
                this.status.setUserData(status);
                this.status.setId(((Task.Status) this.status.getUserData()).name());
            });
            this.status.getItems().add(menuItem);
        }
        this.priority.getItems().clear();
        this.priority.setUserData(Task.Priority.LOW);
        this.priority.setText(((Task.Priority) this.priority.getUserData()).getName());
        this.priority.setId(((Task.Priority) this.priority.getUserData()).name());
        for (Task.Priority priority : Task.Priority.values()) {
            var menuItem = new MenuItem(priority.getName());
            menuItem.setOnAction(event -> {
                this.priority.setText(menuItem.getText());
                this.priority.setUserData(priority);
                this.priority.setId(((Task.Priority) this.priority.getUserData()).name());
            });
            this.priority.getItems().add(menuItem);
        }
        this.collaboratorMenu.getItems().clear();
        for (User user : this.task.getParentProject().getUsers()) {
            var checkMenuItem = new CheckMenuItem(user.getName());
            if (this.selectedUsers.stream().map(User::getId).toList().contains(user.getId())) {
                checkMenuItem.setSelected(true);
            }
            checkMenuItem.setOnAction(event -> {
                if (checkMenuItem.isSelected()) {
                    this.selectedUsers.add(user);
                    this.newActors.add(user);
                } else {
                    this.selectedUsers.removeIf(obj -> obj.getId() == user.getId());
                    this.newActors.removeIf(obj -> obj.getId() == user.getId());
                }
                this.collaboratorMenu.setText(this.selectedUsers.size() + " user(s) selected");
                System.out.println(this.selectedUsers.toString());
                System.out.println(this.task.getParentProject().getUsers().toString());
            });
            this.collaboratorMenu.getItems().add(checkMenuItem);
        }

        this.name.setText(this.task.getName());
        this.description.setText(this.task.getDescription());
        this.deadline.setValue(new java.sql.Date(this.task.getDeadline().getTime()).toLocalDate());
    }

    public void updateProject() {
        this.task.setName(this.name.getText());
        this.task.setDescription(this.description.getText());
        this.task.setDeadline(java.sql.Date.valueOf(this.deadline.getValue()));
        this.task.setPriority((Task.Priority) this.priority.getUserData());
        this.task.setStatus((Task.Status) this.status.getUserData());
        this.task.setUpdatedAt(new Date());
        this.task.setActors(this.selectedUsers);
        try {
            DatabaseConnection.getInstance().updateTask(this.task, this.newActors);
            DatabaseConnection.getInstance().updateAllTasks(this.task.getParentProject());
            AppController.getInstance().navigateWithData(this.task);
            AppController.getInstance().updateRecentContainer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.stage.close();
    }



    public void deleteTask() {
        try {
            DatabaseConnection.getInstance().deleteTask(this.task);
            this.task.getParentProject().getTasks().remove(this.task);
            DatabaseConnection.getInstance().updateAllTasks(this.task.getParentProject());
            AppController.getInstance().navigateWithData(this.task.getParentProject());
            AppController.getInstance().deleteRecent(this.task);
            AppController.getInstance().updateRecentContainer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.stage.close();
    }
}
