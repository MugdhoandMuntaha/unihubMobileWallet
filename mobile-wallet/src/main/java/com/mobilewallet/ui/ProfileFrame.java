package com.mobilewallet.ui;

import com.mobilewallet.config.AppConfig;
import com.mobilewallet.model.User;
import com.mobilewallet.service.AuthService;
import com.mobilewallet.service.UserService;
import com.mobilewallet.ui.components.ModernButton;
import com.mobilewallet.ui.components.ModernTextField;
import com.mobilewallet.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * User Profile screen
 */
public class ProfileFrame extends JFrame {

    private User currentUser;
    private UserService userService;
    private AuthService authService;
    private JFrame dashboardFrame;
    private ModernTextField nameField;
    private ModernTextField phoneField;
    private ModernTextField emailField;
    private JPasswordField oldPinField;
    private JPasswordField newPinField;
    private JPasswordField confirmPinField;

    public ProfileFrame(User user, JFrame dashboardFrame) {
        this.currentUser = user;
        this.dashboardFrame = dashboardFrame;
        this.userService = new UserService();
        this.authService = new AuthService();
        initComponents();
    }

    private void initComponents() {
        setTitle(AppConfig.APP_NAME + " - Profile");
        setSize(500, 700);
        setLocationRelativeTo(dashboardFrame);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppConfig.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(AppConfig.TEXT_PRIMARY);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(AppConfig.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Profile Info Section
        JLabel infoSectionLabel = new JLabel("Profile Information");
        infoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoSectionLabel.setForeground(AppConfig.TEXT_PRIMARY);
        infoSectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(infoSectionLabel);
        contentPanel.add(Box.createVerticalStrut(16));

        // Full Name
        addFormField(contentPanel, "Full Name", nameField = new ModernTextField());
        nameField.setText(currentUser.getFullName());
        contentPanel.add(Box.createVerticalStrut(12));

        // Phone Number (read-only)
        addFormField(contentPanel, "Phone Number", phoneField = new ModernTextField());
        phoneField.setText(currentUser.getPhoneNumber());
        phoneField.setEnabled(false);
        contentPanel.add(Box.createVerticalStrut(12));

        // Email
        addFormField(contentPanel, "Email", emailField = new ModernTextField());
        emailField.setText(currentUser.getEmail());
        contentPanel.add(Box.createVerticalStrut(20));

        // Update Profile Button
        ModernButton updateProfileButton = new ModernButton("Update Profile");
        updateProfileButton.setMaximumSize(new Dimension(400, 45));
        updateProfileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateProfileButton.addActionListener(e -> handleUpdateProfile());

        contentPanel.add(updateProfileButton);
        contentPanel.add(Box.createVerticalStrut(30));

        // Divider
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(30));

        // Change PIN Section
        JLabel pinSectionLabel = new JLabel("Change PIN");
        pinSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pinSectionLabel.setForeground(AppConfig.TEXT_PRIMARY);
        pinSectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(pinSectionLabel);
        contentPanel.add(Box.createVerticalStrut(16));

        // Old PIN
        addPasswordField(contentPanel, "Current PIN", oldPinField = new JPasswordField());
        contentPanel.add(Box.createVerticalStrut(12));

        // New PIN
        addPasswordField(contentPanel, "New PIN", newPinField = new JPasswordField());
        contentPanel.add(Box.createVerticalStrut(12));

        // Confirm New PIN
        addPasswordField(contentPanel, "Confirm New PIN", confirmPinField = new JPasswordField());
        contentPanel.add(Box.createVerticalStrut(20));

        // Change PIN Button
        ModernButton changePinButton = new ModernButton("Change PIN", AppConfig.WARNING_COLOR);
        changePinButton.setMaximumSize(new Dimension(400, 45));
        changePinButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        changePinButton.addActionListener(e -> handleChangePin());

        contentPanel.add(changePinButton);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void addFormField(JPanel panel, String labelText, ModernTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(AppConfig.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(400, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);
    }

    private void addPasswordField(JPanel panel, String labelText, JPasswordField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(AppConfig.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppConfig.BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        field.setMaximumSize(new Dimension(400, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);
    }

    private void handleUpdateProfile() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        // Validation
        if (!ValidationUtil.isValidName(name)) {
            showError("Please enter a valid name");
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            showError("Please enter a valid email");
            return;
        }

        // Update
        boolean success = userService.updateProfile(currentUser.getUserId(), name, email);

        if (success) {
            currentUser.setFullName(name);
            currentUser.setEmail(email);
            JOptionPane.showMessageDialog(this,
                    "Profile updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showError("Failed to update profile");
        }
    }

    private void handleChangePin() {
        String oldPin = new String(oldPinField.getPassword());
        String newPin = new String(newPinField.getPassword());
        String confirmPin = new String(confirmPinField.getPassword());

        // Validation
        if (!ValidationUtil.isValidPin(oldPin)) {
            showError("Please enter your current PIN");
            return;
        }

        if (!ValidationUtil.isValidPin(newPin)) {
            showError("New PIN must be 4-6 digits");
            return;
        }

        if (!newPin.equals(confirmPin)) {
            showError("New PINs do not match");
            return;
        }

        // Change PIN
        boolean success = authService.changePin(currentUser.getUserId(), oldPin, newPin);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "PIN changed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            oldPinField.setText("");
            newPinField.setText("");
            confirmPinField.setText("");
        } else {
            showError("Failed to change PIN. Current PIN may be incorrect.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
