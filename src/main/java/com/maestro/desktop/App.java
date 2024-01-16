package com.maestro.desktop;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/views/login-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Maestro");
		stage.setScene(scene);
		stage.show();
    }

	public static void main(String[] args) {
		launch();
	}

}