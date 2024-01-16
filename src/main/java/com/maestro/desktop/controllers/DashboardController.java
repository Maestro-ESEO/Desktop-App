package com.maestro.desktop.controllers;

import com.maestro.desktop.models.DatabaseConnection;
import com.maestro.desktop.models.User;
import com.maestro.desktop.views.AccountView;
import com.maestro.desktop.views.DashboardView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.maestro.desktop.models.User.setUser;

public class DashboardController {
    @FXML
    private javafx.scene.control.Button yourButton; // Make sure this matches the fx:id in your FXML
    @FXML
    private Text dashboardFirstname;
    @FXML
    private Button accountButton;

    private DashboardView view;
    private User user;


    public DashboardController(){
    }

    public void initialize(Object user){
        System.out.println("test");
        this.user = (User) user;
        dashboardFirstname.setText(this.user.getFirstname());
    }

    @FXML
    private static void timeline() {
        // Create a timeline with a KeyFrame that changes the button text every second
        Timeline timeline = new Timeline(
               // new KeyFrame(Duration.seconds(0), event -> yourButton.setText("Step 1")),
                //new KeyFrame(Duration.seconds(1), event -> yourButton.setText("Step 2")),
                //new KeyFrame(Duration.seconds(2), event -> yourButton.setText("Step 3"))
        );

        // Set the cycle count to indefinite for continuous looping
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Play the timeline
        timeline.play();
    }

}
