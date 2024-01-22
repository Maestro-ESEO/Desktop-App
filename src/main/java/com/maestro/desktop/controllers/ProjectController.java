package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.utils.ComponentFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * ProjectController - Controller's methods related to the project page.
 */
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
    @FXML
    private Button accessProjects;

    /**
     * initialize - Sets the project and displays the items of the page.
     * @param data - Project to display.
     */
    @Override
    public void initialize(Object data) {
        this.project = (Project) data;
        this.title.setText(this.project.getName());
        this.description.setText(this.project.getDescription());
        var df = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);
        this.date.setText(this.project.getEndDate() != null ? df.format(this.project.getEndDate()) : "Not specified");
        ComponentFactory.getInstance().displayActors(this.actors, 10, this.project.getActors());
        this.displayTasks();
        this.accessProjects.setOnAction(event -> AppController.getInstance().updateView(AppController.getInstance().getAllProjects()));
    }

    /**
     * displayTasks - Displays the different tasks of the project on the page.
     */
    public void displayTasks() {
        List<Task> inRevisionList = this.project.getTasks().stream().filter(task -> task.getStatus() == Task.Status.IN_REVISION).toList();
        List<Task> toDoList = this.project.getTasks().stream().filter(task -> task.getStatus() == Task.Status.TO_DO).toList();
        List<Task> inProgressList = this.project.getTasks().stream().filter(task -> task.getStatus() == Task.Status.IN_PROGRESS).toList();
        List<Task> completedList = this.project.getTasks().stream().filter(task -> task.getStatus() == Task.Status.COMPLETED).toList();

        if (!inRevisionList.isEmpty()) {
            this.inRevisionTasks.getChildren().clear();
            for (Task task : inRevisionList) {
                HBox taskItem = ComponentFactory.getInstance().createTaskItem(task);
                taskItem.setOnMouseClicked(event -> AppController.getInstance().navigateWithData(task));
                this.inRevisionTasks.getChildren().add(taskItem);
            }
        }

        if (!toDoList.isEmpty()) {
            this.toDoTasks.getChildren().clear();
            for (Task task : toDoList) {
                HBox taskItem = ComponentFactory.getInstance().createTaskItem(task);
                taskItem.setOnMouseClicked(event -> AppController.getInstance().navigateWithData(task));
                this.toDoTasks.getChildren().add(taskItem);
            }
        }

        if (!inProgressList.isEmpty()) {
            this.inProgressTasks.getChildren().clear();
            for (Task task : inProgressList) {
                HBox taskItem = ComponentFactory.getInstance().createTaskItem(task);
                taskItem.setOnMouseClicked(event -> AppController.getInstance().navigateWithData(task));
                this.inProgressTasks.getChildren().add(taskItem);
            }
        }

        if (!completedList.isEmpty()) {
            this.completedTasks.getChildren().clear();
            for (Task task : completedList) {
                HBox taskItem = ComponentFactory.getInstance().createTaskItem(task);
                taskItem.setOnMouseClicked(event -> AppController.getInstance().navigateWithData(task));
                this.completedTasks.getChildren().add(taskItem);
            }
        }
    }

    /**
     * addTask - Opens a new window and displays the page to create a new task.
     * @param event - ActionEvent raised when clicking on the create a new task button.
     */
    public void addTask(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dialogs/new-task-dialog.fxml"));
            DialogPane pane = loader.load();
            NewTaskDialogController controller = loader.getController();
            Stage stage = new Stage();
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            controller.initialize(stage, this.project);
            stage.setScene(new Scene(pane));
            stage.setTitle("New Task");
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/maestro/desktop/images/logo.png")));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editProject(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dialogs/edit-project-dialog.fxml"));
            DialogPane pane = loader.load();
            EditProjectDialogController controller = loader.getController();
            Stage stage = new Stage();
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            controller.initialize(stage, this.project);
            stage.setScene(new Scene(pane));
            stage.setTitle("Edit Project");
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/maestro/desktop/images/logo.png")));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
