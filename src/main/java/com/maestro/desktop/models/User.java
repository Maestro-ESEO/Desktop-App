package com.maestro.desktop.models;

import java.util.ArrayList;
import java.util.Arrays;
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

    public int getId() { return this.id; }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
