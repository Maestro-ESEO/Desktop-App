package com.maestro.desktop.views;

import com.maestro.desktop.controllers.AccountController;
import com.maestro.desktop.controllers.DashboardController;
import com.maestro.desktop.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static com.maestro.desktop.views.LoginView.stage;

public class DashboardView {

    private DashboardController dashboardController;

    private User user;

    public DashboardView(User user) throws IOException {
        this.user = user;
        setDashboardView();
    }

    // display the dashboard on the window
    public void setDashboardView() throws IOException {
        System.out.println("in view");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/maestro/desktop/views/dashboard.fxml"));
        javafx.scene.Parent root = loader.load();
        DashboardController controller = loader.getController();
        controller.initialize(this.user);
        Scene scene = new Scene(root, 800, 600);

        stage.setScene(scene);
        stage.setTitle("Maestro");
        stage.show();
    }

}
