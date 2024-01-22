package com.maestro.desktop;

// MainApp.java
import com.maestro.desktop.utils.DatabaseConnection;
import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.User;
import javafx.application.Application;
import javafx.stage.Stage;
import com.maestro.desktop.views.LoginView;
import com.maestro.desktop.controllers.LoginController;

import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {
	@Override
	public void start(Stage primaryStage) throws SQLException {
		DatabaseConnection.getInstance();
		LoginView view = new LoginView(primaryStage);
		LoginController controller = new LoginController(view);
	}

	public static void main(String[] args) {
		launch(args);
	}
}

