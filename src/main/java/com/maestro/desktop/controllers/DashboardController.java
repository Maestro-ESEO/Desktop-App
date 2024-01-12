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
import javafx.util.Duration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {
    @FXML
    private javafx.scene.control.Button yourButton; // Make sure this matches the fx:id in your FXML
    @FXML
    private Label dashboardFirstname;
    @FXML
    private Button accountButton;

    public static User user;
    private DashboardView view;


    public DashboardController(){
    }

    public DashboardController(DashboardView view, String email){
        this.view = view;
        setUser(email);
    }

    @FXML
    private void initialize() {
        // Create a timeline with a KeyFrame that changes the button text every second
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> yourButton.setText("Step 1")),
                new KeyFrame(Duration.seconds(1), event -> yourButton.setText("Step 2")),
                new KeyFrame(Duration.seconds(2), event -> yourButton.setText("Step 3"))
        );

        // Set the cycle count to indefinite for continuous looping
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Play the timeline
        timeline.play();
    }

    @FXML
    private void accessAccount(){
        AccountView account = new AccountView(user.getEmail());
    }

    private void setUser(String email){
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT * FROM `users` WHERE `email` = ?";
        try {
            ps = DatabaseConnection.getConnection().prepareStatement(query);
            ps.setString(1, email);

            rs = ps.executeQuery();

            if (rs.next()) {
                // Assuming your password column in the database is named "password"
                this.user = new User(rs.getString("first_name"),rs.getString("last_name"), rs.getString("email"), rs.getString("password"));
                System.out.println("Firstname: "+this.user.getFirstname());
                System.out.println("Lastname: "+this.user.getLastname());
                System.out.println("Email: "+this.user.getEmail());
                System.out.println("Password: "+this.user.getPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
