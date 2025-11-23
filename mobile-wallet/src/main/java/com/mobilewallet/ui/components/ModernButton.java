package com.mobilewallet.ui.components;

import com.mobilewallet.config.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern styled button with rounded corners and hover effects
 */
public class ModernButton extends JButton {
    
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    
    public ModernButton(String text) {
        this(text, AppConfig.PRIMARY_COLOR);
    }
    
    public ModernButton(String text, Color backgroundColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.hoverColor = backgroundColor.darker();
        this.pressedColor = backgroundColor.darker().darker();
        
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color currentColor = backgroundColor;
        if (isPressed) {
            currentColor = pressedColor;
        } else if (isHovered) {
            currentColor = hoverColor;
        }
        
        g2.setColor(currentColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                                           AppConfig.BORDER_RADIUS, AppConfig.BORDER_RADIUS));
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        this.hoverColor = color.darker();
        this.pressedColor = color.darker().darker();
        repaint();
    }
}
