package com.mobilewallet.service;

import com.mobilewallet.model.Transaction;
import com.mobilewallet.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Transaction service for managing all transactions
 */
public class TransactionService {
    
    private Connection connection;
    private UserService userService;
    
    public TransactionService() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.userService = new UserService();
    }
    
    /**
     * Send money to another user
     */
    public boolean sendMoney(int senderId, String receiverPhone, double amount, String note, double fee) {
        try {
            connection.setAutoCommit(false);
            
            // Get receiver
            UserService userService = new UserService();
            var receiver = userService.getUserByPhone(receiverPhone);
            
            if (receiver == null) {
                connection.rollback();
                return false;
            }
            
            // Check sender balance
            double senderBalance = userService.getBalance(senderId);
            if (senderBalance < (amount + fee)) {
                connection.rollback();
                return false;
            }
            
            // Update balances
            userService.updateBalance(senderId, senderBalance - amount - fee);
            userService.updateBalance(receiver.getUserId(), receiver.getBalance() + amount);
            
            // Create transaction record
            String refNumber = generateReferenceNumber();
            createTransaction(senderId, receiver.getUserId(), 1, amount, fee, refNumber, note);
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Cash out from agent
     */
    public boolean cashOut(int userId, String agentPhone, double amount, double fee) {
        try {
            connection.setAutoCommit(false);
            
            // Get agent
            var agent = userService.getUserByPhone(agentPhone);
            if (agent == null) {
                connection.rollback();
                return false;
            }
            
            // Check user balance
            double userBalance = userService.getBalance(userId);
            if (userBalance < (amount + fee)) {
                connection.rollback();
                return false;
            }
            
            // Update balances
            userService.updateBalance(userId, userBalance - amount - fee);
            userService.updateBalance(agent.getUserId(), agent.getBalance() + amount);
            
            // Create transaction record
            String refNumber = generateReferenceNumber();
            createTransaction(userId, agent.getUserId(), 2, amount, fee, refNumber, "Cash Out");
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Add money to wallet
     */
    public boolean addMoney(int userId, double amount) {
        try {
            connection.setAutoCommit(false);
            
            // Update balance
            double currentBalance = userService.getBalance(userId);
            userService.updateBalance(userId, currentBalance + amount);
            
            // Create transaction record
            String refNumber = generateReferenceNumber();
            createTransaction(null, userId, 3, amount, 0, refNumber, "Added from bank");
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Mobile recharge
     */
    public boolean mobileRecharge(int userId, String phoneNumber, double amount) {
        try {
            connection.setAutoCommit(false);
            
            // Check balance
            double balance = userService.getBalance(userId);
            if (balance < amount) {
                connection.rollback();
                return false;
            }
            
            // Update balance
            userService.updateBalance(userId, balance - amount);
            
            // Create transaction record
            String refNumber = generateReferenceNumber();
            createTransaction(userId, null, 4, amount, 0, refNumber, "Recharge: " + phoneNumber);
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Create transaction record
     */
    private void createTransaction(Integer senderId, Integer receiverId, int typeId, 
                                   double amount, double fee, String refNumber, String note) {
        String query = "INSERT INTO transactions (sender_id, receiver_id, transaction_type_id, " +
                      "amount, fee, reference_number, note, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'COMPLETED')";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            if (senderId != null) {
                stmt.setInt(1, senderId);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            
            if (receiverId != null) {
                stmt.setInt(2, receiverId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            stmt.setInt(3, typeId);
            stmt.setDouble(4, amount);
            stmt.setDouble(5, fee);
            stmt.setString(6, refNumber);
            stmt.setString(7, note);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get transaction history for user
     */
    public List<Transaction> getTransactionHistory(int userId, int limit) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transaction_history_view " +
                      "WHERE sender_id = ? OR receiver_id = ? " +
                      "ORDER BY created_at DESC LIMIT ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, limit);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setReferenceNumber(rs.getString("reference_number"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setFee(rs.getDouble("fee"));
                transaction.setNote(rs.getString("note"));
                transaction.setStatus(rs.getString("status"));
                transaction.setTransactionType(rs.getString("type_name"));
                transaction.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                transaction.setSenderName(rs.getString("sender_name"));
                transaction.setSenderPhone(rs.getString("sender_phone"));
                transaction.setReceiverName(rs.getString("receiver_name"));
                transaction.setReceiverPhone(rs.getString("receiver_phone"));
                
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return transactions;
    }
    
    /**
     * Generate unique reference number
     */
    private String generateReferenceNumber() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}
