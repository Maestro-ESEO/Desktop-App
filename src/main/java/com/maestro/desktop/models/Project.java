package com.maestro.desktop.models;

import javafx.scene.Group;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Project(int id, String name, String description, Date startDate, Date endDate, Date createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.users = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public Project(String name, User admin) {
        this.id = 0;
        this.name = name;
        this.description = "description";
        this.startDate = new Date();
        this.endDate = new Date();
        this.createdAt = new Date();
        this.admin = admin;
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

    public List<User> getActors() {
        return Stream.concat(this.users.stream(), Stream.of(this.admin))
                .sorted(Comparator.comparing(User::getName)).toList();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String toString() { return name; }
}
