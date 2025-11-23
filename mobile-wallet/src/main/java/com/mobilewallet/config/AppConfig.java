package com.mobilewallet.config;

import java.awt.Color;

/**
 * Application configuration and constants
 */
public class AppConfig {
    
    // Database Configuration
    public static final String DB_URL = "jdbc:mysql://localhost:3306/mobile_wallet_db";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "root";
    
    // Application Info
    public static final String APP_NAME = "Mobile Wallet";
    public static final String APP_VERSION = "1.0.0";
    
    // Color Scheme - Modern Purple/Blue Gradient Theme
    public static final Color PRIMARY_COLOR = new Color(124, 58, 237); // Purple
    public static final Color PRIMARY_DARK = new Color(91, 33, 182);
    public static final Color PRIMARY_LIGHT = new Color(167, 139, 250);
    
    public static final Color SECONDARY_COLOR = new Color(59, 130, 246); // Blue
    public static final Color SECONDARY_DARK = new Color(29, 78, 216);
    public static final Color SECONDARY_LIGHT = new Color(147, 197, 253);
    
    public static final Color SUCCESS_COLOR = new Color(34, 197, 94); // Green
    public static final Color DANGER_COLOR = new Color(239, 68, 68); // Red
    public static final Color WARNING_COLOR = new Color(251, 146, 60); // Orange
    public static final Color INFO_COLOR = new Color(14, 165, 233); // Light Blue
    
    public static final Color BACKGROUND_COLOR = new Color(248, 250, 252);
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    public static final Color BORDER_COLOR = new Color(226, 232, 240);
    
    // UI Constants
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;
    public static final int BORDER_RADIUS = 12;
    public static final int COMPONENT_PADDING = 16;
    
    // Transaction Fees
    public static final double CASH_OUT_FEE_PERCENT = 1.85; // 1.85%
    public static final double SEND_MONEY_FEE_THRESHOLD = 1000.00;
    public static final double SEND_MONEY_FEE = 5.00;
    
    // Validation Constants
    public static final int MIN_PIN_LENGTH = 4;
    public static final int MAX_PIN_LENGTH = 6;
    public static final String PHONE_REGEX = "^01[3-9]\\d{8}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
}
