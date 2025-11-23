package com.mobilewallet.model;

import java.time.LocalDateTime;

/**
 * Transaction model class
 */
public class Transaction {
    private int transactionId;
    private int senderId;
    private int receiverId;
    private int transactionTypeId;
    private String transactionType;
    private double amount;
    private double fee;
    private String referenceNumber;
    private String note;
    private String status;
    private LocalDateTime createdAt;
    
    // Additional fields for display
    private String senderName;
    private String senderPhone;
    private String receiverName;
    private String receiverPhone;
    
    public Transaction() {}
    
    public Transaction(int transactionId, String transactionType, double amount, 
                      String referenceNumber, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.referenceNumber = referenceNumber;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    
    public int getSenderId() {
        return senderId;
    }
    
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    
    public int getReceiverId() {
        return receiverId;
    }
    
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
    
    public int getTransactionTypeId() {
        return transactionTypeId;
    }
    
    public void setTransactionTypeId(int transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }
    
    public String getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public double getFee() {
        return fee;
    }
    
    public void setFee(double fee) {
        this.fee = fee;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public String getNote() {
        return note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    public String getSenderPhone() {
        return senderPhone;
    }
    
    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }
    
    public String getReceiverName() {
        return receiverName;
    }
    
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    
    public String getReceiverPhone() {
        return receiverPhone;
    }
    
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
}
