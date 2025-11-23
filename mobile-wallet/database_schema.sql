-- Mobile Wallet Database Schema
-- Execute this script in MySQL Workbench to set up the database

-- Create database
CREATE DATABASE IF NOT EXISTS mobile_wallet_db;
USE mobile_wallet_db;

-- Users table

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    pin_hash VARCHAR(255) NOT NULL,
    balance DECIMAL(15, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_phone (phone_number),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Transaction types lookup table
CREATE TABLE IF NOT EXISTS transaction_types (
    type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Transactions table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT,
    receiver_id INT,
    transaction_type_id INT NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    fee DECIMAL(15, 2) DEFAULT 0.00,
    reference_number VARCHAR(50) UNIQUE NOT NULL,
    note VARCHAR(255),
    status ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED') DEFAULT 'COMPLETED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (transaction_type_id) REFERENCES transaction_types(type_id),
    INDEX idx_sender (sender_id),
    INDEX idx_receiver (receiver_id),
    INDEX idx_created_at (created_at),
    INDEX idx_reference (reference_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert transaction types
INSERT INTO transaction_types (type_name, description) VALUES
('SEND_MONEY', 'Send money to another user'),
('CASH_OUT', 'Cash out from agent'),
('ADD_MONEY', 'Add money from bank/card'),
('MOBILE_RECHARGE', 'Mobile recharge'),
('PAYMENT', 'Bill payment or merchant payment'),
('REQUEST_MONEY', 'Request money from another user');

-- Insert sample users (PIN: 1234 for all - hashed with BCrypt)
-- BCrypt hash for "1234": $2a$10$N9qo8uLOickgx2ZMRZoMye1J8.qhKYcu3fJYuXKEYxr5JxRc3QJnm
INSERT INTO users (full_name, phone_number, email, pin_hash, balance) VALUES
('John Doe', '01712345678', 'john.doe@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8.qhKYcu3fJYuXKEYxr5JxRc3QJnm', 5000.00),
('Jane Smith', '01812345678', 'jane.smith@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8.qhKYcu3fJYuXKEYxr5JxRc3QJnm', 3000.00),
('Agent One', '01912345678', 'agent1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8.qhKYcu3fJYuXKEYxr5JxRc3QJnm', 50000.00);

-- Insert sample transactions
INSERT INTO transactions (sender_id, receiver_id, transaction_type_id, amount, fee, reference_number, note, status) VALUES
(1, 2, 1, 500.00, 0.00, 'TXN001', 'Lunch payment', 'COMPLETED'),
(1, NULL, 4, 100.00, 0.00, 'TXN002', 'Mobile recharge', 'COMPLETED'),
(NULL, 1, 3, 2000.00, 0.00, 'TXN003', 'Added from bank', 'COMPLETED');

-- Create view for transaction history with user details
CREATE OR REPLACE VIEW transaction_history_view AS
SELECT 
    t.transaction_id,
    t.reference_number,
    t.amount,
    t.fee,
    t.note,
    t.status,
    t.created_at,
    tt.type_name,
    tt.description as type_description,
    s.full_name as sender_name,
    s.phone_number as sender_phone,
    r.full_name as receiver_name,
    r.phone_number as receiver_phone
FROM transactions t
LEFT JOIN users s ON t.sender_id = s.user_id
LEFT JOIN users r ON t.receiver_id = r.user_id
JOIN transaction_types tt ON t.transaction_type_id = tt.type_id
ORDER BY t.created_at DESC;

-- Display success message
SELECT 'Database setup completed successfully!' as Status;
