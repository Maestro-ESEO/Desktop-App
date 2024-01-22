package com.maestro.desktop.utils;

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

/**
 * ComponentFactory - Methods to display the tile.
 */
public class ComponentFactory {
    private static ComponentFactory instance;

    /**
     * getInstance - Create or get the instance of this class.
     *
     * @return - Instance of the ComponentFactory class.
     */
    public static ComponentFactory getInstance() {
        if (instance == null) {
            instance = new ComponentFactory();
        }
        return instance;
    }

    /**
     * displayActors - Displays the actors of the project.
     *
     * @param container - Container in which to display the actors.
     * @param limit - Limit of profile picture to display.
     * @param actorList - List of user related to the project.
     */
    public void displayActors(HBox container, int limit, List<User> actorList) {
        container.getChildren().clear();
        if (actorList.isEmpty()) {
            System.out.println("No actors found");
        } else if (actorList.size() <= limit) {
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
                container.getChildren().add(btn);
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
                container.getChildren().add(btn);
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
            container.getChildren().add(btn);
        }
    }

    /**
     * createTaskItem - Create the component of the task.
     *
     * @param task - Task to create.
     */
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

        Button calendarIcon = new Button();
        calendarIcon.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        calendarIcon.setPrefSize(16, 16);
        calendarIcon.setId("calendar-icon");
        var df = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);
        Label deadlineText = new Label(task.getDeadline()!= null ? df.format(task.getDeadline()) : "Not specified");
        deadlineText.getStyleClass().setAll("item-due-date");
        HBox deadline = new HBox(5, calendarIcon, deadlineText);
        deadline.setAlignment(Pos.CENTER_LEFT);

        HBox taskActors = new HBox();
        this.displayActors(taskActors, 4, task.getActors());

        Region filler = new Region();
        filler.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(filler, Priority.ALWAYS);

        HBox item = new HBox(15, itemTitle, sep, deadline, sep2, taskActors, filler);
        item.setPadding(new Insets(10, 15, 10, 15));
        item.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().setAll("task-item");

        if (task.getStatus() == Task.Status.IN_REVISION) {
            Button acceptBtn = new Button();
            acceptBtn.setId("accept-icon");
            acceptBtn.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            acceptBtn.setPrefSize(20, 20);

            Button rejectBtn = new Button();
            rejectBtn.setId("refuse-icon");
            rejectBtn.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            rejectBtn.setPrefSize(20, 20);

            item.getChildren().addAll(acceptBtn, rejectBtn);
        }
        item.setUserData(task);

        return item;
    }
}
