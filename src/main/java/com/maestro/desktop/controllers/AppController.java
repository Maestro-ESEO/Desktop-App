package com.maestro.desktop.controllers;

import com.maestro.desktop.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
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

    public static AppController getInstance() {
        return INSTANCE;
    }

    public void initialize(User user) {
        this.user = user;
        AppController.INSTANCE = this;
        this.dashboard = new NavigableView(null, NavigableView.FxmlView.DASHBOARD, dashboardButton);
        this.allProjects = new NavigableView(this.user.getProjects(), NavigableView.FxmlView.ALL_PROJECTS, allProjectsButton);
        recents = new ArrayList<>();
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
                System.out.println(nav.getData());
                controller.initialize(nav.getData());
            }
            this.currentView.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                updateView(allProjects);
            } else {
                throw new Error("Button not recognized");
            }

        }
    }

    public void updateRecents() {
        List<Button> listOfRecents = new ArrayList<>();
        for (NavigableView nav : recents) {
            listOfRecents.add((Button) nav.getNavSource());
        }
        ((VBox) this.recentContainer.getChildren().getLast()).getChildren().setAll(listOfRecents);
    }
}
