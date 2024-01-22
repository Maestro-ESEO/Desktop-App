package com.maestro.desktop.controllers;

import com.maestro.desktop.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * AccountController - Controller's methods related to the account page.
 * AccountController is a subclass of NavigationViewController.
 */
public class AccountController extends NavigationViewController{

    private User user;

    @FXML
    private Text accountFirstname;
    @FXML
    private Text accountLastname;
    @FXML
    private Text accountEmail;
    @FXML
    private Text accountPosition;
    @FXML
    private Button save;
    @FXML
    private Label wrongSave;
    @FXML
    private ImageView profilePicture;
    @FXML
    private Text userFirstname;
    @FXML
    private Text userLastname;
    @FXML
    private Text userEmail;
    @FXML
    private Text userPosition;
    @FXML
    private Text projectsInProgress;
    @FXML
    private Text tasksToDo;
    @FXML
    private Text tasksInProgress;
    @FXML
    private Text tasksDone;
    @FXML
    private Button editAccount;

    /**
     * initialize - Sets the user and displays the dynamic items of the account page.
     * @param user - User logged in.
     */
    public void initialize(Object user){
        this.user = (User) user;
        accountFirstname.setText(this.user.getFirstname());
        accountLastname.setText(this.user.getLastname());
        accountEmail.setText(this.user.getEmail());
        this.profilePicture.setUserData(this.user.getProfilePhotoPath().isEmpty() ? getClass().getResource("/images/default-pfp.png").toString() : this.user.getProfilePhotoPath());
        this.profilePicture.setFitWidth(80);
        this.profilePicture.setFitHeight(80);
        Circle clipShape = new Circle(40, 40, 40);
        this.profilePicture.setClip(clipShape);
        new Thread(() -> {
            this.profilePicture.setImage(new Image((String) this.profilePicture.getUserData()));
        }).start();

        accountPosition.setText(this.user.getPosition());
        projectsInProgress.setText(Integer.toString(this.user.getProjects().size()));
        tasksToDo.setText(Integer.toString(this.user.getTasksToDo()));
        tasksInProgress.setText(Integer.toString(this.user.getTasksInProgress()));
        tasksDone.setText(Integer.toString(this.user.getTasksDone()));
    }

    /**
     * changeViewAccount - Opens a new window for the edition of the account.
     * @param event - ActionEvent detected after clicking on the "edit" button.
     * Called after clicking on the edit button.
     */
    @FXML
    public void changeViewAccount(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/maestro/desktop/views/accountEdit.fxml"));
            DialogPane pane = loader.load();
            EditAccountDialogController controller = loader.getController();
            Stage stage = new Stage();
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            controller.initialize(stage, this.user);
            stage.setScene(new Scene(pane));
            stage.setTitle("Edit account");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/maestro/desktop/images/logo.png")));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


