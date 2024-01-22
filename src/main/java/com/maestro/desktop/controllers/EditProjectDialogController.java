package com.maestro.desktop.controllers;

import com.maestro.desktop.App;
import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditProjectDialogController {

    private List<User> collaborators;
    private List<User> newCollaborators;
    private Stage stage;
    private Project project;

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
    private Button updateButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;

    public void initialize(Stage stage, Project project) {
        this.stage = stage;
        this.project = project;
        this.updateButton.setDisable(true);
        this.updateButton.setOnAction(event -> this.updateProject());
        this.cancelButton.setOnAction(event -> this.stage.close());
        this.deleteButton.setOnAction(event -> this.deleteProject());
        this.name.textProperty().addListener((observable, oldValue, newValue) -> { this.updateButton.setDisable(this.name.getText().isBlank()); });
        this.findCollaborators.setOnAction(event -> this.checkUser());
        this.findCollaborators.textProperty().addListener((observable, oldValue, newValue) -> { this.findCollaborators.getStyleClass().setAll("text-field"); });
        this.collaborators = new ArrayList<>(this.project.getUsers());
        this.newCollaborators = new ArrayList<>();
        for (User user : this.collaborators) {
            Label collaborator = new Label(user.getName());
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
                this.collaborators.remove(user);
                this.collaboratorPane.getChildren().remove(hBox);
                this.stage.sizeToScene();
            });
            this.stage.sizeToScene();
        }
        this.name.setText(this.project.getName());
        this.description.setText(this.project.getDescription());
        this.startDate.setValue(LocalDate.ofInstant(this.project.getStartDate().toInstant(), ZoneId.systemDefault()));
        this.endDate.setValue(LocalDate.ofInstant(this.project.getEndDate().toInstant(), ZoneId.systemDefault()));
    }

    public void updateProject() {
        this.project.setName(this.name.getText());
        this.project.setDescription(this.description.getText());
        this.project.setStartDate(java.sql.Date.valueOf(this.startDate.getValue()));
        this.project.setEndDate(java.sql.Date.valueOf(this.endDate.getValue()));
        this.project.setUpdatedAt(new Date());
        this.project.setUsers(this.collaborators);
        try {
            DatabaseConnection.getInstance().updateProject(this.project, this.newCollaborators);
            DatabaseConnection.getInstance().updateAllProjects(AppController.getInstance().getUser());
            AppController.getInstance().navigateWithData(this.project);
            AppController.getInstance().updateRecentContainer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.stage.close();
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
                    this.newCollaborators.remove(fetchedUser);
                    this.collaboratorPane.getChildren().remove(hBox);
                    this.stage.sizeToScene();
                });
                this.collaborators.add(fetchedUser);
                this.newCollaborators.add(fetchedUser);
                this.stage.sizeToScene();
                this.findCollaborators.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProject() {
        try {
            DatabaseConnection.getInstance().deleteProject(this.project);
            AppController.getInstance().getUser().removeProject(this.project);
            DatabaseConnection.getInstance().updateAllProjects(AppController.getInstance().getUser());
            AppController.getInstance().updateView(AppController.getInstance().getAllProjects());
            AppController.getInstance().deleteRecent(this.project);
            AppController.getInstance().updateRecentContainer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.stage.close();
    }
}
