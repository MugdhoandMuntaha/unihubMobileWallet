package com.mobilewallet.ui;

import com.mobilewallet.config.AppConfig;
import com.mobilewallet.model.Transaction;
import com.mobilewallet.model.User;
import com.mobilewallet.service.TransactionService;
import com.mobilewallet.service.UserService;
import com.mobilewallet.ui.components.*;
import com.mobilewallet.ui.dialogs.*;
import com.mobilewallet.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Main Dashboard - The centerpiece of the application
 */
public class DashboardFrame extends JFrame {
    
    private User currentUser;
    private UserService userService;
    private TransactionService transactionService;
    private JLabel balanceLabel;
    private JPanel transactionsPanel;
    private boolean balanceVisible = true;
    
    public DashboardFrame(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        this.transactionService = new TransactionService();
        initComponents();
        loadTransactions();
    }
    
    private void initComponents() {
        setTitle(AppConfig.APP_NAME + " - Dashboard");
        setSize(AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppConfig.BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = createHeader();
        
        // Content (scrollable)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(AppConfig.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Balance Card
        JPanel balanceCard = createBalanceCard();
        contentPanel.add(balanceCard);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Quick Actions
        JPanel quickActionsPanel = createQuickActions();
        contentPanel.add(quickActionsPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Recent Transactions
        JPanel transactionsSection = createTransactionsSection();
        contentPanel.add(transactionsSection);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Left: Greeting
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel greetingLabel = new JLabel("Hello, " + currentUser.getFullName().split(" ")[0] + "!");
        greetingLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        greetingLabel.setForeground(AppConfig.TEXT_PRIMARY);
        
        leftPanel.add(greetingLabel);
        
        // Right: Profile and Logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JLabel profileLabel = new JLabel("👤");
        profileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        profileLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileLabel.setToolTipText("Profile");
        profileLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openProfile();
            }
        });
        
        JLabel logoutLabel = new JLabel("Logout");
        logoutLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutLabel.setForeground(AppConfig.DANGER_COLOR);
        logoutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleLogout();
            }
        });
        
        rightPanel.add(profileLabel);
        rightPanel.add(logoutLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createBalanceCard() {
        ModernPanel balanceCard = new ModernPanel(AppConfig.PRIMARY_COLOR, 16, true);
        balanceCard.setLayout(new BorderLayout());
        balanceCard.setBorder(new EmptyBorder(30, 30, 30, 30));
        balanceCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        // Gradient effect (simulated with solid color for simplicity)
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Available Balance");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(255, 255, 255, 200));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        balancePanel.setOpaque(false);
        balancePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        balanceLabel = new JLabel(ValidationUtil.formatAmount(currentUser.getBalance()));
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        balanceLabel.setForeground(Color.WHITE);
        
        JLabel eyeIcon = new JLabel(" 👁");
        eyeIcon.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        eyeIcon.setForeground(Color.WHITE);
        eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleBalanceVisibility();
            }
        });
        
        balancePanel.add(balanceLabel);
        balancePanel.add(eyeIcon);
        
        JLabel phoneLabel = new JLabel(ValidationUtil.formatPhoneNumber(currentUser.getPhoneNumber()));
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        phoneLabel.setForeground(new Color(255, 255, 255, 180));
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(balancePanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(phoneLabel);
        
        balanceCard.add(contentPanel, BorderLayout.CENTER);
        
        return balanceCard;
    }
    
    private JPanel createQuickActions() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        JLabel titleLabel = new JLabel("Quick Actions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(AppConfig.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(16));
        
        // Actions grid
        JPanel actionsGrid = new JPanel(new GridLayout(2, 3, 20, 20));
        actionsGrid.setOpaque(false);
        actionsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        
        // Send Money
        QuickActionButton sendMoneyBtn = new QuickActionButton("↑", "Send Money", AppConfig.PRIMARY_COLOR);
        sendMoneyBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openSendMoney();
            }
        });
        
        // Cash Out
        QuickActionButton cashOutBtn = new QuickActionButton("💵", "Cash Out", AppConfig.WARNING_COLOR);
        cashOutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openCashOut();
            }
        });
        
        // Add Money
        QuickActionButton addMoneyBtn = new QuickActionButton("↓", "Add Money", AppConfig.SUCCESS_COLOR);
        addMoneyBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openAddMoney();
            }
        });
        
        // Mobile Recharge
        QuickActionButton rechargeBtn = new QuickActionButton("📱", "Recharge", AppConfig.INFO_COLOR);
        rechargeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openMobileRecharge();
            }
        });
        
        // Pay Bill
        QuickActionButton payBillBtn = new QuickActionButton("💳", "Pay Bill", AppConfig.SECONDARY_COLOR);
        payBillBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(DashboardFrame.this, "Pay Bill feature coming soon!");
            }
        });
        
        // History
        QuickActionButton historyBtn = new QuickActionButton("📋", "History", new Color(139, 92, 246));
        historyBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openTransactionHistory();
            }
        });
        
        actionsGrid.add(sendMoneyBtn);
        actionsGrid.add(cashOutBtn);
        actionsGrid.add(addMoneyBtn);
        actionsGrid.add(rechargeBtn);
        actionsGrid.add(payBillBtn);
        actionsGrid.add(historyBtn);
        
        panel.add(actionsGrid);
        
        return panel;
    }
    
    private JPanel createTransactionsSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Recent Transactions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(AppConfig.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(16));
        
        // Transactions list
        transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        transactionsPanel.setBackground(Color.WHITE);
        transactionsPanel.setBorder(BorderFactory.createLineBorder(AppConfig.BORDER_COLOR, 1, true));
        
        panel.add(transactionsPanel);
        
        return panel;
    }
    
    private void loadTransactions() {
        transactionsPanel.removeAll();
        
        List<Transaction> transactions = transactionService.getTransactionHistory(currentUser.getUserId(), 10);
        
        if (transactions.isEmpty()) {
            JLabel emptyLabel = new JLabel("No transactions yet");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            emptyLabel.setForeground(AppConfig.TEXT_SECONDARY);
            emptyLabel.setBorder(new EmptyBorder(30, 20, 30, 20));
            transactionsPanel.add(emptyLabel);
        } else {
            for (Transaction transaction : transactions) {
                TransactionListItem item = new TransactionListItem(transaction, currentUser.getUserId());
                transactionsPanel.add(item);
            }
        }
        
        transactionsPanel.revalidate();
        transactionsPanel.repaint();
    }
    
    private void toggleBalanceVisibility() {
        balanceVisible = !balanceVisible;
        if (balanceVisible) {
            balanceLabel.setText(ValidationUtil.formatAmount(currentUser.getBalance()));
        } else {
            balanceLabel.setText("৳ ****");
        }
    }
    
    private void refreshBalance() {
        currentUser = userService.getUserById(currentUser.getUserId());
        if (balanceVisible) {
            balanceLabel.setText(ValidationUtil.formatAmount(currentUser.getBalance()));
        }
        loadTransactions();
    }
    
    private void openSendMoney() {
        SendMoneyDialog dialog = new SendMoneyDialog(this, currentUser);
        dialog.setVisible(true);
        refreshBalance();
    }
    
    private void openCashOut() {
        CashOutDialog dialog = new CashOutDialog(this, currentUser);
        dialog.setVisible(true);
        refreshBalance();
    }
    
    private void openAddMoney() {
        AddMoneyDialog dialog = new AddMoneyDialog(this, currentUser);
        dialog.setVisible(true);
        refreshBalance();
    }
    
    private void openMobileRecharge() {
        MobileRechargeDialog dialog = new MobileRechargeDialog(this, currentUser);
        dialog.setVisible(true);
        refreshBalance();
    }
    
    private void openTransactionHistory() {
        new TransactionHistoryFrame(currentUser).setVisible(true);
    }
    
    private void openProfile() {
        new ProfileFrame(currentUser, this).setVisible(true);
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
