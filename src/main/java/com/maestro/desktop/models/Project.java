package com.maestro.desktop.models;

import javafx.scene.Group;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    private User admin;
    private List<User> users;
    private List<Task> tasks;

    public Project(int id, String name, String description, Date startDate, Date endDate, Date createdAt, User admin) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.admin = admin;
        this.users = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public Project(String name) {
        this.id = 0;
        this.name = name;
        this.description = "description";
        this.startDate = new Date();
        this.endDate = new Date();
        this.createdAt = new Date();
        this.admin = null;
        this.users = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public User getAdmin() {
        return admin;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public String toString() { return name; }
}
