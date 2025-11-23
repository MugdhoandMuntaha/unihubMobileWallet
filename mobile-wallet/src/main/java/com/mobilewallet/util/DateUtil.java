package com.mobilewallet.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Date and time formatting utilities
 */
public class DateUtil {
    
    private static final DateTimeFormatter DISPLAY_FORMAT = 
        DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
    
    private static final DateTimeFormatter SHORT_FORMAT = 
        DateTimeFormatter.ofPattern("dd MMM yyyy");
    
    private static final DateTimeFormatter TIME_FORMAT = 
        DateTimeFormatter.ofPattern("hh:mm a");
    
    /**
     * Format date time for display
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DISPLAY_FORMAT);
    }
    
    /**
     * Format date only
     */
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(SHORT_FORMAT);
    }
    
    /**
     * Format time only
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(TIME_FORMAT);
    }
    
    /**
     * Get relative time string (e.g., "2 hours ago")
     */
    public static String getRelativeTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        
        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " min" + (minutes > 1 ? "s" : "") + " ago";
        }
        
        long hours = ChronoUnit.HOURS.between(dateTime, now);
        if (hours < 24) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        }
        
        long days = ChronoUnit.DAYS.between(dateTime, now);
        if (days < 7) {
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        }
        
        return formatDate(dateTime);
    }
}
