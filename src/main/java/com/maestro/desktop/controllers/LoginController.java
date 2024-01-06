package com.maestro.desktop.controllers;

import com.maestro.desktop.views.AccountView;
import com.maestro.desktop.views.LoginView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// LoginController.java
public class LoginController {
    private static LoginView view;

    @FXML
    private Button loginButton;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Label wrongLogin;
    @FXML
    private TextField createFirstname;
    @FXML
    private TextField createLastname;
    @FXML
    private TextField createEmail;
    @FXML
    private PasswordField createPassword;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label wrongSignup;

    public LoginController(){
    }

    public LoginController(LoginView view) {
        this.view = view;
        this.view.initUI();
    }

    @FXML
    public void handleLogin(){
        if(email.getText().toString().equals("javacoding") && password.getText().toString().equals("123")) {
            wrongLogin.setText("Success!");
            Stage stg = this.view.getStage();
            AccountView account = new AccountView(LoginController.view.getStage());
        }

        else if(email.getText().isEmpty() && password.getText().isEmpty()) {
            wrongLogin.setText("Please enter your data.");
        }


        else {
            wrongLogin.setText("Wrong username or password!");
        }
    }

    @FXML
    public void createAccount(){
        view.setCreateAccountView();
    }

    @FXML
    public void signUp(){
        // Define a regular expression pattern to check the password
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$";
        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);
        // Create a matcher with the given password
        Matcher matcher = pattern.matcher(createPassword.getText());
        // test if a field is left empty
        if(createFirstname.getText().trim().isEmpty() || createLastname.getText().trim().isEmpty() || createEmail.getText().trim().isEmpty() || createPassword.getText().trim().isEmpty() || confirmPassword.getText().trim().isEmpty()){
            wrongSignup.setText("You must complete every field.");
        }
        // test if the password has every required elements
        else if(createPassword.getText().length() < 8 || matcher.matches()){
            wrongSignup.setText("Password must be of at least 8 characters and contain a lower case, an upper\n case, a number and a special character."); // not working !!
        }
        // test if the first password equals the second one
        else if(createPassword.getText().equals(confirmPassword.getText())) {
            // add data of the new account in database
            view.initUI();
            //wrongLogin.setText("Your account has been successfully created!"); //not working !!
        }else{
            wrongSignup.setText("Passwords must be the same.");
        }
    }

    @FXML
    private void backToLogin(){
        view.initUI();
    }
}