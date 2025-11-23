package com.mobilewallet.ui.components;

import com.mobilewallet.config.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Quick action button for dashboard
 */
public class QuickActionButton extends JPanel {
    
    private Color iconColor;
    private String iconText;
    private String labelText;
    private boolean isHovered = false;
    
    public QuickActionButton(String iconText, String labelText, Color iconColor) {
        this.iconText = iconText;
        this.labelText = labelText;
        this.iconColor = iconColor;
        
        setLayout(new BorderLayout(0, 8));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(120, 120));
        
        // Icon panel
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = isHovered ? iconColor : new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), 30);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                
                g2.setColor(isHovered ? Color.WHITE : iconColor);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(iconText)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(iconText, x, y);
                
                g2.dispose();
            }
        };
        iconPanel.setPreferredSize(new Dimension(70, 70));
        iconPanel.setOpaque(false);
        
        // Label
        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(AppConfig.TEXT_PRIMARY);
        
        // Center icon
        JPanel iconWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconWrapper.setOpaque(false);
        iconWrapper.add(iconPanel);
        
        add(iconWrapper, BorderLayout.CENTER);
        add(label, BorderLayout.SOUTH);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }
}
