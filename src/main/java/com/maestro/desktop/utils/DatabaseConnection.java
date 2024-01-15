package com.maestro.desktop.utils;

import com.maestro.desktop.models.Project;
import com.maestro.desktop.models.Task;
import com.maestro.desktop.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import java.sql.DriverManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String URL = "jdbc:mysql://monorail.proxy.rlwy.net:56240/railway";
    private static final String USER = "root";
    private static final String PASS = "HcCee21C-F43ff-FaDf6d4g4A5C5eEc6";

    private DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASS);
    }
    public Connection getConnection() {
        return connection;
    }
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public User login(String email, String password) {
        try {
            String query = String.format("select id, first_name, last_name, email, profile_photo_path, created_at from users where email = '%s' and password = '%s'", email, password);
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("profile_photo_path"),
                    this.dateFromString(rs.getString("created_at"))
            );
            user.setProjects(this.getAllProjects(user));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Project> getAllProjects(User user) {
        var list = new ArrayList<Project>();
        try {
            String query = String.format("select p.id, p.name, p.description, p.start_date, p.end_date, p.created_at from projects p, user_project up where p.id = up.project_id and up.user_id = %d", user.getId());
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                var project = new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        this.dateFromString(rs.getString("start_date")),
                        this.dateFromString(rs.getString("end_date")),
                        this.dateFromString(rs.getString("created_at")),
                        user
                );
                project.setTasks(this.getAllTasks(project));
                list.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Task> getAllTasks(Project project) {
        var list = new ArrayList<Task>();
        try {
            String query = String.format("select id, name, description, deadline, status, priority, created_at from tasks where project_id = '%d'", project.getId());
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                var task = new Task(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        this.dateFromString(rs.getString("deadline")),
                        Task.Status.fromValue(rs.getInt("status")),
                        Task.Priority.fromValue(rs.getInt("priority")),
                        project,
                        this.dateFromString(rs.getString("created_at"))
                );
                list.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    private Date dateFromString(String str) {
        var df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = df.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(0);
        }
    }
}
