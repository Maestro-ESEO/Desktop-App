package com.maestro.desktop.controllers;

import com.maestro.desktop.utils.DatabaseConnection;
import com.maestro.desktop.models.User;
import com.maestro.desktop.views.AccountView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AccountController extends NavigationViewController{

    private User user;

    private NavigableView editAccountView;


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

    @Override
    public void initialize(Object user){
        this.user = (User) user;
        accountFirstname.setText(this.user.getFirstname());
        accountLastname.setText(this.user.getLastname());
        accountEmail.setText(this.user.getEmail());
        if(this.user.getProfilePhotoPath() == null) {
            profilePicture.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/default-pfp.png")));
        }else{
            profilePicture.setImage(new Image(this.user.getProfilePhotoPath()));
        }
        System.out.println("picture: "+this.user.getProfilePhotoPath());
        accountPosition.setText(this.user.getPosition());
        projectsInProgress.setText(Integer.toString(this.user.getProjects().size()));
        tasksToDo.setText(Integer.toString(this.user.getTasksToDo()));
        tasksInProgress.setText(Integer.toString(this.user.getTasksInProgress()));
        tasksDone.setText(Integer.toString(this.user.getTasksDone()));
    }

    @FXML
    private void editAccount(){
        //view.setAccountEditView();
        userFirstname.setText(user.getFirstname());
        userLastname.setText(user.getLastname());
        userEmail.setText(user.getEmail());
        userPosition.setText(user.getPosition());
        projectsInProgress.setText(Integer.toString(user.getProjects().size()));
    }

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


