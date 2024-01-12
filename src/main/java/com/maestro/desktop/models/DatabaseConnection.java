package com.maestro.desktop.models;

import java.sql.*;


public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://monorail.proxy.rlwy.net:56240/railway";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "HcCee21C-F43ff-FaDf6d4g4A5C5eEc6";

    private static Connection connection;

        public static void initialize() throws SQLException {
            //Class.forName(driverClassName);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }

        public void shutdown() throws SQLException {
            if (connection != null) {
                connection.close();
            }
        }

        public static ResultSet executeQuery(String query) throws SQLException {
            try (
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(query);
            ){
                while (rs.next()) {
                    //System.out.print(rs.getString("first_name"));
                }
                return rs;
            }
        }

    public static Connection getConnection() {
        return connection;
    }
}
