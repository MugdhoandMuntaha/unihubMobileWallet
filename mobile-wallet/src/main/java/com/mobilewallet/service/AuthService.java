package com.mobilewallet.service;

import com.mobilewallet.model.User;
import com.mobilewallet.util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Authentication service for login and registration
 */
public class AuthService {
    
    private Connection connection;
    
    public AuthService() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Authenticate user with phone and PIN
     */
    public User login(String phoneNumber, String pin) {
        String query = "SELECT * FROM users WHERE phone_number = ? AND is_active = TRUE";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, phoneNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("pin_hash");
                
                if (BCrypt.checkpw(pin, storedHash)) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Register new user
     */
    public boolean register(String fullName, String phoneNumber, String email, String pin) {
        // Check if phone number already exists
        if (isPhoneNumberExists(phoneNumber)) {
            return false;
        }
        
        String query = "INSERT INTO users (full_name, phone_number, email, pin_hash, balance) " +
                      "VALUES (?, ?, ?, ?, 0.00)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String hashedPin = BCrypt.hashpw(pin, BCrypt.gensalt());
            
            stmt.setString(1, fullName);
            stmt.setString(2, phoneNumber);
            stmt.setString(3, email);
            stmt.setString(4, hashedPin);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if phone number already exists
     */
    private boolean isPhoneNumberExists(String phoneNumber) {
        String query = "SELECT COUNT(*) FROM users WHERE phone_number = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, phoneNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Verify PIN for current user
     */
    public boolean verifyPin(int userId, String pin) {
        String query = "SELECT pin_hash FROM users WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("pin_hash");
                return BCrypt.checkpw(pin, storedHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Change user PIN
     */
    public boolean changePin(int userId, String oldPin, String newPin) {
        if (!verifyPin(userId, oldPin)) {
            return false;
        }
        
        String query = "UPDATE users SET pin_hash = ? WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String hashedPin = BCrypt.hashpw(newPin, BCrypt.gensalt());
            stmt.setString(1, hashedPin);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
