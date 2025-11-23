package com.mobilewallet.ui.dialogs;

import com.mobilewallet.config.AppConfig;
import com.mobilewallet.model.User;
import com.mobilewallet.service.AuthService;
import com.mobilewallet.service.TransactionService;
import com.mobilewallet.ui.components.ModernButton;
import com.mobilewallet.ui.components.ModernTextField;
import com.mobilewallet.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Mobile Recharge dialog
 */
public class MobileRechargeDialog extends JDialog {
    
    private User currentUser;
    private TransactionService transactionService;
    private AuthService authService;
    private ModernTextField phoneField;
    private JComboBox<String> operatorCombo;
    private JComboBox<String> amountCombo;
    private ModernTextField customAmountField;
    private JPasswordField pinField;
    
    public MobileRechargeDialog(JFrame parent, User user) {
        super(parent, "Mobile Recharge", true);
        this.currentUser = user;
        this.transactionService = new TransactionService();
        this.authService = new AuthService();
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 550);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Mobile Recharge");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(AppConfig.INFO_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Phone Number
        addFormField(mainPanel, "Phone Number", phoneField = new ModernTextField("01XXXXXXXXX"));
        phoneField.setText(currentUser.getPhoneNumber()); // Default to own number
        mainPanel.add(Box.createVerticalStrut(16));
        
        // Operator
        JLabel operatorLabel = new JLabel("Operator");
        operatorLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        operatorLabel.setForeground(AppConfig.TEXT_PRIMARY);
        operatorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[] operators = {"Grameenphone", "Robi", "Banglalink", "Airtel", "Teletalk"};
        operatorCombo = new JComboBox<>(operators);
        operatorCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        operatorCombo.setMaximumSize(new Dimension(350, 45));
        operatorCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(operatorLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(operatorCombo);
        mainPanel.add(Box.createVerticalStrut(16));
        
        // Amount
        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        amountLabel.setForeground(AppConfig.TEXT_PRIMARY);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[] amounts = {"৳ 20", "৳ 50", "৳ 100", "৳ 200", "৳ 500", "Custom"};
        amountCombo = new JComboBox<>(amounts);
        amountCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amountCombo.setMaximumSize(new Dimension(350, 45));
        amountCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountCombo.addActionListener(e -> toggleCustomAmount());
        
        mainPanel.add(amountLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(amountCombo);
        mainPanel.add(Box.createVerticalStrut(16));
        
        // Custom Amount (hidden by default)
        customAmountField = new ModernTextField("Enter custom amount");
        customAmountField.setMaximumSize(new Dimension(350, 45));
        customAmountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        customAmountField.setVisible(false);
        
        mainPanel.add(customAmountField);
        mainPanel.add(Box.createVerticalStrut(16));
        
        // PIN
        JLabel pinLabel = new JLabel("Enter PIN");
        pinLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pinLabel.setForeground(AppConfig.TEXT_PRIMARY);
        pinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pinField = new JPasswordField();
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppConfig.BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        pinField.setMaximumSize(new Dimension(350, 45));
        pinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(pinLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(pinField);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(350, 45));
        
        ModernButton rechargeButton = new ModernButton("Recharge", AppConfig.INFO_COLOR);
        rechargeButton.setPreferredSize(new Dimension(160, 45));
        rechargeButton.addActionListener(e -> handleRecharge());
        
        ModernButton cancelButton = new ModernButton("Cancel", AppConfig.TEXT_SECONDARY);
        cancelButton.setPreferredSize(new Dimension(160, 45));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(rechargeButton);
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
    
    private void toggleCustomAmount() {
        boolean isCustom = amountCombo.getSelectedItem().equals("Custom");
        customAmountField.setVisible(isCustom);
        revalidate();
        repaint();
    }
    
    private void handleRecharge() {
        String phone = phoneField.getText().trim();
        String operator = (String) operatorCombo.getSelectedItem();
        String amountSelection = (String) amountCombo.getSelectedItem();
        String pin = new String(pinField.getPassword());
        
        // Validation
        if (!ValidationUtil.isValidPhone(phone)) {
            showError("Please enter a valid phone number");
            return;
        }
        
        // Get amount
        double amount;
        if (amountSelection.equals("Custom")) {
            String customAmount = customAmountField.getText().trim();
            if (!ValidationUtil.isValidAmount(customAmount)) {
                showError("Please enter a valid custom amount");
                return;
            }
            amount = Double.parseDouble(customAmount);
        } else {
            amount = Double.parseDouble(amountSelection.replace("৳ ", ""));
        }
        
        if (amount <= 0) {
            showError("Amount must be greater than 0");
            return;
        }
        
        if (!ValidationUtil.isValidPin(pin)) {
            showError("Please enter your PIN");
            return;
        }
        
        // Verify PIN
        if (!authService.verifyPin(currentUser.getUserId(), pin)) {
            showError("Incorrect PIN");
            return;
        }
        
        // Confirm
        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Recharge %s to %s (%s)?", 
                ValidationUtil.formatAmount(amount),
                phone,
                operator),
            "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Recharge
        boolean success = transactionService.mobileRecharge(currentUser.getUserId(), phone, amount);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Recharge successful!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            showError("Transaction failed. Please check your balance.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
