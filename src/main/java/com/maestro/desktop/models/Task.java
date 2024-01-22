package com.maestro.desktop.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {
    public enum Status {
        TO_DO("To do", 0),
        IN_PROGRESS("In Progress", 1),
        IN_REVISION("In Revision", 2),
        COMPLETED("Done", 3);

        private final int value;
        private final String name;

        Status(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid Status value: " + value);
        }
    }
    public enum Priority {
        LOW("Low", 0),
        MEDIUM("Medium", 1),
        HIGH("High", 2);
        private final String name;
        private final int value;
        Priority(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public static Priority fromValue(int value) {
            for (Priority priority : Priority.values()) {
                if (priority.value == value) {
                    return priority;
                }
            }
            throw new IllegalArgumentException("Invalid Priority value: " + value);
        }
    }
    private int id;
    private String name;
    private String description;
    private Date deadline;
    private Status status;
    private Priority priority;
    private Project parentProject;
    private Date createdAt;

    private Date updatedAt;
    private List<User> actors;
    private List<Comment> comments;

    public Task(int id, String name, String description, Date deadline, Status status, Priority priority, Project parentProject, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.parentProject = parentProject;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.actors = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Project getParentProject() {
        return parentProject;
    }

    public List<User> getActors() { return actors; }

    public void setActors(List<User> actors) {
        this.actors = actors;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
