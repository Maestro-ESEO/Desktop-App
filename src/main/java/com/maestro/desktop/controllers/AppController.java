package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import com.maestro.desktop.views.LoginView;
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

import static com.maestro.desktop.views.LoginView.stage;

/**
 * AppController - Controller's methods related to the sidebar and the global app.
 */
public class AppController {

    private static AppController INSTANCE = new AppController();
    private User user;
    public List<NavigableView> recents;
    private NavigableView dashboard;
    private NavigableView allProjects;
    private NavigableView account;

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
    @FXML
    private Button logout;

    public static AppController getInstance() {
        return INSTANCE;
    }

    public User getUser() { return this.user; }

    public NavigableView getAllProjects() { return allProjects; }
    public Button getProfileBtn() { return this.profileBtn; }

    /**
     * initialize - Sets the user and displays the items from the sidebar.
     * @param user - User logged in.
     * @throws SQLException - If a SQL exception occurs during user initialization.
     */
    public void initialize(User user) throws SQLException {
        this.user = user;
        this.profileBtn.setText(this.user.getName());
        Circle clipShape = new Circle(15, 15, 15);
        this.profileBtn.getGraphic().setClip(clipShape);
        if(this.user.getProfilePhotoPath() != null) {
            ((ImageView) this.profileBtn.getGraphic()).setImage(new Image(this.user.getProfilePhotoPath()));
        }else{
            ((ImageView) this.profileBtn.getGraphic()).setImage(new Image("/com/maestro/desktop/images/profile.png"));
        }
        AppController.INSTANCE = this;
        this.dashboard = new NavigableView(this.user, NavigableView.FxmlView.DASHBOARD, dashboardButton);
        this.allProjects = new NavigableView(this.user.getProjects(), NavigableView.FxmlView.ALL_PROJECTS, allProjectsButton);
        this.account = new NavigableView(this.user, NavigableView.FxmlView.ACCOUNT, profileBtn);
        recents = new ArrayList<>();
        this.recentContainer.setVisible(false);
        updateView(dashboard);
    }

    /**
     * updateView - Sets the user and displays the items from the sidebar.
     * @param nav - User logged in.
     */
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

    /**
     * logout - Destroys the user instance and displays the login page.
     */
    public void logout() {
        this.user = null;
        stage.close();
        LoginView view = new LoginView(stage);
        LoginController controller = new LoginController(view);
        System.out.println("Logging out");
    }

    /**
     * changeView - Choose which page to display.
     * @param e - ActionEvent raised when a button is activated.
     */
    public void changeView(ActionEvent e) {
        this.user = DatabaseConnection.getInstance().updateUser(this.user.getId());
        Object source = e.getSource();
        if (source instanceof Button) {
            if (source == dashboardButton) {
                updateView(dashboard);
            } else if (source == allProjectsButton) {
                allProjects.setData(this.user.getProjects());
                System.out.println("projects: " + this.user.getProjects());
                updateView(allProjects);
            } else if (source == profileBtn) {
                updateView(account);
            } else if (source == logout) {
                logout();
            }else {
                throw new Error("Button not recognized");
            }
        }
    }

    /**
     * navigateWithData - Displays the recent pages in the sidebar.
     * @param data - User logged in.
     */
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

    /**
     * createNewProject - Creates a new project and adds it in the database.
     * @param event - ActionEvent raised when clicking on the "New project" button.
     */
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
      
    /**
     * getAccount - Getter for the account member of the AppController class.
     * @return NavigableView - The account member of the class.
     */
    public NavigableView getAccount(){
        return this.account;
    }
}
