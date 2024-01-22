package com.maestro.desktop.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User - User model class.
 */
public class User {

    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String position;
    private String profilePhotoPath;
    private Date createdAt;
    private Date updatedAt;
    private List<Project> projects;
    private User admin;

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

    public User(int id, String firstname, String lastname, String email, String password, String picture, User admin) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.projects = new ArrayList<Project>();
        this.profilePhotoPath = picture;
        this.createdAt = new Date();
        this.admin = admin;
    }

    public User(int id, String firstName, String lastName, String email, String profilePhotoPath, Date createdAt, Date updatedAt) {
        this.id = id;
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.profilePhotoPath = profilePhotoPath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.projects = new ArrayList<>();
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

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }


    public void removeProject(Project project) { this.projects.remove(project); }

    /**
     * getTasksToDo - Get the number of tasks to do of the user.
     *
     * @return - Amount of tasks to do.
     */
    public int getTasksToDo(){
        int counter = 0;
        for(Project project : this.projects){
            counter+= project.getTasksToDo();
        }
        return counter;
    }

    /**
     * getTasksInProgress - Get the number of tasks in progress of the user.
     *
     * @return - Amount of tasks in progress.
     */
    public int getTasksInProgress(){
        int counter = 0;
        for(Project project : this.projects){
            counter+= project.getTasksInProgress();
        }
        return counter;
    }

    /**
     * getTasksDone - Get the number of tasks done of the user.
     *
     * @return - Amount of tasks done.
     */
    public int getTasksDone(){
        int counter = 0;
        for(Project project : this.projects){
            counter+= project.getTasksDone();
        }
        return counter;
    }

    /**
     * getNumberOfTasks - Calculate the number of tasks of the user.
     *
     * @return - Amount of tasks of the user.
     */
    public int getNumberOfTasks(){
        int counter = 0;
        for(Project project : this.projects){
            counter+= project.getNumberOfTasks();
        }
        return counter;
    }
}

