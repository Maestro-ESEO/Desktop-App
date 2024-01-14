package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
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
import java.util.List;
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
        this.displayActors();
    }

    private String getTasksLeft() {
        if (this.project.getTasks().isEmpty()) {
            return "No tasks yet";
        }
        long tasksLeft = this.project.getTasks().stream().filter(task -> task.getStatus() != Task.Status.DONE).count();
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
        long tasksLeft = this.project.getTasks().stream().filter(task -> task.getStatus() == Task.Status.DONE).count();
        return (int) (100 * tasksLeft / totalTasks);
    }

     private void displayActors() {
        this.actors.getChildren().clear();
        var actorList = this.project.getActors();
        if (actorList.isEmpty()) {
            System.out.println("No actors found");
        } else if (actorList.size() < 4) {
            for(int i=0; i<actorList.size(); i++) {
                Button btn = new Button();
                btn.getStyleClass().setAll("actor-pfp");
                ImageView iv = new ImageView();
                iv.setUserData(actorList.get(i).getProfilePhotoPath());
                iv.setFitWidth(24);
                iv.setFitHeight(24);
                Circle clipShape = new Circle(12, 12, 12);
                iv.setClip(clipShape);
                btn.setGraphic(iv);
                if (i!=0) {
                    HBox.setMargin(btn, new Insets(0, 0, 0, -5));
                }
                this.actors.getChildren().add(btn);
                new Thread(() -> {
                    iv.setImage(new Image((String) iv.getUserData()));
                }).start();
            }
        } else {
            for(int i=0; i<3; i++) {
                Button btn = new Button();
                btn.getStyleClass().setAll("actor-pfp");
                ImageView iv = new ImageView();
                iv.setUserData(actorList.get(i).getProfilePhotoPath());
                iv.setFitWidth(24);
                iv.setFitHeight(24);
                Circle clipShape = new Circle(12, 12, 12);
                iv.setClip(clipShape);
                btn.setGraphic(iv);
                if (i!=0) {
                    HBox.setMargin(btn, new Insets(0, 0, 0, -5));
                }
                this.actors.getChildren().add(btn);
                new Thread(() -> {
                    iv.setImage(new Image((String) iv.getUserData()));
                }).start();
            }
            Button btn = new Button();
            btn.getStyleClass().setAll("actor-pfp");
            HBox.setMargin(btn, new Insets(0, 0, 0, -5));
            Label label = new Label("+" + (actorList.size()-3));
            label.setAlignment(Pos.CENTER);
            label.setPrefWidth(24);
            label.setPrefHeight(24);
            label.setMinWidth(Region.USE_PREF_SIZE);
            btn.setGraphic(label);
            this.actors.getChildren().add(btn);
        }
    }
}
