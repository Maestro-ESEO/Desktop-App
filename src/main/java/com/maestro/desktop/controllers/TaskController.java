package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Comment;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.utils.ComponentFactory;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
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
    private Label priority;
    @FXML
    private MenuButton status;
    @FXML
    private Button accessProject;
    @FXML
    private VBox commentContainer;
    @FXML
    private TextArea commentTextArea;
    @FXML
    private Button publishCommentBtn;

    public void initialize(Object data){
        this.task = (Task) data;
        this.title.setText(this.task.getName());
        this.description.setText(this.task.getDescription());
        var df = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);
        this.date.setText(this.task.getDeadline() != null ? df.format(this.task.getDeadline()) : "Not specified");
        ComponentFactory.getInstance().displayActors(this.actors, 10, this.task.getActors());
        this.priority.setText(task.getPriority().getName());
        this.priority.setId(task.getPriority().name());
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
        this.commentContainer.getChildren().clear();
        for (Comment comment : this.task.getComments()) {
            this.commentContainer.getChildren().add(ComponentFactory.getInstance().createCommentItem(comment));
        }
        this.publishCommentBtn.setDisable(true);
        this.commentTextArea.textProperty().addListener((observable, oldValue, newValue) -> { this.publishCommentBtn.setDisable(this.commentTextArea.getText().isBlank()); });

    }

    public void publishComment(ActionEvent event) {
        if (this.commentTextArea.getText().isBlank()) {
            return;
        }
        var comment = new Comment(
                -1,
                this.commentTextArea.getText(),
                AppController.getInstance().getUser(),
                new Date(),
                new Date()
        );
        try {
            System.out.println(comment.getId());
            DatabaseConnection.getInstance().insertComment(comment, this.task.getId());
            System.out.println(comment.getId());
            this.commentTextArea.clear();
            this.task.getComments().add(this.task.getComments().stream().filter(obj -> obj.getCreatedAt().compareTo(comment.getCreatedAt()) <= 0)
                    .findFirst().map(obj -> this.task.getComments().indexOf(obj)).orElse(this.task.getComments().size()), comment);
           AppController.getInstance().navigateWithData(this.task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
