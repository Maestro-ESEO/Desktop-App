package com.maestro.desktop.controllers;

import com.maestro.desktop.utils.DatabaseConnection;
import com.maestro.desktop.views.AccountView;
import com.maestro.desktop.views.AppView;
import com.maestro.desktop.views.DashboardView;
import com.maestro.desktop.views.LoginView;
import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static com.maestro.desktop.views.LoginView.stage;

/**
 * LoginController - Controller's methods related to the login page and the creation of account page.
 */
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

    /**
     * handleLogin - Checks the conditions to login.
     * Called after clicking on the login button.
     */
    @FXML
    public void handleLogin() {
        String emailLogin = email.getText();
        String passwordLogin = password.getText();
        // Compare the given email and password to the database data
        boolean check = DatabaseConnection.getInstance().checkLogin(emailLogin, passwordLogin);
        // Check if one of the field is empty
        if(email.getText().isEmpty() || password.getText().isEmpty()) {
            wrongLogin.setText("Please enter your data.");
        }
        // Check if the given email and password are correct
        else if(check) {
            wrongLogin.setText("Success!");
            // Display the dashboard view
            AppView appView = new AppView(email.getText(), password.getText());
            view.stage.sizeToScene();
        }
        else {
            wrongLogin.setText("Wrong username or password!");
        }
    }

    /**
     * createAccount - Calls the method from LoginView to display the page of the creation of an account.
     * Called when clicking on the "create an account" button.
     */
    @FXML
    public void createAccount(){
        view.setCreateAccountView();
    }

    /**
     * signUp - Handles the user sign-up process.
     * @throws NoSuchAlgorithmException - If the algorithm required for password hashing is not available.
     * @throws InvalidKeySpecException - If an invalid key specification is encountered during password hashing.
     * Called when clicking on the "create my account" button.
     */
    @FXML
    public void signUp() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String email = createEmail.getText();
        String password = createPassword.getText();
        String checkPassword = confirmPassword.getText();
        String lastname = createLastname.getText();
        String firstname = createFirstname.getText();
        // Define a regular expression pattern to check the password
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$";
        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);
        // Create a matcher with the given password
        Matcher matcher = pattern.matcher(createPassword.getText());
        // test if a field is left empty
        if(firstname.trim().isEmpty() || lastname.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || checkPassword.trim().isEmpty()){
            wrongSignup.setText("You must complete every field.");
        }
        // test if the email is not already in the database
        else if(DatabaseConnection.getInstance().checkEmailInDatabase(email)){
            wrongSignup.setText("Email already used.");
        }
        // test if the password has every required elements
        else if(password.length() < 8 && matcher.matches()){
            wrongSignup.setText("Password must be of at least 8 characters and contain a lower case, an upper\n case, a number and a special character."); // not working !!
        }
        // test if the first password equals the second one
        else if(password.equals(checkPassword) && isValidEmail(email)) {
            DatabaseConnection.getInstance().addSignUpData(firstname, lastname, email, password);
            stage.close();
            // add data of the new account in database
            view.initUI();
        }else{
            wrongSignup.setText("Passwords must be the same.");
        }
    }

    /**
     * isValidEmail - Check if the email has a correct format.
     * @param email - Given email.
     * @return - True if the email has a correct format, or else false.
     */
    private boolean isValidEmail(String email) {
        // Define a regular expression for a simple email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailRegex);
        // Create a matcher object
        Matcher matcher = pattern.matcher(email);
        // Return true if the email matches the pattern, otherwise false
        return matcher.matches();
    }

    /**
     * backToLogin - Calls the method from LoginView to display the login page.
     * Called when clicking on the "login" button of the creation of account page.
     */
    @FXML
    private void backToLogin(){
        stage.close();
        view.initUI();
    }
}
