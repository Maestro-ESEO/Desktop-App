package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AppController {

	@FXML
	private Button loginButton;

	public void handleLoginButton(ActionEvent e) {
		User loggedInUser = new User("John Doe", "jdoe@mail.com", "1234");
		loggedInUser.addProject(new Project("Project1"));

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
			Parent load = loader.load();
			DashboardController dashboardController = loader.getController();
			dashboardController.setLoggedInUser(loggedInUser);
			Stage dashboardStage = new Stage();
			dashboardStage.setScene(new Scene(load));
			dashboardStage.setTitle("Dashboard");
			dashboardStage.show();
			Stage loginStage = (Stage) loginButton.getScene().getWindow();
			loginStage.close();

		} catch (IOException error) {
			error.printStackTrace();
		}
	}
}
