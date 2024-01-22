package com.maestro.desktop.controllers;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.models.User;
import com.maestro.desktop.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EditAccountDialogController - Controller's methods related to the edit account popup page.
 */
public class EditAccountDialogController {
    private Stage stage;
    private User user;

    @FXML
    private Button save;
    @FXML
    private Button cancel;
    @FXML
    private TextField editFirstname;
    @FXML
    private TextField editLastname;
    @FXML
    private TextField editEmail;
    @FXML
    private TextField editPosition;
    @FXML
    private PasswordField editPassword;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Text wrongSave;

    /**
     * initialize - Sets the user, the stage and displays the items of the page.
     * @param user - User logged in.
     * @param stage - Stage of the window.
     */
    public void initialize(Stage stage, User user) {
        this.user = user;
        this.stage = stage;
        this.cancel.setOnAction(event -> this.stage.close());
        this.editFirstname.setText(this.user.getFirstname());
        this.editLastname.setText(this.user.getLastname());
        this.editEmail.setText(this.user.getEmail());
        if(this.user.getPosition() != null) {
            this.editPosition.setText(this.user.getPosition());
        }

    }

    /**
     * saveChanges - Checks the conditions to create an account.
     * Called when clicking on the save button.
     */
    @FXML
    private void saveChanges() {
        // Define a regular expression pattern to check the password
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$";
        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);
        // Create a matcher with the given password
        Matcher matcher = pattern.matcher(editPassword.getText());
        if(!editPassword.getText().isEmpty() && editPassword.getText().length() < 8 || matcher.matches()){
            wrongSave.setText("Password must be of at least 8 characters and contain a lower case, an\n upper case, a number and a special character.");
        }else if(!editPassword.getText().isEmpty() && !editPassword.getText().equals(confirmPassword.getText())){
            wrongSave.setText("Passwords must be the same.");
        }else{
            // Edit the modified data in the database
            if(!editFirstname.getText().isEmpty() && !user.getFirstname().equals(editFirstname.getText())){
                DatabaseConnection.getInstance().editTable("users", "first_name","id", user.getId(), editFirstname.getText());
                this.user.setFirstname(editFirstname.getText());
            }
            if(!editLastname.getText().isEmpty() && !user.getLastname().equals(editLastname.getText())){
                DatabaseConnection.getInstance().editTable("users", "last_name","id", user.getId(), editLastname.getText());
                this.user.setLastname(editLastname.getText());
            }
            if(!editEmail.getText().isEmpty() && !user.getEmail().equals(editEmail.getText())){
                DatabaseConnection.getInstance().editTable("users", "email","id", user.getId(), editEmail.getText());
                this.user.setEmail(editEmail.getText());
            }
            if(!editPosition.getText().isEmpty() && user.getPosition() != null && !user.getPosition().equals(editPosition.getText())){
                DatabaseConnection.getInstance().editTable("users", "profile_photo_path","id", user.getId(), editPosition.getText());
            }
            if(!editPassword.getText().isEmpty() && !user.getPassword().equals(editPassword.getText())){
                DatabaseConnection.getInstance().editTable("users", "password","id", user.getId(), editPassword.getText());
                this.user.setPassword(editPassword.getText());
            }
            AppController.getInstance().getProfileBtn().setText(this.user.getName());
            AppController.getInstance().updateView(AppController.getInstance().getAccount());
            this.stage.close();
        }
    }
}
