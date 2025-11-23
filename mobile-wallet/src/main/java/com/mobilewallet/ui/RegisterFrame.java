package com.mobilewallet.ui;

import com.mobilewallet.config.AppConfig;
import com.mobilewallet.service.AuthService;
import com.mobilewallet.ui.components.ModernButton;
import com.mobilewallet.ui.components.ModernTextField;
import com.mobilewallet.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Registration screen
 */
public class RegisterFrame extends JFrame {
    
    private ModernTextField nameField;
    private ModernTextField phoneField;
    private ModernTextField emailField;
    private JPasswordField pinField;
    private JPasswordField confirmPinField;
    private AuthService authService;
    private JFrame loginFrame;
    
    public RegisterFrame(JFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.authService = new AuthService();
        initComponents();
    }
    
    private void initComponents() {
        setTitle(AppConfig.APP_NAME + " - Register");
        setSize(450, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(AppConfig.BACKGROUND_COLOR);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(AppConfig.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(AppConfig.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Join us today!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(AppConfig.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Full Name
        addFormField(contentPanel, "Full Name", nameField = new ModernTextField("Enter your full name"));
        contentPanel.add(Box.createVerticalStrut(16));
        
        // Phone Number
        addFormField(contentPanel, "Phone Number", phoneField = new ModernTextField("01XXXXXXXXX"));
        contentPanel.add(Box.createVerticalStrut(16));
        
        // Email
        addFormField(contentPanel, "Email", emailField = new ModernTextField("your@email.com"));
        contentPanel.add(Box.createVerticalStrut(16));
        
        // PIN
        JLabel pinLabel = new JLabel("PIN (4-6 digits)");
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
        
        contentPanel.add(pinLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(pinField);
        contentPanel.add(Box.createVerticalStrut(16));
        
        // Confirm PIN
        JLabel confirmPinLabel = new JLabel("Confirm PIN");
        confirmPinLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        confirmPinLabel.setForeground(AppConfig.TEXT_PRIMARY);
        confirmPinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        confirmPinField = new JPasswordField();
        confirmPinField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppConfig.BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        confirmPinField.setMaximumSize(new Dimension(350, 45));
        confirmPinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(confirmPinLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(confirmPinField);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Register button
        ModernButton registerButton = new ModernButton("Register");
        registerButton.setMaximumSize(new Dimension(350, 45));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> handleRegister());
        
        contentPanel.add(registerButton);
        contentPanel.add(Box.createVerticalStrut(16));
        
        // Back to login link
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setOpaque(false);
        loginPanel.setMaximumSize(new Dimension(350, 30));
        
        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loginLabel.setForeground(AppConfig.TEXT_SECONDARY);
        
        JLabel loginLink = new JLabel("Login");
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginLink.setForeground(AppConfig.PRIMARY_COLOR);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backToLogin();
            }
        });
        
        loginPanel.add(loginLabel);
        loginPanel.add(loginLink);
        
        contentPanel.add(loginPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
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
    
    private void handleRegister() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String pin = new String(pinField.getPassword());
        String confirmPin = new String(confirmPinField.getPassword());
        
        // Validation
        if (!ValidationUtil.isValidName(name)) {
            showError("Please enter your full name");
            return;
        }
        
        if (!ValidationUtil.isValidPhone(phone)) {
            showError("Please enter a valid phone number (e.g., 01712345678)");
            return;
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            showError("Please enter a valid email address");
            return;
        }
        
        if (!ValidationUtil.isValidPin(pin)) {
            showError("PIN must be 4-6 digits");
            return;
        }
        
        if (!pin.equals(confirmPin)) {
            showError("PINs do not match");
            return;
        }
        
        // Register
        boolean success = authService.register(name, phone, email, pin);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Registration successful! Please login.", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            backToLogin();
        } else {
            showError("Registration failed. Phone number may already be registered.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void backToLogin() {
        loginFrame.setVisible(true);
        dispose();
    }
}
