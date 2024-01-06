package com.maestro.desktop.controllers;

import com.maestro.desktop.models.User;
import com.maestro.desktop.views.AccountView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountController {
    private User user;
    private static AccountView view;

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
    private Button save;
    @FXML
    private Label wrongSave;

    public AccountController(){
        this.user = new User("John Doe","javacoding","123");
        setAccount();
    }

    public AccountController(AccountView view){
        this.view = view;
        this.user = new User("John Doe","javacoding","123");
        setAccount();
    }

    @FXML
    private void setAccount(){
        System.out.println(editFirstname);
        //firstname.setText("test");
       // lastname.setText();
        //email.setText(this.user.getEmail());
        //position.setText();

    }

    @FXML
    private void editAccount(){
        view.setAccountEditView();
    }

    @FXML
    private void saveChanges(){
        // Define a regular expression pattern to check the password
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$";
        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);
        // Create a matcher with the given password
        Matcher matcher = pattern.matcher(editPassword.getText());
        if(editPassword.getText().length() < 8 || matcher.matches()){
            wrongSave.setText("Password must be of at least 8 characters and contain a lower case, an upper case, a number and a special\n character."); // not working !!
        }
        else if(editPassword.getText().equals(confirmPassword.getText())) {
            // add data of the new account in database
            view.setAccountEditView();
            //wrongLogin.setText("Your account has been successfully created!"); //not working !!
        }else{
            wrongSave.setText("Passwords must be the same.");
        }
    }

}
