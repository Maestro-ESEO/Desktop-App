package com.maestro.desktop.utils;

import com.maestro.desktop.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

import java.sql.SQLException;
import java.util.List;

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
}
