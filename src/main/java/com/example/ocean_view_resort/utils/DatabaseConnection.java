package com.example.ocean_view_resort.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection – singleton for driver loading and config.
 *
 * getConnection() returns a FRESH connection every time so that
 * each DAO call/try-with-resources block gets its own independent
 * connection. Sharing one connection across concurrent requests caused
 * "ResultSet closed" errors because one thread's try-with-resources
 * would close the singleton connection while another thread was still
 * iterating its ResultSet.
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;

    // Database configuration
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/ocean_view_resort"
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "Chiran123$";

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Returns a new, independent database connection.
     * Callers are responsible for closing it (use try-with-resources).
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}

