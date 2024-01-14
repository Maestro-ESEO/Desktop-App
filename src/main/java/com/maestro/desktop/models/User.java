package com.maestro.desktop.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {

    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String picture;
    private String position;

    private List<Project> projects;

    public User(int id, String firstname, String lastname, String email, String password, String picture) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.projects = new ArrayList<Project>();
        this.picture = picture;
    }

    public int getId(){ return this.id; }

    public void setId(int id){ this.id = id; }

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

    public String getPicture(){ return this.picture; }
    public String getPosition(){ return this.position; }
    public void setPosition(String position){ this.position = position; }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
import java.util.Date;
import java.util.List;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePhotoPath;
    private Date createdAt;
    private List<Project> projects;

    public User(int id, String firstName, String lastName, String email, String profilePhotoPath, Date createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePhotoPath = profilePhotoPath;
        this.createdAt = createdAt;
        this.projects = new ArrayList<>();
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public List<Project> getProjects() {
        return this.projects;
    }

    public String getProfilePhotoPath() { return this.profilePhotoPath; }

    public void addProject(Project project) {
        this.projects.add(project);
    }
}
