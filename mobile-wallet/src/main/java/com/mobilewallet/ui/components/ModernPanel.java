package com.mobilewallet.ui.components;

import com.mobilewallet.config.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern panel with rounded corners and optional shadow
 */
public class ModernPanel extends JPanel {
    
    private int borderRadius;
    private Color backgroundColor;
    private boolean hasShadow;
    
    public ModernPanel() {
        this(AppConfig.CARD_BACKGROUND, AppConfig.BORDER_RADIUS, false);
    }
    
    public ModernPanel(Color backgroundColor) {
        this(backgroundColor, AppConfig.BORDER_RADIUS, false);
    }
    
    public ModernPanel(Color backgroundColor, int borderRadius, boolean hasShadow) {
        this.backgroundColor = backgroundColor;
        this.borderRadius = borderRadius;
        this.hasShadow = hasShadow;
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Shadow
        if (hasShadow) {
            g2.setColor(new Color(0, 0, 0, 20));
            for (int i = 0; i < 5; i++) {
                g2.fill(new RoundRectangle2D.Float(i, i, getWidth() - i * 2, getHeight() - i * 2, 
                                                   borderRadius, borderRadius));
            }
        }
        
        // Background
        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                                           borderRadius, borderRadius));
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
    
    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
        repaint();
    }
}
