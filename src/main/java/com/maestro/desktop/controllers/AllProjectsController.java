package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * AllProjectsController - Controller's methods related to the all projects page.
 * AllProjectsController is a subclass of NavigationViewController.
 */
public class AllProjectsController extends NavigationViewController{

    @FXML
    private TilePane projectContainer;

    private List<Project> projectList;

    /**
     * initialize - Displays the page with all the projects of the user.
     * @param data - List of projects of the user.
     */
    @Override
    public void initialize(Object data) {

        if (data instanceof List<?>) {
            this.projectList = (List<Project>) data;
            for (Project project : this.projectList) {
                try{
                    var loader = new FXMLLoader(getClass().getResource("/views/components/project-tile.fxml"));
                    Button btn = loader.load();
                    ProjectTileController controller = loader.getController();
                    controller.initialize(project);
                    btn.setOnAction(event -> AppController.getInstance().navigateWithData(project));
                    this.projectContainer.getChildren().add(btn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new Error("Could not process init data (Wrong type)");
        }
    }
}
