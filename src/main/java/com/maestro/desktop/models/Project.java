package com.maestro.desktop.models;

import javafx.scene.Group;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Project - Project model class.
 */
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

    /**
     * getId - Getter the id member of the Project class.
     * @return - Id member.
     */
    public int getId() {
        return id;
    }

    /**
     * setId - Setter the id member of the Project class.
     * @param id - Id member.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getName - Getter the name member of the Project class.
     * @return - Name member.
     */
    public String getName() {
        return name;
    }

    /**
     * getDescription - Getter the description member of the Project class.
     * @return - Description member.
     */
    public String getDescription() {
        return description;
    }

    /**
     * getStartDate - Getter the startDate member of the Project class.
     * @return - StartDate member.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * getEndDate - Getter the endDate member of the Project class.
     * @return - EndDate member.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * getCreatedAt - Getter the createdAt member of the Project class.
     * @return - CreatedAt member.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * getUpdatedAt - Getter the updatedAt member of the Project class.
     * @return - UpdatedAt member.
     */
    public Date getUpdatedAt() { return  updatedAt; }

    /**
     * getAdmin - Getter the admin member of the Project class.
     * @return - Admin member.
     */
    public User getAdmin() {
        return admin;
    }

    /**
     * getUsers - Getter the users member of the Project class.
     * @return - Users member.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * getTasks - Getter the tasks member of the Project class.
     * @return - Tasks member.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * getActors - Gets the different users of the project.
     * @return - List of actors of the project.
     */
    public List<User> getActors() {
        return Stream.concat(this.users.stream(), Stream.of(this.admin))
                .sorted(Comparator.comparing(User::getName)).toList();
    }

    /**
     * setUsers - Setter the users member of the Project class.
     * @param users - Users of the project.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * setTasks - Setter the tasks member of the Project class.
     * @param tasks - Tasks of the project.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * setName - Setter the name member of the Project class.
     * @param name - Name of the project.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setDescription - Setter the description member of the Project class.
     * @param description - Description of the project.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * setStartDate - Setter the startDate member of the Project class.
     * @param startDate - StartDate of the project.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * setEndDate - Setter the endDate member of the Project class.
     * @param endDate - EndDate of the project.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * setCreatedAt - Setter the createdAt member of the Project class.
     * @param createdAt - CreatedAt date of the project.
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * setUpdatedAt - Setter the updatedAt member of the Project class.
     * @param updatedAt - UpdatedAt date of the project.
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * setAdmin - Setter the admin member of the Project class.
     * @param admin - Admin of the project.
     */
    public void setAdmin(User admin) {
        this.admin = admin;
    }

    /**
     * toString - Gets the name of the project.
     * @return - Name of the project.
     */
    public String toString() { return name; }

    /**
     * getTasksToDo - Calculate the number of tasks to do.
     * @return - Amount of tasks to do.
     */
    public int getTasksToDo(){
        int counter = 0;
        for(Task task : this.tasks){
            if(task.getStatus() == Task.Status.TO_DO){
                counter++;
            }
        }
        return counter;
    }

    /**
     * getTasksInProgress - Calculate the number of tasks in progress.
     * @return - Amount of tasks in progress.
     */
    public int getTasksInProgress(){
        int counter = 0;
        for(Task task : this.tasks){
            if(task.getStatus() == Task.Status.IN_PROGRESS){
                counter++;
            }
        }
        return counter;
    }

    /**
     * getTasksDone - Calculate the number of tasks done.
     * @return - Amount of tasks done.
     */
    public int getTasksDone(){
        int counter = 0;
        for(Task task : this.tasks){
            if(task.getStatus() == Task.Status.COMPLETED){
                counter++;
            }
        }
        return counter;
    }

    /**
     * getNumberOfTasks- Calculate the number of tasks of the user.
     * @return - Amount of tasks of the user.
     */
    public int getNumberOfTasks(){
        return this.tasks.size();
    }
}
