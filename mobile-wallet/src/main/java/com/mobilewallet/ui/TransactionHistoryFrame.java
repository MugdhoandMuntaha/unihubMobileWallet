package com.mobilewallet.ui;

import com.mobilewallet.config.AppConfig;
import com.mobilewallet.model.Transaction;
import com.mobilewallet.model.User;
import com.mobilewallet.service.TransactionService;
import com.mobilewallet.ui.components.TransactionListItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Transaction History screen
 */
public class TransactionHistoryFrame extends JFrame {
    
    private User currentUser;
    private TransactionService transactionService;
    private JPanel transactionsPanel;
    private JComboBox<String> filterCombo;
    
    public TransactionHistoryFrame(User user) {
        this.currentUser = user;
        this.transactionService = new TransactionService();
        initComponents();
        loadTransactions();
    }
    
    private void initComponents() {
        setTitle(AppConfig.APP_NAME + " - Transaction History");
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppConfig.BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(AppConfig.TEXT_PRIMARY);
        
        // Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterLabel.setForeground(AppConfig.TEXT_SECONDARY);
        
        String[] filters = {"All", "Send Money", "Cash Out", "Add Money", "Mobile Recharge"};
        filterCombo = new JComboBox<>(filters);
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterCombo.addActionListener(e -> loadTransactions());
        
        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(filterPanel, BorderLayout.EAST);
        
        // Transactions list
        transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        transactionsPanel.setBackground(Color.WHITE);
        transactionsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JScrollPane scrollPane = new JScrollPane(transactionsPanel);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppConfig.BORDER_COLOR));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void loadTransactions() {
        transactionsPanel.removeAll();
        
        List<Transaction> transactions = transactionService.getTransactionHistory(currentUser.getUserId(), 100);
        
        // Apply filter
        String filter = (String) filterCombo.getSelectedItem();
        if (!filter.equals("All")) {
            String filterType = filter.replace(" ", "_").toUpperCase();
            transactions.removeIf(t -> !t.getTransactionType().equals(filterType));
        }
        
        if (transactions.isEmpty()) {
            JLabel emptyLabel = new JLabel("No transactions found");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(AppConfig.TEXT_SECONDARY);
            emptyLabel.setBorder(new EmptyBorder(50, 20, 50, 20));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            transactionsPanel.add(emptyLabel);
        } else {
            for (Transaction transaction : transactions) {
                TransactionListItem item = new TransactionListItem(transaction, currentUser.getUserId());
                transactionsPanel.add(item);
            }
        }
        
        transactionsPanel.revalidate();
        transactionsPanel.repaint();
    }
}
