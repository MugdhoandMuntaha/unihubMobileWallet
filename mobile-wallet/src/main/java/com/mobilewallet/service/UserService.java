package com.mobilewallet.service;

import com.mobilewallet.model.User;
import com.mobilewallet.util.DatabaseConnection;

import java.sql.*;

/**
 * User service for managing user data
 */
public class UserService {
    
    private Connection connection;
    
    public UserService() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setEmail(rs.getString("email"));
                user.setBalance(rs.getDouble("balance"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setActive(rs.getBoolean("is_active"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get user by phone number
     */
    public User getUserByPhone(String phoneNumber) {
        String query = "SELECT * FROM users WHERE phone_number = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, phoneNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setEmail(rs.getString("email"));
                user.setBalance(rs.getDouble("balance"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setActive(rs.getBoolean("is_active"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Update user profile
     */
    public boolean updateProfile(int userId, String fullName, String email) {
        String query = "UPDATE users SET full_name = ?, email = ? WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setInt(3, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get current balance
     */
    public double getBalance(int userId) {
        String query = "SELECT balance FROM users WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    /**
     * Update balance
     */
    public boolean updateBalance(int userId, double newBalance) {
        String query = "UPDATE users SET balance = ? WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
