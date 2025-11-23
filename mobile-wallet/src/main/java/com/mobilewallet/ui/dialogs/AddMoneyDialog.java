package com.mobilewallet.ui.dialogs;

import com.mobilewallet.config.AppConfig;
import com.mobilewallet.model.User;
import com.mobilewallet.service.TransactionService;
import com.mobilewallet.ui.components.ModernButton;
import com.mobilewallet.ui.components.ModernTextField;
import com.mobilewallet.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Add Money dialog
 */
public class AddMoneyDialog extends JDialog {
    
    private User currentUser;
    private TransactionService transactionService;
    private ModernTextField amountField;
    private JComboBox<String> sourceCombo;
    
    public AddMoneyDialog(JFrame parent, User user) {
        super(parent, "Add Money", true);
        this.currentUser = user;
        this.transactionService = new TransactionService();
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Add Money");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(AppConfig.SUCCESS_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Source selection
        JLabel sourceLabel = new JLabel("Source");
        sourceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sourceLabel.setForeground(AppConfig.TEXT_PRIMARY);
        sourceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[] sources = {"Bank Account", "Debit Card", "Credit Card"};
        sourceCombo = new JComboBox<>(sources);
        sourceCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sourceCombo.setMaximumSize(new Dimension(350, 45));
        sourceCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(sourceLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(sourceCombo);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Amount
        addFormField(mainPanel, "Amount", amountField = new ModernTextField("0.00"));
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Info
        JLabel infoLabel = new JLabel("No fees for adding money");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(AppConfig.SUCCESS_COLOR);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(infoLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(350, 45));
        
        ModernButton addButton = new ModernButton("Add Money", AppConfig.SUCCESS_COLOR);
        addButton.setPreferredSize(new Dimension(160, 45));
        addButton.addActionListener(e -> handleAddMoney());
        
        ModernButton cancelButton = new ModernButton("Cancel", AppConfig.TEXT_SECONDARY);
        cancelButton.setPreferredSize(new Dimension(160, 45));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    
    private void addFormField(JPanel panel, String labelText, ModernTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(AppConfig.TEXT_PRIMARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        field.setMaximumSize(new Dimension(350, 45));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);
    }
    
    private void handleAddMoney() {
        String amountStr = amountField.getText().trim();
        String source = (String) sourceCombo.getSelectedItem();
        
        // Validation
        if (!ValidationUtil.isValidAmount(amountStr)) {
            showError("Please enter a valid amount");
            return;
        }
        
        double amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
            showError("Amount must be greater than 0");
            return;
        }
        
        // Confirm
        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Add %s from %s?", 
                ValidationUtil.formatAmount(amount),
                source),
            "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Simulate processing
        JOptionPane.showMessageDialog(this, 
            "Processing...", 
            "Please Wait", JOptionPane.INFORMATION_MESSAGE);
        
        // Add money
        boolean success = transactionService.addMoney(currentUser.getUserId(), amount);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Money added successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            showError("Transaction failed. Please try again.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
