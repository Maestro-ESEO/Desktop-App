package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;


public class AppController {

    private static AppController INSTANCE = new AppController();
    private User user;

    public List<NavigableView> recents;

    private NavigableView dashboard;

    private NavigableView allProjects;

    @FXML
    private AnchorPane currentView;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button allProjectsButton;

    @FXML
    private VBox recentContainer;

    @FXML
    private Button profileBtn;

    public static AppController getInstance() {
        return INSTANCE;
    }

    public void initialize(User user) {
        this.user = user;
        this.profileBtn.setText(this.user.getName());
        AppController.INSTANCE = this;
        this.dashboard = new NavigableView(null, NavigableView.FxmlView.DASHBOARD, dashboardButton);
        this.allProjects = new NavigableView(this.user.getProjects(), NavigableView.FxmlView.ALL_PROJECTS, allProjectsButton);
        recents = new ArrayList<>();
        this.recentContainer.setVisible(false);
        updateView(dashboard);
    }

    public void updateView(NavigableView nav) {
        if (nav.getNavSource() != null && nav.getNavSource() instanceof Button) {
            for (NavigableView sidebarItem : Stream.concat(recents.stream(), Stream.of(dashboard, allProjects)).toList()) {
                if (sidebarItem.getNavSource() == nav.getNavSource()) {
                    sidebarItem.getNavSource().getStyleClass().setAll("selected-sidebar-item");
                } else {
                    sidebarItem.getNavSource().getStyleClass().setAll("default-sidebar-item");
                }
            }
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nav.getFxml()));
            AnchorPane view = (AnchorPane) loader.load();
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            NavigationViewController controller = loader.getController();
            if (nav.getData() != null) {
                controller.initialize(nav.getData());
            }
            this.currentView.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.recentContainer.setVisible(!this.recents.isEmpty());
    }

    public void logout() {
        System.out.println("Logging out");
    }

    public void changeView(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof Button) {
            if (source == dashboardButton) {
                updateView(this.dashboard);
            } else if (source == allProjectsButton){
                allProjects.setData(this.user.getProjects());
                updateView(allProjects);
            } else {
                throw new Error("Button not recognized");
            }
        }
    }

    public void navigateWithData(Object data) {

        // Check if already in recent Navigable Views
        for (NavigableView nav : recents) {
            if (nav.getData() == data) {
                AppController.getInstance().updateView(nav);
                return;
            }
        }

        // New button for new Navigable View
        Button newRecentButton = new Button(data.toString());

        // Button styling
        newRecentButton.setGraphicTextGap(10.0);
        newRecentButton.setAlignment(Pos.TOP_LEFT);
        newRecentButton.getStyleClass().setAll("default-sidebar-item");
        newRecentButton.setPadding(new Insets(5, 5, 5, 10));
        newRecentButton.setMaxWidth(Double.MAX_VALUE);
        Button graphic = new Button();
        graphic.getStyleClass().setAll("icon");
        graphic.setId("recent-icon");
        graphic.setPrefSize(10, 10);
        graphic.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        newRecentButton.setGraphic(graphic);

        NavigableView newRecentNavigableView = new NavigableView(data, NavigableView.FxmlView.PROJECT, newRecentButton);
        newRecentButton.setOnAction(event -> AppController.getInstance().updateView(newRecentNavigableView));

        this.recents.add(0, newRecentNavigableView);
        if (this.recents.size() > 5) {
            this.recents.remove(this.recents.size() - 1);
        }
        ((VBox) this.recentContainer.getChildren().getLast()).getChildren().setAll(this.recents.stream().map(NavigableView::getNavSource).toList());
        this.updateView(newRecentNavigableView);
    }

    public void createNewProject(ActionEvent event) {
        Project project = new Project(
                420,
                "Test",
                "Delete Later",
                new Date(),
                new Date(),
                new Date(),
                this.user
        );
        try {
            DatabaseConnection.getInstance().insertProject(project);
            DatabaseConnection.getInstance().updateAllProjects(this.user);
            this.navigateWithData(project);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
