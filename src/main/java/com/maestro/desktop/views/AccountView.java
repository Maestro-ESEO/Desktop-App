package com.maestro.desktop.views;

import com.maestro.desktop.controllers.AccountController;
import com.maestro.desktop.controllers.DashboardController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.text.BreakIterator;

import static com.maestro.desktop.views.LoginView.stage;

/**
 * AccountView - View methods for the account page.
 */
public class AccountView {

    private AccountController accountController;

    public AccountView(String email) {
        setAccountView();
    }

    // initialize the UI by loading the FXML file and setting up the stage
    /**
     * setAccountView - Displays the account page.
     */
    public void setAccountView() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/account.fxml"));

            // Load the FXML file and get the root node
            Parent root = loader.load();
            AccountController controller = loader.getController();
            controller.initialize(this);

            // Set up the stage
            stage.setTitle("Maestro");
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));

            Scene scene = stage.getScene();
            System.out.println(scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm()));
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * setAccountEditView - Displays the account edition page.
     */
    public void setAccountEditView() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/accountEdit.fxml"));

            // Load the FXML file and get the root node
            Parent root = loader.load();
            AccountController controller = loader.getController();

            // Set up the stage
            stage.setTitle("Maestro");
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));

            Scene scene = stage.getScene();
            System.out.println(scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm()));
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
