package com.maestro.desktop.utils;

import com.maestro.desktop.controllers.AppController;
import com.maestro.desktop.controllers.NavigableView;
import com.maestro.desktop.controllers.ProjectController;
import com.maestro.desktop.models.Comment;
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
    public static DatabaseConnection getInstance(){
        try {
            if (instance == null) {
                instance = new DatabaseConnection();
            } else if (instance.getConnection().isClosed()) {
                instance = new DatabaseConnection();
            }
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User login(String email, String password) throws SQLException {
        String query = String.format("select id, first_name, last_name, email, profile_photo_path, created_at, updated_at from users where email = '%s' and password = '%s'", email, password);
        Statement stmt = this.connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        User user = new User(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("profile_photo_path"),
                this.dateFromString(rs.getString("created_at")),
                this.dateFromString(rs.getString("updated_at"))
        );
        user.setProjects(this.fetchAllProjects(user));
        return user;
    }

    public List<Project> fetchAllProjects(User user) throws SQLException {
        var list = new ArrayList<Project>();
        String query = String.format("select p.id, p.name, p.description, p.start_date, p.end_date, p.created_at, p.updated_at from projects p, user_project up where up.is_admin = 1 and p.id = up.project_id and up.user_id = %d", user.getId());
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
                    this.dateFromString(rs.getString("updated_at")),
                    user
            );
            project.setTasks(this.fetchAllTasks(project));
            list.add(project);
        }
        return list;
    }

    public List<Task> fetchAllTasks(Project project) throws SQLException {
        var list = new ArrayList<Task>();
        String query = String.format("select id, name, description, deadline, status, priority, created_at, updated_at from tasks where project_id = '%d'", project.getId());
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
                    this.dateFromString(rs.getString("created_at")),
                    this.dateFromString(rs.getString("updated_at"))
            );
            task.setActors(this.fetchTaskActors(task));
            task.setComments(this.fetchTaskComments(task));
            list.add(task);
        }
        return list;
    }

    public List<User> fetchTaskActors(Task task) throws SQLException {
        var list = new ArrayList<User>();
        String query = String.format("select u.id, u.first_name, u.last_name, u.email, u.profile_photo_path, u.created_at, u.updated_at from users u, user_task ut where ut.task_id = '%d' and ut.user_id = u.id", task.getId());
        Statement statement = this.connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            var user = new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("profile_photo_path"),
                    this.dateFromString(rs.getString("created_at")),
                    this.dateFromString(rs.getString("updated_at"))
            );
            list.add(user);
        }
        return list;
    }

    public List<Comment> fetchTaskComments(Task task) throws SQLException{
        var list = new ArrayList<Comment>();
        String query = "select * from comments where task_id = ? order by created_at desc";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setInt(1, task.getId());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            var comment = new Comment(
                    rs.getInt("id"),
                    rs.getString("content"),
                    this.fetchUserFromID(rs.getInt("user_id")),
                    this.dateFromString(rs.getString("created_at")),
                    this.dateFromString(rs.getString("updated_at"))
            );
            list.add(comment);
        }
        return list;
    }

    public User fetchUserFromID(int id) throws SQLException{
        String query = "select u.id, u.first_name, u.last_name, u.email, u.profile_photo_path, u.created_at, u.updated_at from users u where u.id = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
            var user = new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("profile_photo_path"),
                    this.dateFromString(rs.getString("created_at")),
                    this.dateFromString(rs.getString("updated_at"))
            );
            return user;
        } else {
            return null;
        }
    }

    public void insertProject(Project project) throws SQLException {
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

        String query2 = "insert into user_project(user_id, project_id, is_admin, created_at, updated_at) values (?, ?, ?, NOW(), NOW())";
        PreparedStatement preparedStatement  = this.connection.prepareStatement(query2);
        preparedStatement.setInt(1, project.getAdmin().getId());
        preparedStatement.setInt(2, project.getId());
        preparedStatement.setInt(3, 1);
        preparedStatement.executeUpdate();

        for (User user : project.getUsers()){
            String query3 = "insert into user_project(user_id, project_id, is_admin, created_at, updated_at) values (?, ?, ?, NOW(), NOW())";
            PreparedStatement preparedStatement2  = this.connection.prepareStatement(query3);
            preparedStatement2.setInt(1, user.getId());
            preparedStatement2.setInt(2, project.getId());
            preparedStatement2.setInt(3, 0);
            preparedStatement2.executeUpdate();
        }
    }

    public void insertTask(Task task) throws SQLException {
        String query = "insert into tasks(name, description, deadline, status, priority, project_id, created_at, updated_at) values (?, ?, ?, ?, ?, ?, NOW(), NOW())";
        PreparedStatement prepStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        prepStatement.setString(1, task.getName());
        prepStatement.setString(2, task.getDescription());
        prepStatement.setTimestamp(3, new Timestamp(task.getDeadline().getTime()));
        prepStatement.setInt(4, task.getStatus().getValue());
        prepStatement.setInt(5, task.getPriority().getValue());
        prepStatement.setInt(6, task.getParentProject().getId());
        prepStatement.executeUpdate();
        ResultSet rs = prepStatement.getGeneratedKeys();
        if (rs.next()) {
            task.setId(rs.getInt(1));
        }

        for (User user : task.getActors()){
            String query3 = "insert into user-task(user_id, task_id, created_at, updated_at) values (?, ?, NOW(), NOW())";
            PreparedStatement preparedStatement  = this.connection.prepareStatement(query3);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, task.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void insertComment(Comment comment, int taskID) throws SQLException{
        String query = "insert into comments(content, user_id, task_id, created_at, updated_at) values (?, ?, ?, NOW(), NOW())";
        PreparedStatement prepStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        prepStatement.setString(1, comment.getContent());
        prepStatement.setInt(2, AppController.getInstance().getUser().getId());
        prepStatement.setInt(3, taskID);
        prepStatement.executeUpdate();
        ResultSet rs = prepStatement.getGeneratedKeys();
        if (rs.next()) {
            comment.setId(rs.getInt(1));
        }
    }

    public void updateAllProjects(User user) throws SQLException {
        user.setProjects(this.fetchAllProjects(user));
    }

    public void updateAllTasks(Project project) throws SQLException {
        project.setTasks(this.fetchAllTasks(project));
    }

    public void updateTaskStatus(Task task, Task.Status status) throws SQLException{
        String query = "update tasks set status = ? where id = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setInt(1, status.getValue());
        preparedStatement.setInt(2, task.getId());
        preparedStatement.executeUpdate();
    }

    public void checkProjectUpdate(Project project) throws SQLException{
        String query = "select p.name, p.description, p.start_date, p.end_date, p.updated_at from projects p where p.id = ? and p.updated_at != ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setInt(1, project.getId());
        preparedStatement.setTimestamp(2, new Timestamp(project.getUpdatedAt().getTime()));
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            project.setName(rs.getString("name"));
            project.setDescription(rs.getString("description"));
            project.setStartDate(this.dateFromString(rs.getString("start_date")));
            project.setEndDate(this.dateFromString(rs.getString("end_date")));
            project.setUpdatedAt(this.dateFromString(rs.getString("updated_at")));
        }
    }

    private Date dateFromString(String str) {
        var df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = df.parse(str);
            return date;
        } catch (ParseException | NullPointerException e) {
            return new Date(0);
        }
    }
}
