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
    private javafx.scene.control.Button yourButton; // Make sure this matches the fx:id in your FXML
    @FXML
    private Text dashboardFirstname;
    @FXML
    private Button accountButton;
    @FXML
    private TilePane taskContainer;
    @FXML
    private Text nbProject;
    @FXML
    private Text nbTask;

    private List<Project> projectList;

    private DashboardView view;
    private User user;

    @Override
    public void initialize(Object user) {
        this.user = (User) user;
        dashboardFirstname.setText(this.user.getFirstname());
        nbProject.setText(Integer.toString(this.user.getProjects().size()));
        /*String sql = "INSERT INTO user_task (user_id, task_id, created_at, updated_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, 23);
            preparedStatement.setInt(2, 43);
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Row added successfully!");
            } else {
                System.out.println("Failed to add a row.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        System.out.println("Project amount: "+this.user.getProjects().size());
        for (Project project : this.user.getProjects()) {
            List<Task> tasks = project.getTasks();
            System.out.println("Tasks amount: "+tasks.size());
            for (Task task : tasks) {
                if(task.getStatus() == Task.Status.IN_REVISION) {
                    //try {
                        var loader = new FXMLLoader(getClass().getResource("views/components/taskInRevision-tile.fxml"));
                        //Button btn = loader.load();
                        //TaskTileController controller = loader.getController();
                        //controller.initialize(task);
                        //btn.setOnAction(event -> AppController.getInstance().navigateWithData(project));
                        //this.taskContainer.getChildren().add(btn);
                    /*} catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        }
    }

    @FXML
    public void testTask(){
        System.out.println("success!");
    }
}
