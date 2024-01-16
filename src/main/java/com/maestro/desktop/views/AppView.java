package com.maestro.desktop.views;

import com.maestro.desktop.controllers.AppController;
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

import static com.maestro.desktop.views.LoginView.stage;

public class AppView {

        public AppView(String emailUser) {
            setAppView(emailUser);
        }

        public void setAppView(String emailUser) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/app-view.fxml"));
                Parent load = loader.load();
                AppController appController = loader.getController();
                appController.initialize(emailUser);
                stage.setScene(new Scene(load));
                stage.setTitle("Maestro");
                stage.show();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

