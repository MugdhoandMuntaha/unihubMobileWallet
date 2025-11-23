package com.mobilewallet.ui.components;

import com.mobilewallet.config.AppConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern text field with rounded borders and focus effects
 */
public class ModernTextField extends JTextField {
    
    private String placeholder;
    private boolean isFocused = false;
    
    public ModernTextField() {
        this("");
    }
    
    public ModernTextField(String placeholder) {
        this.placeholder = placeholder;
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(AppConfig.TEXT_PRIMARY);
        setBorder(new EmptyBorder(12, 16, 12, 16));
        setOpaque(false);
        
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                isFocused = true;
                repaint();
            }
            
            public void focusLost(java.awt.event.FocusEvent evt) {
                isFocused = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                                           AppConfig.BORDER_RADIUS, AppConfig.BORDER_RADIUS));
        
        // Border
        if (isFocused) {
            g2.setColor(AppConfig.PRIMARY_COLOR);
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setColor(AppConfig.BORDER_COLOR);
            g2.setStroke(new BasicStroke(1));
        }
        g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 
                                           AppConfig.BORDER_RADIUS, AppConfig.BORDER_RADIUS));
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        // Border is painted in paintComponent
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Draw placeholder
        if (getText().isEmpty() && !isFocused && placeholder != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(AppConfig.TEXT_SECONDARY);
            g2.setFont(getFont());
            
            Insets insets = getInsets();
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, insets.left, y);
        }
    }
    
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }
}
