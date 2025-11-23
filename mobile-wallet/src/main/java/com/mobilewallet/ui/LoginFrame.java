package com.mobilewallet.ui;

import com.mobilewallet.config.AppConfig;
import com.mobilewallet.model.User;
import com.mobilewallet.service.AuthService;
import com.mobilewallet.ui.components.ModernButton;
import com.mobilewallet.ui.components.ModernTextField;
import com.mobilewallet.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen
 */
public class LoginFrame extends JFrame {
    
    private ModernTextField phoneField;
    private JPasswordField pinField;
    private AuthService authService;
    
    public LoginFrame() {
        this.authService = new AuthService();
        initComponents();
    }
    
    private void initComponents() {
        setTitle(AppConfig.APP_NAME + " - Login");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(AppConfig.BACKGROUND_COLOR);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(AppConfig.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 40, 40));
        
        // Logo/Title
        JLabel titleLabel = new JLabel(AppConfig.APP_NAME);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(AppConfig.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Welcome Back!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(AppConfig.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(50));
        
        // Phone number field
        JLabel phoneLabel = new JLabel("Phone Number");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        phoneLabel.setForeground(AppConfig.TEXT_PRIMARY);
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        phoneField = new ModernTextField("01XXXXXXXXX");
        phoneField.setMaximumSize(new Dimension(350, 45));
        phoneField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(phoneLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(phoneField);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // PIN field
        JLabel pinLabel = new JLabel("PIN");
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
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Login button
        ModernButton loginButton = new ModernButton("Login");
        loginButton.setMaximumSize(new Dimension(350, 45));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());
        
        contentPanel.add(loginButton);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Register link
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setOpaque(false);
        registerPanel.setMaximumSize(new Dimension(350, 30));
        
        JLabel registerLabel = new JLabel("Don't have an account?");
        registerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        registerLabel.setForeground(AppConfig.TEXT_SECONDARY);
        
        JLabel registerLink = new JLabel("Register");
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerLink.setForeground(AppConfig.PRIMARY_COLOR);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openRegisterFrame();
            }
        });
        
        registerPanel.add(registerLabel);
        registerPanel.add(registerLink);
        
        contentPanel.add(registerPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        // Enter key listener
        pinField.addActionListener(e -> handleLogin());
    }
    
    private void handleLogin() {
        String phone = phoneField.getText().trim();
        String pin = new String(pinField.getPassword());
        
        // Validation
        if (!ValidationUtil.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid phone number (e.g., 01712345678)", 
                "Invalid Phone", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!ValidationUtil.isValidPin(pin)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid PIN (4-6 digits)", 
                "Invalid PIN", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Authenticate
        User user = authService.login(phone, pin);
        
        if (user != null) {
            // Open dashboard
            SwingUtilities.invokeLater(() -> {
                new DashboardFrame(user).setVisible(true);
                dispose();
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid phone number or PIN", 
                "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openRegisterFrame() {
        new RegisterFrame(this).setVisible(true);
        setVisible(false);
    }
}
