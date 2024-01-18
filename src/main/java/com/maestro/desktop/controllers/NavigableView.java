package com.maestro.desktop.controllers;

import javafx.scene.Node;

public class NavigableView {
    public enum FxmlView {
        DASHBOARD("/views/dashboard-view.fxml"),
        ALL_PROJECTS("/views/all-projects-view.fxml"),
        PROJECT("/views/project-view.fxml"),
        TASK("/views/task-view.fxml");

        private final String path;
        FxmlView(String path) { this.path = path; }
    }
    private Object data;
    private String fxml;
    private Node navSource;

    public NavigableView(Object data, FxmlView fxml, Node navSource) {
        this.data = data;
        this.fxml = fxml.path;
        this.navSource = navSource;
    }

    public NavigableView(Object data, Node navSource) {
        this.data = data;
        this.navSource = navSource;
        this.fxml = null;
    }

    public void setData(Object data) { this.data = data; }

    public Object getData() {
        return data;
    }

    public String getFxml() {
        return fxml;
    }

    public void setFxml(FxmlView fxml) { this.fxml = fxml.path; }

    public Node getNavSource() {
        return navSource;
    }
}
