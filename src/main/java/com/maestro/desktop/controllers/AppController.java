package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    public User getUser() { return this.user; }

    public NavigableView getAllProjects() { return allProjects; }

    public void initialize(User user) {
        this.user = user;
        this.profileBtn.setText(this.user.getName());
        Circle clipShape = new Circle(15, 15, 15);
        this.profileBtn.getGraphic().setClip(clipShape);
        ((ImageView) this.profileBtn.getGraphic()).setImage(new Image(this.user.getProfilePhotoPath()));
        AppController.INSTANCE = this;
        this.dashboard = new NavigableView(null, NavigableView.FxmlView.DASHBOARD, dashboardButton);
        this.allProjects = new NavigableView(this.user.getProjects(), NavigableView.FxmlView.ALL_PROJECTS, allProjectsButton);
        recents = new ArrayList<>();
        this.recentContainer.setVisible(false);
        updateView(dashboard);
    }

    public void updateView(NavigableView nav) {
//        DatabaseConnection.getInstance().updateView(nav);
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
            if ((data instanceof Project
                    && nav.getData() instanceof Project
                    && ((Project) data).getId() == ((Project) nav.getData()).getId())
                    || (data instanceof Task
                    && nav.getData() instanceof Task
                    && ((Task) data).getId() == ((Task) nav.getData()).getId()) ) {
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

        NavigableView newRecentNavigableView = new NavigableView(data, newRecentButton);
        if (data instanceof Project) {
            newRecentNavigableView.setFxml(NavigableView.FxmlView.PROJECT);
        } else if (data instanceof Task) {
            newRecentNavigableView.setFxml(NavigableView.FxmlView.TASK);
        } else {
            return;
        }
        newRecentButton.setOnAction(event -> AppController.getInstance().updateView(newRecentNavigableView));


        this.recents.add(0, newRecentNavigableView);
        if (this.recents.size() > 5) {
            this.recents.remove(this.recents.size() - 1);
        }
        ((VBox) this.recentContainer.getChildren().getLast()).getChildren().setAll(this.recents.stream().map(NavigableView::getNavSource).toList());
        this.updateView(newRecentNavigableView);
    }

    public void createNewProject(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dialogs/new-project-dialog.fxml"));
            DialogPane pane = loader.load();
            NewProjectDialogController controller = loader.getController();
            Stage stage = new Stage();
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            controller.initialize(stage);
            stage.setTitle("New Project");
            stage.setScene(new Scene(pane));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRecentContainer() {
        for (NavigableView nav : this.recents) {
            ((Button) nav.getNavSource()).setText(nav.getData().toString());
        }
        ((VBox) this.recentContainer.getChildren().getLast()).getChildren().setAll(this.recents.stream().map(NavigableView::getNavSource).toList());
    }

    public void deleteRecent(Object data) {
        NavigableView nav = this.recents.stream().filter(obj -> obj.getData() == data).toList().getFirst();
        this.recents.remove(nav);
    }
}
