package com.maestro.desktop.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
    private String name;
    private String description;
    private Date startDate;
    private Date deadLine;
    private Date createdTime;
    private User admin;
    private List<User> users;

    public Project(String name, String description, Date startDate, Date deadLine, User admin, List<User> users) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.deadLine = deadLine;
        this.admin = admin;
        this.users = users;
    }

    public Project(String name) {
        this.name = name;
        this.description = "";
        this.users = new ArrayList<User>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
