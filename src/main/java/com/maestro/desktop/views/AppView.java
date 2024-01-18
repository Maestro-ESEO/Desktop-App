package com.maestro.desktop.views;

import com.maestro.desktop.controllers.AppController;
import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static com.maestro.desktop.views.LoginView.stage;

public class AppView {

    private User loggedInUser;
        public AppView(String emailUser, String password) {
            setAppView(emailUser, password);
        }

        public void setAppView(String emailUser, String password) {
            try {
                this.loggedInUser = DatabaseConnection.getInstance().login(emailUser, password);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/app-view.fxml"));
                Parent load = loader.load();
                AppController appController = loader.getController();
                appController.initialize(loggedInUser);
                stage.setScene(new Scene(load));
                stage.setTitle("Maestro");
                stage.show();
            } catch (IOException error) {
                error.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

