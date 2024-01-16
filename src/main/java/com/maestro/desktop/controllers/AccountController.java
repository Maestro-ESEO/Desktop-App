package com.maestro.desktop.controllers;

import com.maestro.desktop.models.DatabaseConnection;
import com.maestro.desktop.models.User;
import com.maestro.desktop.views.AccountView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static TextField editFirstname;
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

    @Override
    public void initialize(Object user){
        this.user = (User) user;
        accountFirstname.setText(this.user.getFirstname());
        accountLastname.setText(this.user.getLastname());
        accountEmail.setText(this.user.getEmail());
        profilePicture.setImage(new Image(getClass().getClassLoader().getResourceAsStream("com/maestro/desktop/images/profile.png")));
        //accountPosition.setText(user.getPosition());
    }

    @FXML
    private void editAccount(){
        //view.setAccountEditView();
        userFirstname.setText(user.getFirstname());
        userLastname.setText(user.getLastname());
        userEmail.setText(user.getEmail());
        userPosition.setText(user.getPosition());
    }

    @FXML
    private void saveChanges(){
        System.out.println("in");
        // Define a regular expression pattern to check the password
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$";
        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);
        // Create a matcher with the given password
        Matcher matcher = pattern.matcher(editPassword.getText());
        if(!editPassword.getText().isEmpty() && editPassword.getText().length() < 8 || matcher.matches()){
            System.out.println("in 1");
            wrongSave.setText("Password must be of at least 8 characters and contain a lower case, an upper case, a number and a special\n character."); // not working !!
        }else if(!editPassword.getText().isEmpty() && !editPassword.getText().equals(confirmPassword.getText())){
            System.out.println("in 2");
            wrongSave.setText("Passwords must be the same.");
        }else if(editPassword.getText().isEmpty() && editPassword.getText().equals(confirmPassword.getText())){
            System.out.println("in 3");
            if(!user.getFirstname().equals(editFirstname.getText())){
                DatabaseConnection.editTable("users", "first_name","id", user.getId(), editFirstname.getText());
            }
            // add data of the new account in database
            //view.setAccountView();
            //wrongLogin.setText("Your account has been successfully created!"); //not working !!
        }else{
            System.out.println("in 4");
            if(!user.getFirstname().equals(editFirstname.getText())){
                DatabaseConnection.editTable("users", "first_name","id", user.getId(), editFirstname.getText());
            }

            // add data of the new account in database
            //view.setAccountView();
        }
    }

}
