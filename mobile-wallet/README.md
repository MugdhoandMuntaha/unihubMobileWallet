# Mobile Wallet Application - Setup Guide

## Quick Start Guide

### Step 1: Install Prerequisites

1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: Open Command Prompt and run `java -version`

2. **Apache Maven**
   - Download from: https://maven.apache.org/download.cgi
   - Extract to a folder (e.g., `C:\Program Files\Apache\maven`)
   - Add Maven to PATH:
     - Right-click "This PC" → Properties → Advanced System Settings
     - Click "Environment Variables"
     - Under "System Variables", find "Path" and click "Edit"
     - Click "New" and add Maven's bin folder (e.g., `C:\Program Files\Apache\maven\bin`)
     - Click OK on all dialogs
   - Restart Command Prompt and verify: `mvn -version`

3. **MySQL Server**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Install MySQL Server and MySQL Workbench
   - Remember the root password you set during installation

### Step 2: Setup Database

1. Open MySQL Workbench
2. Connect to your local MySQL server
3. Open the file `database_schema.sql` (located in the project root)
4. Execute the script (click the lightning bolt icon or press Ctrl+Shift+Enter)
5. Verify the database was created:
   - You should see `mobile_wallet_db` in the Schemas panel
   - It should contain tables: `users`, `transactions`, `transaction_types`

### Step 3: Configure Database Connection

1. Open `src/main/java/com/mobilewallet/config/AppConfig.java`
2. Update the database credentials if needed:
   ```java
   public static final String DB_URL = "jdbc:mysql://localhost:3306/mobile_wallet_db";
   public static final String DB_USER = "root";
   public static final String DB_PASSWORD = "your_mysql_password";
   ```

### Step 4: Build and Run

#### Option A: Using Maven Command Line

1. Open Command Prompt
2. Navigate to the project folder:
   ```bash
   cd d:\GG\mobile-wallet
   ```
3. Build the project:
   ```bash
   mvn clean package
   ```
4. Run the application:
   ```bash
   mvn exec:java
   ```

#### Option B: Using IntelliJ IDEA

1. Open IntelliJ IDEA
2. Click "Open" and select the `mobile-wallet` folder
3. Wait for Maven to download dependencies (check bottom-right corner)
4. Right-click on `App.java` → Run 'App.main()'

#### Option C: Using Eclipse

1. Open Eclipse
2. File → Import → Maven → Existing Maven Projects
3. Select the `mobile-wallet` folder
4. Right-click on project → Run As → Java Application
5. Select `App` as the main class

### Step 5: Test the Application

#### Default Test Accounts

The database comes with pre-configured test accounts:

| Name | Phone | PIN | Balance |
|------|-------|-----|---------|
| John Doe | 01712345678 | 1234 | ৳5,000 |
| Jane Smith | 01812345678 | 1234 | ৳3,000 |
| Agent One | 01912345678 | 1234 | ৳50,000 |

#### Test Scenarios

1. **Login Test**:
   - Phone: `01712345678`
   - PIN: `1234`
   - You should see the dashboard with balance ৳5,000

2. **Send Money Test**:
   - Click "Send Money"
   - Recipient: `01812345678` (Jane Smith)
   - Amount: `500`
   - Note: `Test payment`
   - PIN: `1234`
   - Confirm the transaction

3. **Mobile Recharge Test**:
   - Click "Recharge"
   - Phone: `01712345678`
   - Operator: Select any
   - Amount: Select `৳ 50`
   - PIN: `1234`

4. **View History**:
   - Click "History" to see all transactions
   - Use the filter dropdown to filter by type

5. **Register New User**:
   - Logout
   - Click "Register" on login screen
   - Fill in the form with your details
   - Use a unique phone number (e.g., `01612345678`)
   - Create a PIN
   - Login with your new account

## Features Overview

### 🏠 Dashboard
- **Balance Card**: Shows current balance with hide/show toggle
- **Quick Actions**: 6 action buttons for common operations
- **Recent Transactions**: Last 10 transactions with details

### 💸 Send Money
- Send money to any registered user
- Fee: ৳5 for amounts over ৳1,000
- PIN verification required

### 💵 Cash Out
- Withdraw money from agents
- Fee: 1.85% of amount
- Real-time fee calculation

### ⬇️ Add Money
- Add funds from bank/card
- No fees
- Instant credit

### 📱 Mobile Recharge
- Recharge any mobile number
- Support for all major operators
- Predefined amounts or custom

### 📋 Transaction History
- View all transactions
- Filter by type
- Detailed transaction information

### 👤 Profile
- Update name and email
- Change PIN
- View account information

## Troubleshooting

### "mvn is not recognized"
- Maven is not installed or not in PATH
- Follow Step 1 to install Maven properly
- Restart Command Prompt after adding to PATH

### "Database connection failed"
- MySQL server is not running
- Check credentials in `AppConfig.java`
- Verify database exists in MySQL Workbench

### "Access denied for user 'root'"
- Wrong MySQL password
- Update `DB_PASSWORD` in `AppConfig.java`

### Build errors
- Ensure JDK 11+ is installed
- Delete `.m2` folder in your user directory and rebuild
- Check internet connection (Maven needs to download dependencies)

## Project Structure

```
mobile-wallet/
├── src/main/java/com/mobilewallet/
│   ├── App.java                         # Main entry point
│   ├── config/AppConfig.java            # Configuration
│   ├── model/                           # Data models
│   ├── service/                         # Business logic
│   ├── util/                            # Utilities
│   └── ui/                              # User interface
│       ├── LoginFrame.java
│       ├── RegisterFrame.java
│       ├── DashboardFrame.java
│       ├── TransactionHistoryFrame.java
│       ├── ProfileFrame.java
│       ├── components/                  # Custom UI components
│       └── dialogs/                     # Transaction dialogs
├── database_schema.sql                  # Database setup
├── pom.xml                              # Maven configuration
└── README.md                            # This file
```

## Technologies

- **Java 11**: Programming language
- **Swing**: GUI framework
- **FlatLaf 3.2.5**: Modern look and feel
- **MySQL 8.0**: Database
- **Maven**: Build tool
- **BCrypt**: Password encryption

## Color Theme

Modern purple/blue gradient theme inspired by Bkash:
- Primary Purple: #7C3AED
- Secondary Blue: #3B82F6
- Success Green: #22C55E
- Danger Red: #EF4444
- Warning Orange: #FB923C

## Support

For issues or questions:
1. Check the Troubleshooting section
2. Verify all prerequisites are installed
3. Check database connection and credentials
4. Review console output for error messages

---

**Note**: This is an educational project demonstrating a mobile wallet application with modern UI design and database integration.
