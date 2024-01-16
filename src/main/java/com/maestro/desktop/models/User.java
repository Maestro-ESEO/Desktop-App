package com.maestro.desktop.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class User {

    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String position;
    private String profilePhotoPath;
    private Date createdAt;
    private List<Project> projects;

    public User(int id, String firstname, String lastname, String email, String password, String picture) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.projects = new ArrayList<Project>();
        this.profilePhotoPath = picture;
        this.createdAt = new Date();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String name) {
        this.firstname = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String name) {
        this.lastname = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePhotoPath() {
        return this.profilePhotoPath;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return this.firstname + " " + this.lastname;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public static void setProjects(List<Project> projects) {
        projects = projects;
    }

    public static User setUser(String email){
        User userCreation = null;
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT * FROM `users` WHERE `email` = ?";
        try {
            ps = DatabaseConnection.getConnection().prepareStatement(query);
            ps.setString(1, email);

            rs = ps.executeQuery();

            if (rs.next()) {
                // Assuming your password column in the database is named "password"
                userCreation = new User(rs.getInt("id"),rs.getString("first_name"),rs.getString("last_name"), rs.getString("email"), rs.getString("password"), rs.getString("profile_photo_path"));
                System.out.println("Id: "+userCreation.getId());
                System.out.println("Firstname: "+userCreation.getFirstname());
                System.out.println("Lastname: "+userCreation.getLastname());
                System.out.println("Email: "+userCreation.getEmail());
                System.out.println("Password: "+userCreation.getPassword());
                System.out.println("Picture: "+userCreation.getProfilePhotoPath());
            }else{
                System.out.println("User not completed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            /*String sql = "INSERT INTO user_project (user_id, project_id) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
                preparedStatement.setInt(1, 25);
                preparedStatement.setInt(2, 8);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Row added successfully!");
                } else {
                    System.out.println("Failed to add a row.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }*/
        initializeProjects(userCreation);
        return userCreation;
    }

    private static void initializeProjects(User user) {
        List<Integer> projectsId = new ArrayList<>();
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT project_id FROM user_project WHERE user_id = ?";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, user.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    projectsId.add(resultSet.getInt("project_id"));
                    System.out.println("Project ID: " + resultSet.getInt("project_id"));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        for (Integer id : projectsId) {
            System.out.println("Id: "+id);
            sql = "SELECT * FROM projects WHERE id = ?";
            try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Project project = new Project(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getDate("start_date"), resultSet.getDate("end_date"), resultSet.getDate("created_at"));
                        projects.add(project);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        setProjects(projects);
    }

    public int getId() { return this.id; }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}

