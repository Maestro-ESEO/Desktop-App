package com.maestro.desktop.controllers;

import com.maestro.desktop.models.DatabaseConnection;
import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.models.User;
import com.maestro.desktop.views.AccountView;
import com.maestro.desktop.views.DashboardView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DashboardController extends NavigationViewController{
    @FXML
    private Text dashboardFirstname;
    @FXML
    private TilePane taskContainer;
    @FXML
    private Text nbProject;
    @FXML
    private Text nbTask;

    private User user;

    @Override
    public void initialize(Object user) {
        int counter = 0;
        this.user = (User) user;
        dashboardFirstname.setText(this.user.getFirstname());
        nbProject.setText(Integer.toString(this.user.getProjects().size()));
        nbTask.setText(Integer.toString(this.user.getNumberOfTasks()));
        // set space between the tile
        taskContainer.setVgap(10);
        // center the tile
        taskContainer.setAlignment(Pos.CENTER);
        // for each project of the logged in user
        for (Project project : this.user.getProjects()) {
            List<Task> tasks = project.getTasks();
            for (Task task : tasks) {
                // check if the task is in revision status and if there is less than 3 tasks displayed
                if(task.getStatus() == Task.Status.IN_REVISION && counter < 3) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/components/taskInRevision-tile.fxml"));
                        Button btn = loader.load();
                        TaskTileController controller = loader.getController();
                        controller.initialize(task);
                        // Set the alignment of the button within the TilePane
                        TilePane.setAlignment(btn, Pos.CENTER);
                        // set the button to display the page of the related project
                        btn.setOnAction(event -> AppController.getInstance().navigateWithData(task));
                        this.taskContainer.getChildren().add(btn);
                        counter++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
