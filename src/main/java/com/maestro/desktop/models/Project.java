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
    private Date updatedAt;
    private User admin;
    private List<User> users;
    private List<Task> tasks;

    public Project(int id, String name, String description, Date startDate, Date endDate, Date createdAt, Date updatedAt, User admin) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.admin = admin;
        this.users = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getUpdatedAt() { return  updatedAt; }

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

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String toString() { return name; }

    public int getTasksToDo(){
        int counter = 0;
        for(Task task : this.tasks){
            if(task.getStatus() == Task.Status.TO_DO){
                counter++;
            }
        }
        return counter;
    }

    public int getTasksInProgress(){
        int counter = 0;
        for(Task task : this.tasks){
            if(task.getStatus() == Task.Status.IN_PROGRESS){
                counter++;
            }
        }
        return counter;
    }

    public int getTasksDone(){
        int counter = 0;
        for(Task task : this.tasks){
            if(task.getStatus() == Task.Status.COMPLETED){
                counter++;
            }
        }
        return counter;
    }

    public int getNumberOfTasks(){
        return this.tasks.size();
    }
}
