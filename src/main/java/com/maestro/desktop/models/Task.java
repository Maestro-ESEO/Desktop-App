package com.maestro.desktop.models;

import java.util.Date;

public class Task {
    public enum Status {
        TODO("To do", 0),
        IN_PROGRESS("In Progress", 1),
        IN_REVISION("In Revision", 2),
        DONE("Done", 3);

        private final String name;
        private final int value;
        Status(String name, int value) {
            this.name = name;
            this.value = value;
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
    }
    private int id;
    private String name;
    private String description;
    private Date deadline;
    private Status status;
    private Priority priority;
    private Project parentProject;

    public Task(int id, String name, String description, Date deadline, Status status, Priority priority, Project parentProject) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.parentProject = parentProject;
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

    public Priority getPriority() {
        return priority;
    }

    public Project getParentProject() {
        return parentProject;
    }
}
