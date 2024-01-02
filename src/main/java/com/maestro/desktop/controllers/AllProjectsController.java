package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import javafx.css.Size;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.List;

public class AllProjectsController extends NavigationViewController{

    @FXML
    private VBox projectContainer;

    private List<Project> projectList;

    @Override
    public void initialize(Object data) {
        if (data instanceof List<?>) {
            this.projectList = (List<Project>) data;
            for (Project project : this.projectList) {
                Button btn = new Button(project.getName());
                btn.setUserData(project);
                btn.setOnAction(event -> onProjectItemClicked(project));
                this.projectContainer.getChildren().add(btn);
            }
        }
    }

    private void onProjectItemClicked(Project project) {
        for (NavigableView nav : AppController.getInstance().recents) {
            if (nav.getData() == project) {
                AppController.getInstance().updateView(nav);
                return;
            }
        }
        Button recentBtn = new Button(project.getName());
        recentBtn.setGraphicTextGap(10.0);
        recentBtn.setAlignment(Pos.TOP_LEFT);
        recentBtn.getStyleClass().setAll("default-sidebar-item");
        recentBtn.setPadding(new Insets(5, 5, 5, 10));
        recentBtn.setMaxWidth(Double.MAX_VALUE);
        Button graphic = new Button();
        graphic.getStyleClass().setAll("icon");
        graphic.setId("project-icon");
        graphic.setPrefSize(10, 10);
        graphic.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        recentBtn.setGraphic(graphic);

        NavigableView newNav = new NavigableView(project, NavigableView.FxmlView.PROJECT, recentBtn);
        recentBtn.setOnAction(event -> AppController.getInstance().updateView(newNav));
        AppController.getInstance().recents.add(0, newNav);
        if (AppController.getInstance().recents.size() > 3) {
            AppController.getInstance().recents.remove(AppController.getInstance().recents.size() - 1);
        }
        AppController.getInstance().updateRecents();
        AppController.getInstance().updateView(newNav);
    }
}
