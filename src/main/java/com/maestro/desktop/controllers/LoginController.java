package com.maestro.desktop.controllers;

import com.maestro.desktop.models.DatabaseConnection;
import com.maestro.desktop.views.AccountView;
import com.maestro.desktop.views.DashboardView;
import com.maestro.desktop.views.LoginView;
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

import static com.maestro.desktop.models.DatabaseConnection.executeQuery;
import static com.maestro.desktop.models.DatabaseConnection.getConnection;

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
    public void handleLogin() throws IOException {
        String emailLogin = email.getText();
        String passwordLogin = password.getText();
        boolean check = checkLogin(emailLogin, passwordLogin);
        System.out.println("Email found?: " + check);
        if(email.getText().isEmpty() && password.getText().isEmpty()) {
            wrongLogin.setText("Please enter your data.");
        }
        else if(check) {
            wrongLogin.setText("Success!");
            DashboardView dashboard = new DashboardView(emailLogin);
        }
        else {
            wrongLogin.setText("Wrong username or password!");
        }
    }

    public boolean checkLogin(String email, String password) {
        PreparedStatement ps;
        ResultSet rs;
        boolean isPasswordCorrect = false;
        String query = "SELECT * FROM `users` WHERE `email` = ?";
        try {
            ps = DatabaseConnection.getConnection().prepareStatement(query);
            ps.setString(1, email);

            rs = ps.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                isPasswordCorrect = decrypt(storedPassword, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return isPasswordCorrect;
    }

    @FXML
    public void createAccount(){
        view.setCreateAccountView();
    }

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
        // test if the password has every required elements
        else if(password.length() < 8 || matcher.matches()){
            wrongSignup.setText("Password must be of at least 8 characters and contain a lower case, an upper\n case, a number and a special character."); // not working !!
        }
        // test if the first password equals the second one
        else if(password.equals(checkPassword)) {
            checkSignup(firstname, lastname, email, password);
            // add data of the new account in database
            view.initUI();
            wrongLogin.setText("Your account has been successfully created!"); //not working !!
        }else{
            wrongSignup.setText("Passwords must be the same.");
        }
    }

    public boolean checkSignup(String firstname, String lastname, String email, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String hashedPassword = hash(password); // Hash the password
        PreparedStatement ps;
        boolean accountAdded = false;
        String query = "INSERT INTO users(first_name,last_name,email,password) VALUES(?,?,?,?)";
        try {
            ps = DatabaseConnection.getConnection().prepareStatement(query);
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, email);
            ps.setString(4, hashedPassword);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User added successfully!");
                accountAdded = true;
            } else {
                System.out.println("Failed to add user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountAdded;
    }

    @FXML
    private void backToLogin(){
        view.initUI();
    }

    // hash the password when creating an account
    public String hash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String generatedSecuredPasswordHash = generateStrongPasswordHash(password);
        System.out.println(generatedSecuredPasswordHash);
        return generatedSecuredPasswordHash;
    }

    // use to hash the password
    private static String generateStrongPasswordHash(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    // use to hash the password
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    // use to hash the password
    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    // decrypt the password to login
    public boolean decrypt(String hashedPassword, String loginPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        boolean matched = validatePassword(loginPassword, hashedPassword);
        return matched;
    }

    // use to decrypt the password
    private static boolean validatePassword(String originalPassword, String storedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
                salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    // use to decrypt the password
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

}