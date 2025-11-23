package com.mobilewallet.util;

import com.mobilewallet.config.AppConfig;
import java.util.regex.Pattern;

/**
 * Validation utility for user inputs
 */
public class ValidationUtil {
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(AppConfig.PHONE_REGEX);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(AppConfig.EMAIL_REGEX);
    
    /**
     * Validate phone number format (Bangladesh)
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate PIN format
     */
    public static boolean isValidPin(String pin) {
        if (pin == null || pin.trim().isEmpty()) {
            return false;
        }
        int length = pin.trim().length();
        return length >= AppConfig.MIN_PIN_LENGTH && 
               length <= AppConfig.MAX_PIN_LENGTH &&
               pin.matches("\\d+");
    }
    
    /**
     * Validate amount
     */
    public static boolean isValidAmount(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return false;
        }
        try {
            double value = Double.parseDouble(amount.trim());
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate name
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() >= 2;
    }
    
    /**
     * Format phone number for display
     */
    public static String formatPhoneNumber(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 4) + "-" + 
               phone.substring(4, 7) + "-" + 
               phone.substring(7);
    }
    
    /**
     * Format amount for display
     */
    public static String formatAmount(double amount) {
        return String.format("৳ %.2f", amount);
    }
}
