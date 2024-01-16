package com.maestro.desktop.models;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class argon {

        public static void main(String[] args) {
            // Hash the password during registration or password change
            String rawPassword = "user_password";
            String hashedPassword = hashPassword(rawPassword);
            System.out.println("Hashed password: "+hashedPassword);

            // Store hashedPassword in the database along with user information

            // During login
            String providedPassword = "user_password";

            // Verify the provided password
            boolean passwordMatches = verifyPassword(providedPassword, hashedPassword);

            // Display the result
            if (passwordMatches) {
                System.out.println("Password is correct.");
            } else {
                System.out.println("Incorrect password.");
            }
        }

        private static String hashPassword(String rawPassword) {
            Argon2 argon2 = Argon2Factory.create();
            String hashedPassword = argon2.hash(10, 65536, 1, rawPassword);
            //argon2.dispose();
            return hashedPassword;
        }

        private static boolean verifyPassword(String providedPassword, String storedHash) {
            Argon2 argon2 = Argon2Factory.create();
            boolean passwordMatches = argon2.verify(storedHash, providedPassword);
            //argon2.dispose();
            return passwordMatches;
        }
}
