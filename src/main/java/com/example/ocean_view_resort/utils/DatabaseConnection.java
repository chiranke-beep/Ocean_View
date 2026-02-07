package com.example.ocean_view_resort.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton pattern for Database Connection
 * Provides a single instance of database connection throughout the application
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Database configuration
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ocean_view_resort";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Chiran123$";
    
    /**
     * Private constructor to prevent instantiation
     */
    private DatabaseConnection() {
        try {
            Class.forName(DB_DRIVER);
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to establish database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Thread-safe method to get single instance of DatabaseConnection
     * @return instance of DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Get the database connection
     * @return Connection object
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            System.out.println("Connection is closed, re-establishing: " + e.getMessage());
            try {
                this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return this.connection;
    }
    
    /**
     * Close the database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
