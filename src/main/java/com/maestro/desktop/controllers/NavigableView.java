package com.maestro.desktop.controllers;

import javafx.scene.Node;

public class NavigableView {
    public enum FxmlView {
        DASHBOARD("/views/dashboard-view.fxml"),
        ALL_PROJECTS("/views/all-projects-view.fxml"),
        PROJECT("/views/project-view.fxml");

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

    public void setData(Object data) { this.data = data; }

    public Object getData() {
        return data;
    }

    public String getFxml() {
        return fxml;
    }

    public Node getNavSource() {
        return navSource;
    }
}
