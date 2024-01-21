package com.maestro.desktop.models;

import java.util.Date;

public class Comment {
    private int id;
    private String content;
    private User author;
    private Date createdAt;
    private Date updatedAt;

    public Comment(int id, String content, User author, Date createdAt, Date updatedAt) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
