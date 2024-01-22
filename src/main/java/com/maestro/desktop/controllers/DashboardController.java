package com.maestro.desktop.controllers;

import com.maestro.desktop.App;
import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * DashboardController - Controller's methods related to the dashboard page.
 * DashboardController is a subclass of NavigationViewController.
 */
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

    /**
     * initialize - Sets the user logged in and displays the items of the dashboard page.
     * @param user - User logged in.
     */
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
        // for each project of the user logged in
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
                        btn.setOnAction(event -> {
                            try {
                                DatabaseConnection.getInstance().updateTaskStatus(task, Task.Status.COMPLETED);
                                task.setStatus(Task.Status.COMPLETED);
                                DatabaseConnection.getInstance().updateAllTasks(task.getParentProject());
                                AppController.getInstance().navigateWithData(task);
//                                AppController.getInstance().updateView(AppController.getInstance().getDashboard());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
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
