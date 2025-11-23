package com.mobilewallet;

import com.formdev.flatlaf.FlatLightLaf;
import com.mobilewallet.config.AppConfig;
import com.mobilewallet.ui.LoginFrame;

import javax.swing.*;

/**
 * Main application entry point
 */
public class App {

    public static void main(String[] args) {
        // Set FlatLaf Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            // Customize UI properties
            UIManager.put("Button.arc", AppConfig.BORDER_RADIUS);
            UIManager.put("Component.arc", AppConfig.BORDER_RADIUS);
            UIManager.put("TextComponent.arc", AppConfig.BORDER_RADIUS);
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("Component.innerFocusWidth", 0);

        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
            e.printStackTrace();
        }

        // Launch application
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
