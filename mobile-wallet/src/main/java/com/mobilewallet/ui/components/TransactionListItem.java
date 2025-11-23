package com.mobilewallet.ui.components;

import com.mobilewallet.config.AppConfig;
import com.mobilewallet.model.Transaction;
import com.mobilewallet.util.DateUtil;
import com.mobilewallet.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Transaction list item component
 */
public class TransactionListItem extends JPanel {
    
    public TransactionListItem(Transaction transaction, int currentUserId) {
        setLayout(new BorderLayout(12, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppConfig.BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        // Determine if this is credit or debit
        boolean isCredit = transaction.getReceiverPhone() != null && 
                          isCurrentUserReceiver(transaction, currentUserId);
        
        // Icon
        JLabel iconLabel = new JLabel(getTransactionIcon(transaction.getTransactionType()));
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(getTransactionColor(transaction.getTransactionType()));
        iconLabel.setPreferredSize(new Dimension(40, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Details panel
        JPanel detailsPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        detailsPanel.setOpaque(false);
        
        String displayName = getDisplayName(transaction, currentUserId);
        JLabel nameLabel = new JLabel(displayName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(AppConfig.TEXT_PRIMARY);
        
        JLabel timeLabel = new JLabel(DateUtil.getRelativeTime(transaction.getCreatedAt()));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(AppConfig.TEXT_SECONDARY);
        
        detailsPanel.add(nameLabel);
        detailsPanel.add(timeLabel);
        
        // Amount panel
        JPanel amountPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        amountPanel.setOpaque(false);
        
        String amountText = (isCredit ? "+ " : "- ") + ValidationUtil.formatAmount(transaction.getAmount());
        JLabel amountLabel = new JLabel(amountText, SwingConstants.RIGHT);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        amountLabel.setForeground(isCredit ? AppConfig.SUCCESS_COLOR : AppConfig.DANGER_COLOR);
        
        JLabel statusLabel = new JLabel(transaction.getStatus(), SwingConstants.RIGHT);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(AppConfig.TEXT_SECONDARY);
        
        amountPanel.add(amountLabel);
        amountPanel.add(statusLabel);
        
        add(iconLabel, BorderLayout.WEST);
        add(detailsPanel, BorderLayout.CENTER);
        add(amountPanel, BorderLayout.EAST);
    }
    
    private boolean isCurrentUserReceiver(Transaction transaction, int currentUserId) {
        // Simple check - in real implementation, compare receiver ID
        return transaction.getTransactionType().equals("ADD_MONEY") || 
               transaction.getTransactionType().equals("SEND_MONEY");
    }
    
    private String getDisplayName(Transaction transaction, int currentUserId) {
        String type = transaction.getTransactionType();
        
        switch (type) {
            case "SEND_MONEY":
                return transaction.getReceiverName() != null ? 
                       transaction.getReceiverName() : "Send Money";
            case "CASH_OUT":
                return "Cash Out";
            case "ADD_MONEY":
                return "Add Money";
            case "MOBILE_RECHARGE":
                return "Mobile Recharge";
            case "PAYMENT":
                return "Payment";
            default:
                return type;
        }
    }
    
    private String getTransactionIcon(String type) {
        switch (type) {
            case "SEND_MONEY": return "↑";
            case "CASH_OUT": return "💵";
            case "ADD_MONEY": return "↓";
            case "MOBILE_RECHARGE": return "📱";
            case "PAYMENT": return "💳";
            default: return "•";
        }
    }
    
    private Color getTransactionColor(String type) {
        switch (type) {
            case "SEND_MONEY": return AppConfig.PRIMARY_COLOR;
            case "CASH_OUT": return AppConfig.WARNING_COLOR;
            case "ADD_MONEY": return AppConfig.SUCCESS_COLOR;
            case "MOBILE_RECHARGE": return AppConfig.INFO_COLOR;
            case "PAYMENT": return AppConfig.SECONDARY_COLOR;
            default: return AppConfig.TEXT_SECONDARY;
        }
    }
}
