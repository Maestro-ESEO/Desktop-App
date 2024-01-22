package com.maestro.desktop.controllers;

/**
 * TaskController - Controller's methods related to the task page.
 */
import com.maestro.desktop.models.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TaskController extends NavigationViewController{
    private Task task;

    @FXML
    private Label title;

    /**
     * initialize - Sets the task.
     * @param data - Task to display.
     */
    public void initialize(Object data){
        this.task = (Task) data;
        this.title.setText(this.task.getName());
    }
}
