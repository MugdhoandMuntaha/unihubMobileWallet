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
 * Cash Out dialog
 */
public class CashOutDialog extends JDialog {
    
    private User currentUser;
    private TransactionService transactionService;
    private AuthService authService;
    private ModernTextField agentPhoneField;
    private ModernTextField amountField;
    private JPasswordField pinField;
    private JLabel feeLabel;
    
    public CashOutDialog(JFrame parent, User user) {
        super(parent, "Cash Out", true);
        this.currentUser = user;
        this.transactionService = new TransactionService();
        this.authService = new AuthService();
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 480);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Cash Out");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(AppConfig.WARNING_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Agent Phone
        addFormField(mainPanel, "Agent Phone Number", agentPhoneField = new ModernTextField("01XXXXXXXXX"));
        mainPanel.add(Box.createVerticalStrut(16));
        
        // Amount
        addFormField(mainPanel, "Amount", amountField = new ModernTextField("0.00"));
        amountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateFee();
            }
        });
        mainPanel.add(Box.createVerticalStrut(16));
        
        // Fee display
        feeLabel = new JLabel("Fee: ৳ 0.00");
        feeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        feeLabel.setForeground(AppConfig.TEXT_SECONDARY);
        feeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(feeLabel);
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
        
        ModernButton cashOutButton = new ModernButton("Cash Out", AppConfig.WARNING_COLOR);
        cashOutButton.setPreferredSize(new Dimension(160, 45));
        cashOutButton.addActionListener(e -> handleCashOut());
        
        ModernButton cancelButton = new ModernButton("Cancel", AppConfig.TEXT_SECONDARY);
        cancelButton.setPreferredSize(new Dimension(160, 45));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(cashOutButton);
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
    
    private void updateFee() {
        String amountStr = amountField.getText().trim();
        if (ValidationUtil.isValidAmount(amountStr)) {
            double amount = Double.parseDouble(amountStr);
            double fee = amount * AppConfig.CASH_OUT_FEE_PERCENT / 100;
            feeLabel.setText("Fee: " + ValidationUtil.formatAmount(fee));
        } else {
            feeLabel.setText("Fee: ৳ 0.00");
        }
    }
    
    private void handleCashOut() {
        String agentPhone = agentPhoneField.getText().trim();
        String amountStr = amountField.getText().trim();
        String pin = new String(pinField.getPassword());
        
        // Validation
        if (!ValidationUtil.isValidPhone(agentPhone)) {
            showError("Please enter a valid agent phone number");
            return;
        }
        
        if (!ValidationUtil.isValidAmount(amountStr)) {
            showError("Please enter a valid amount");
            return;
        }
        
        double amount = Double.parseDouble(amountStr);
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
        
        // Calculate fee
        double fee = amount * AppConfig.CASH_OUT_FEE_PERCENT / 100;
        
        // Confirm
        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Cash out %s from agent %s?\nFee: %s\nTotal deducted: %s", 
                ValidationUtil.formatAmount(amount),
                agentPhone,
                ValidationUtil.formatAmount(fee),
                ValidationUtil.formatAmount(amount + fee)),
            "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Cash out
        boolean success = transactionService.cashOut(currentUser.getUserId(), agentPhone, amount, fee);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Cash out successful!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            showError("Transaction failed. Please check agent number and your balance.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
