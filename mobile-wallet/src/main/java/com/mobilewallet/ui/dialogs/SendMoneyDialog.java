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
 * Send Money dialog
 */
public class SendMoneyDialog extends JDialog {
    
    private User currentUser;
    private TransactionService transactionService;
    private AuthService authService;
    private ModernTextField phoneField;
    private ModernTextField amountField;
    private ModernTextField noteField;
    private JPasswordField pinField;
    
    public SendMoneyDialog(JFrame parent, User user) {
        super(parent, "Send Money", true);
        this.currentUser = user;
        this.transactionService = new TransactionService();
        this.authService = new AuthService();
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Send Money");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(AppConfig.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Recipient Phone
        addFormField(mainPanel, "Recipient Phone Number", phoneField = new ModernTextField("01XXXXXXXXX"));
        mainPanel.add(Box.createVerticalStrut(16));
        
        // Amount
        addFormField(mainPanel, "Amount", amountField = new ModernTextField("0.00"));
        mainPanel.add(Box.createVerticalStrut(16));
        
        // Note
        addFormField(mainPanel, "Note (Optional)", noteField = new ModernTextField("Add a note"));
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
        
        ModernButton sendButton = new ModernButton("Send Money");
        sendButton.setPreferredSize(new Dimension(160, 45));
        sendButton.addActionListener(e -> handleSendMoney());
        
        ModernButton cancelButton = new ModernButton("Cancel", AppConfig.TEXT_SECONDARY);
        cancelButton.setPreferredSize(new Dimension(160, 45));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(sendButton);
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
    
    private void handleSendMoney() {
        String phone = phoneField.getText().trim();
        String amountStr = amountField.getText().trim();
        String note = noteField.getText().trim();
        String pin = new String(pinField.getPassword());
        
        // Validation
        if (!ValidationUtil.isValidPhone(phone)) {
            showError("Please enter a valid phone number");
            return;
        }
        
        if (phone.equals(currentUser.getPhoneNumber())) {
            showError("You cannot send money to yourself");
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
        double fee = amount > AppConfig.SEND_MONEY_FEE_THRESHOLD ? AppConfig.SEND_MONEY_FEE : 0;
        
        // Confirm
        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Send %s to %s?\nFee: %s\nTotal: %s", 
                ValidationUtil.formatAmount(amount),
                phone,
                ValidationUtil.formatAmount(fee),
                ValidationUtil.formatAmount(amount + fee)),
            "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Send money
        boolean success = transactionService.sendMoney(currentUser.getUserId(), phone, amount, note, fee);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Money sent successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            showError("Transaction failed. Please check recipient number and your balance.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
