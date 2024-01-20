package com.maestro.desktop.views;

// LoginView.java
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginView {

    public static Stage stage;

    public LoginView(Stage primaryStage) {
        this.stage = primaryStage;
    }

    // initialize the UI by loading the FXML file and setting up the stage
    public void initUI() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/maestro/desktop/views/login.fxml"));

            // Set the controller for the FXML file (if not set in FXML file)
            //loader.setController(this);

            // Load the FXML file and get the root node
            Parent root = loader.load();

            // Set up the stage
            stage.setTitle("Login Maestro");
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/maestro/desktop/images/logo.png")));

            Scene scene = stage.getScene();
            scene.getStylesheets().add(getClass().getResource("/com/maestro/desktop/styles/style.css").toExternalForm());
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void setCreateAccountView(){
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/maestro/desktop/views/createAccount.fxml"));

            // Load the FXML file and get the root node
            Parent root = loader.load();

            // Set up the stage
            stage.setTitle("Login Maestro");
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/maestro/desktop/images/logo.png")));

            Scene scene = stage.getScene();
            scene.getStylesheets().add(getClass().getResource("/com/maestro/desktop/styles/style.css").toExternalForm());
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
        // Implement this method if needed
        return null;
    }

    @FXML
    public String getPassword() {
        TextField password;
        String text ; //= password.getText();
        return null ; //text;
    }


}

