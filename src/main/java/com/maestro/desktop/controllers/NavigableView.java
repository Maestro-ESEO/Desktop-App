package com.maestro.desktop.controllers;

import javafx.scene.Node;

import java.sql.SQLException;
/**
 * NavigableView - Enum to choose which page to display.
 */
public class NavigableView {
    public enum FxmlView {
        DASHBOARD("/com/maestro/desktop/views/dashboard.fxml"),
        ACCOUNT("/com/maestro/desktop/views/account.fxml"),
        ACCOUNT_EDIT("/com/maestro/desktop/views/accountEdit.fxml"),
        ALL_PROJECTS("/views/all-projects-view.fxml"),
        PROJECT("/views/project-view.fxml"),
        TASK("/views/task-view.fxml"),
        LOGIN("/com/maestro/desktop/views/login.fxml"),
        EDIT_ACCOUNT("/com/maestro/desktop/views/accountEdit.fxml");

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

    /**
     * setData - Setter of the data member of the NavigableView class.
     * @param data - Data member of the class.
     */
    public void setData(Object data) { this.data = data; }

    /**
     * getData - Getter of the data member of the NavigableView class.
     * @return Object - Data member of the class.
     */
    public Object getData() {
        return data;
    }

    /**
     * getFxml - Getter of the fxml member of the NavigableView class.
     * @return String - fxml member of the class.
     */
    public String getFxml() {
        return fxml;
    }

    /**
     * setFxml - Setter of the fxml member of the NavigableView class.
     * @param fxml - Fxml member of the class.
     */
    public void setFxml(FxmlView fxml) { this.fxml = fxml.path; }

    /**
     * getNavSource - Getter of the navSource member of the NavigableView class.
     * @return Node - NavSource member of the class.
     */
    public Node getNavSource() {
        return navSource;
    }
}
