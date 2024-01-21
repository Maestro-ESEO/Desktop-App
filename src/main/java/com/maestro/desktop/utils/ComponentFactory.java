package com.maestro.desktop.utils;

import com.maestro.desktop.controllers.AppController;
import com.maestro.desktop.models.Comment;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.input.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ComponentFactory {
    private static ComponentFactory instance;

    public static ComponentFactory getInstance() {
        if (instance == null) {
            instance = new ComponentFactory();
        }
        return instance;
    }

    public void displayActors(HBox container, int limit, List<User> actorList) {
        container.getChildren().clear();
        if (actorList.isEmpty()) {
            System.out.println("No actors found");
        } else if (actorList.size() <= limit) {
            for (int i = 0; i < actorList.size(); i++) {
                Button btn = new Button();
                btn.setMouseTransparent(true);
                btn.setOnAction(event -> btn.getParent().fireEvent(event));
                btn.getStyleClass().setAll("actor-pfp");
                ImageView iv = new ImageView();
                iv.setUserData(actorList.get(i).getProfilePhotoPath());
                iv.setFitWidth(24);
                iv.setFitHeight(24);
                Circle clipShape = new Circle(12, 12, 12);
                iv.setClip(clipShape);
                btn.setGraphic(iv);
                if (i != 0) {
                    HBox.setMargin(btn, new Insets(0, 0, 0, -5));
                }
                container.getChildren().add(btn);
                new Thread(() -> {
                    iv.setImage(new Image((String) iv.getUserData()));
                }).start();
            }
        } else {
            for (int i = 0; i < 3; i++) {
                Button btn = new Button();
                btn.setMouseTransparent(true);
                btn.getStyleClass().setAll("actor-pfp");
                ImageView iv = new ImageView();
                iv.setUserData(actorList.get(i).getProfilePhotoPath());
                iv.setFitWidth(24);
                iv.setFitHeight(24);
                Circle clipShape = new Circle(12, 12, 12);
                iv.setClip(clipShape);
                btn.setGraphic(iv);
                if (i != 0) {
                    HBox.setMargin(btn, new Insets(0, 0, 0, -5));
                }
                container.getChildren().add(btn);
                new Thread(() -> {
                    iv.setImage(new Image((String) iv.getUserData()));
                }).start();
            }
            Button btn = new Button();
            btn.setMouseTransparent(true);
            btn.getStyleClass().setAll("actor-pfp");
            HBox.setMargin(btn, new Insets(0, 0, 0, -5));
            Label label = new Label("+" + (actorList.size() - 3));
            label.setAlignment(Pos.CENTER);
            label.setPrefWidth(24);
            label.setPrefHeight(24);
            label.setMinWidth(Region.USE_PREF_SIZE);
            btn.setGraphic(label);
            container.getChildren().add(btn);
        }
    }

    public HBox createTaskItem(Task task) {
        if (task == null) {
            return null;
        }
        Label itemTitle = new Label(task.getName());
        itemTitle.getStyleClass().setAll("item-title");

        Line sep = new Line(0, 0, 0, 20);
        sep.getStyleClass().setAll("line");
        Line sep2 = new Line(0, 0, 0, 20);
        sep2.getStyleClass().setAll("line");
        Line sep3 = new Line(0, 0, 0, 20);
        sep3.getStyleClass().setAll("line");

        Button calendarIcon = new Button();
        calendarIcon.setMouseTransparent(true);
        calendarIcon.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        calendarIcon.setPrefSize(16, 16);
        calendarIcon.setId("calendar-icon");
        var df = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);
        Label deadlineText = new Label(task.getDeadline() != null ? df.format(task.getDeadline()) : "Not specified");
        deadlineText.getStyleClass().setAll("item-due-date");
        HBox deadline = new HBox(5, calendarIcon, deadlineText);
        deadline.setAlignment(Pos.CENTER_LEFT);

        HBox taskActors = new HBox();
        this.displayActors(taskActors, 4, task.getActors());

        Label priority = new Label(task.getPriority().getName());
        priority.setId(task.getPriority().name());
        priority.setPadding(new Insets(2, 7, 2, 7));

        Region filler = new Region();
        filler.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(filler, Priority.ALWAYS);

        HBox item = new HBox(15, itemTitle, sep, deadline, sep2, priority, sep3, taskActors, filler);
        item.setPadding(new Insets(10, 15, 10, 15));
        item.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().setAll("task-item");

        if (task.getStatus() == Task.Status.IN_REVISION) {
            Button acceptBtn = new Button();
            acceptBtn.setId("accept-icon");
            acceptBtn.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            acceptBtn.setPrefSize(20, 20);
            acceptBtn.setOnAction(event -> {
                try {
                    DatabaseConnection.getInstance().updateTaskStatus(task, Task.Status.COMPLETED);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                task.setStatus(Task.Status.COMPLETED);
                AppController.getInstance().navigateWithData(task.getParentProject());
            });

            Button rejectBtn = new Button();
            rejectBtn.setId("refuse-icon");
            rejectBtn.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            rejectBtn.setPrefSize(20, 20);
            rejectBtn.setOnAction(event -> {
                try {
                    DatabaseConnection.getInstance().updateTaskStatus(task, Task.Status.IN_PROGRESS);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                task.setStatus(Task.Status.IN_PROGRESS);
                AppController.getInstance().navigateWithData(task.getParentProject());
            });

            item.getChildren().addAll(acceptBtn, rejectBtn);
        }
        item.setUserData(task);

        return item;
    }

    public VBox createCommentItem(Comment comment) {
        if (comment == null) {
            return null;
        }
        ImageView iv = new ImageView();
        iv.setUserData(comment.getAuthor().getProfilePhotoPath());
        iv.setFitWidth(32);
        iv.setFitHeight(32);
        Circle clipShape = new Circle(16, 16, 16);
        iv.setClip(clipShape);

        Label author = new Label(comment.getAuthor().getName());
        author.getStyleClass().setAll("comment-author");
        var df = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);
        Label publishedDate = new Label(comment.getCreatedAt() != null ? df.format(comment.getCreatedAt()) : "No date specified");
        publishedDate.getStyleClass().setAll("published-date");
        VBox vBox = new VBox(1, author, publishedDate);

        HBox hbox = new HBox(10, iv, vBox);
        Label content = new Label(comment.getContent());
        content.setWrapText(true);
        content.setMinHeight(Region.USE_PREF_SIZE);
        content.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);

        VBox item = new VBox(15, hbox, content);
        item.setPadding(new Insets(15, 15, 15, 15));
        item.setPrefSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        item.setMaxWidth(500);
        item.getStyleClass().setAll("new-comment");

        new Thread(() -> {
            iv.setImage(new Image((String) iv.getUserData()));
        }).start();

        return item;
    }
}