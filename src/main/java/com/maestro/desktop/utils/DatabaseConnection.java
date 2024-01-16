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
            user.setProjects(this.fetchAllProjects(user));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Project> fetchAllProjects(User user) {
        var list = new ArrayList<Project>();
        try {
            String query = String.format("select p.id, p.name, p.description, p.start_date, p.end_date, p.created_at from projects p, user_project up where up.is_admin = 1 and p.id = up.project_id and up.user_id = %d", user.getId());
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
                project.setTasks(this.fetchAllTasks(project));
                list.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Task> fetchAllTasks(Project project) {
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

    public void insertProject(Project project) {
        try {
            String query = "insert into projects(name, description, start_date, end_date, created_at, updated_at) values (?, ?, ?, ?, ?, NOW())";
            PreparedStatement prepStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            prepStatement.setString(1, project.getName());
            prepStatement.setString(2, project.getDescription());
            prepStatement.setTimestamp(3, new Timestamp(project.getStartDate().getTime()));
            prepStatement.setTimestamp(4, new Timestamp(project.getEndDate().getTime()));
            prepStatement.setTimestamp(5, new Timestamp(project.getCreatedAt().getTime()));
            prepStatement.executeUpdate();
            ResultSet rs = prepStatement.getGeneratedKeys();
            if (rs.next()) {
                project.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String query = "insert into user_project(user_id, project_id, is_admin, created_at, updated_at) values (?, ?, ?, NOW(), NOW())";
            PreparedStatement preparedStatement  = this.connection.prepareStatement(query);
            preparedStatement.setInt(1, project.getAdmin().getId());
            preparedStatement.setInt(2, project.getId());
            preparedStatement.setInt(3, 1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (User user : project.getUsers()){
            try {
                String query = "insert into user_project(user_id, project_id, is_admin, created_at, updated_at) values (?, ?, ?, NOW(), NOW())";
                PreparedStatement preparedStatement  = this.connection.prepareStatement(query);
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setInt(2, project.getId());
                preparedStatement.setInt(3, 0);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAllProjects(User user) {
        user.setProjects(this.fetchAllProjects(user));
    }

    public void updateAllTasks(Project project) {
        project.setTasks(this.fetchAllTasks(project));
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
