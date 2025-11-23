package com.mobilewallet.util;

import com.mobilewallet.config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility using Singleton pattern
 */
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                AppConfig.DB_URL, 
                AppConfig.DB_USER, 
                AppConfig.DB_PASSWORD
            );
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null || !isConnectionValid()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    private static boolean isConnectionValid() {
        try {
            return instance != null && 
                   instance.connection != null && 
                   !instance.connection.isClosed() &&
                   instance.connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                    AppConfig.DB_URL, 
                    AppConfig.DB_USER, 
                    AppConfig.DB_PASSWORD
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
