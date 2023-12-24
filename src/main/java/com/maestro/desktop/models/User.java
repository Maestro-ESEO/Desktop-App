package com.maestro.desktop.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    private String name;
    private String email;
    private String password;

    private List<Project> projects;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.projects = new ArrayList<Project>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void addProject(Project project) {
        this.projects.add(project);
    }
}
