package com.maestro.desktop;

// MainApp.java
import javafx.application.Application;
import javafx.stage.Stage;
import com.maestro.desktop.views.LoginView;
import com.maestro.desktop.controllers.LoginController;

public class App extends Application {
	@Override
	public void start(Stage primaryStage) {
		LoginView view = new LoginView(primaryStage);
		LoginController controller = new LoginController(view);
	}

	public static void main(String[] args) {
		launch(args);
	}
}

