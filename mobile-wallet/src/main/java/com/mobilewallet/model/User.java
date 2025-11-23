package com.mobilewallet.model;

import java.time.LocalDateTime;

/**
 * User model class
 */
public class User {
    private int userId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String pinHash;
    private double balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
    
    public User() {}
    
    public User(int userId, String fullName, String phoneNumber, String email, 
                double balance, LocalDateTime createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.balance = balance;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPinHash() {
        return pinHash;
    }
    
    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
}
