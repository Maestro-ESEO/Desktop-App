package com.maestro.desktop.controllers;

import com.maestro.desktop.App;
import com.maestro.desktop.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;


public class AppController {

    private User user;

    @FXML
    private AnchorPane currentView;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button projectsButton;


    public void initialize(User user) {
        System.out.println(user.getName());
        this.user = user;
        updateSidebar(dashboardButton);
        updateView("/views/dashboard-view.fxml");

    }

    private void updateView(String fxml) {
        try {
            AnchorPane view = (AnchorPane) FXMLLoader.load(getClass().getResource(fxml));
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            this.currentView.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateView(String fxml, Object data) {
        try {
            AnchorPane view = (AnchorPane) FXMLLoader.load(getClass().getResource(fxml));
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            this.currentView.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateSidebar(Button selBtn) {
        for (Button btn : new Button[]{dashboardButton, projectsButton}) {
            if (btn == selBtn) {
                selBtn.getStyleClass().setAll("selected-sidebar-item");
            } else {
                btn.getStyleClass().setAll("default-sidebar-item");
            }
        }
    }

    public void logout() {
        System.out.println("Logging out");
    }

    public void changeView(ActionEvent e) {
        Object source = e.getSource();
        System.out.println(source);
        if (source instanceof Button) {
            if (source == dashboardButton) {
                updateSidebar(dashboardButton);
                updateView("/views/dashboard-view.fxml");
            } else if (source == projectsButton){
                updateSidebar(projectsButton);
                updateView("/views/projects-view.fxml");
            } else {
                updateView("/views/project-view.fxml", ((Button) source).getUserData());
            }

        }
    }
}
