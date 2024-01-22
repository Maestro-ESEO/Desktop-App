package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NewProjectDialogController {
    private List<User> collaborators;
    private Stage stage;

    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TextField findCollaborators;
    @FXML
    private TilePane collaboratorPane;
    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;

    public void initialize(Stage stage) {
        this.stage = stage;
        this.startDate.setValue(LocalDate.now());
        this.endDate.setValue(LocalDate.now().plusMonths(1));
        this.createButton.setDisable(true);
        this.createButton.setOnAction(event -> this.createProject());
        this.cancelButton.setOnAction(event -> this.stage.close());
        this.name.textProperty().addListener((observable, oldValue, newValue) -> { this.createButton.setDisable(this.name.getText().isBlank()); });
        this.findCollaborators.setOnAction(event -> this.checkUser());
        this.findCollaborators.textProperty().addListener((observable, oldValue, newValue) -> { this.findCollaborators.getStyleClass().setAll("text-field"); });
        this.collaborators = new ArrayList<>();
    }

    public void checkUser() {
        String email = this.findCollaborators.getText().toLowerCase().strip();
        try  {
            User fetchedUser = DatabaseConnection.getInstance().fetchUserFromEmail(email);
            if (fetchedUser == null) {
                this.findCollaborators.getStyleClass().setAll("error-container");
                return;
            } else if (collaborators.stream().anyMatch(user -> user.getId() == fetchedUser.getId())) {
                return;
            } else {
                Label collaborator = new Label(fetchedUser.getName());
                Button deleteBtn = new Button();
                deleteBtn.setId("delete-collaborator");
                deleteBtn.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                deleteBtn.setPrefSize(12, 12);

                HBox hBox = new HBox(5, deleteBtn, collaborator);
                hBox.getStyleClass().setAll("collaborator");
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                this.collaboratorPane.getChildren().add(hBox);
                deleteBtn.setOnAction(event -> {
                    this.collaborators.remove(fetchedUser);
                    this.collaboratorPane.getChildren().remove(hBox);
                    this.stage.sizeToScene();
                });
                this.collaborators.add(fetchedUser);
                this.stage.sizeToScene();
                this.findCollaborators.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createProject() {
        Project project = new Project(
                -1,
                this.name.getText(),
                this.description.getText(),
                java.sql.Date.valueOf(this.startDate.getValue()),
                java.sql.Date.valueOf(this.endDate.getValue()),
                new Date(),
                new Date(),
                AppController.getInstance().getUser()
        );
        project.setUsers(this.collaborators);
        try {
            DatabaseConnection.getInstance().insertProject(project);
            DatabaseConnection.getInstance().updateAllProjects(AppController.getInstance().getUser());
            AppController.getInstance().navigateWithData(project);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.stage.close();
    }
}
