# MySQL Workbench Setup Guide for Mobile Wallet

## Complete Step-by-Step Process

### Step 1: Install MySQL Server and MySQL Workbench

#### Download and Install MySQL
1. Go to: https://dev.mysql.com/downloads/installer/
2. Download **MySQL Installer for Windows**
3. Run the installer
4. Choose **"Developer Default"** installation type
5. Click **Next** through the installation
6. When prompted, set a **root password** (remember this!)
   - Example: `root` or `mypassword123`
7. Complete the installation

**Important:** MySQL Workbench is included in the MySQL Installer.

---

### Step 2: Verify MySQL Installation

1. Open **MySQL Workbench** from Start Menu
2. You should see a connection named **"Local instance MySQL80"** or similar
3. Click on it to connect
4. Enter the root password you set during installation
5. If connected successfully, you'll see the MySQL Workbench interface

---

### Step 3: Create the Database

#### Option A: Using the SQL Script (Recommended)

1. In MySQL Workbench, click **File → Open SQL Script**
2. Navigate to: `d:\GG\mobile-wallet\database_schema.sql`
3. Click **Open**
4. You'll see the SQL script in the editor
5. Click the **⚡ Execute** button (lightning bolt icon) or press **Ctrl+Shift+Enter**
6. Check the **Output** panel at the bottom - you should see:
   ```
   Database setup completed successfully!
   ```

#### Option B: Manual Execution

If you prefer to copy-paste:
1. Open the file `database_schema.sql` in any text editor
2. Copy all the content
3. In MySQL Workbench, paste it into a new query tab
4. Click the **⚡ Execute** button

---

### Step 4: Verify Database Creation

1. In the left sidebar, look for **"Schemas"** panel
2. Click the **🔄 Refresh** button (circular arrow icon)
3. You should now see **`mobile_wallet_db`**
4. Expand it by clicking the ▶ arrow
5. You should see 3 tables:
   - `users`
   - `transactions`
   - `transaction_types`

#### Check Sample Data

To verify sample data was inserted:
1. Right-click on the **`users`** table
2. Select **"Select Rows - Limit 1000"**
3. You should see 3 users:
   - John Doe (01712345678)
   - Jane Smith (01812345678)
   - Agent One (01912345678)

---

### Step 5: Configure Your Java Project

Now you need to tell your Java application how to connect to MySQL.

1. Open the file: `d:\GG\mobile-wallet\src\main\java\com\mobilewallet\config\AppConfig.java`

2. Find these lines (around line 11-13):
   ```java
   public static final String DB_URL = "jdbc:mysql://localhost:3306/mobile_wallet_db";
   public static final String DB_USER = "root";
   public static final String DB_PASSWORD = "root";
   ```

3. **Update the password** to match what you set during MySQL installation:
   ```java
   public static final String DB_PASSWORD = "your_actual_password";
   ```

**Example:**
If your MySQL root password is `mypassword123`, change it to:
```java
public static final String DB_PASSWORD = "mypassword123";
```

---

### Step 6: Test the Connection

#### Quick MySQL Connection Test

In MySQL Workbench, run this query to test:
```sql
USE mobile_wallet_db;
SELECT * FROM users;
```

You should see the 3 test users.

---

### Step 7: Run Your Java Application

Now you're ready to run the application!

#### Option A: Using Maven (Command Line)

1. Open **Command Prompt** (Win + R, type `cmd`, press Enter)
2. Navigate to your project:
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
2. Click **File → Open**
3. Select the folder: `d:\GG\mobile-wallet`
4. Wait for Maven to download dependencies (bottom-right corner)
5. Find `App.java` in the project explorer
6. Right-click on `App.java` → **Run 'App.main()'**

#### Option C: Using Eclipse

1. Open Eclipse
2. **File → Import → Maven → Existing Maven Projects**
3. Browse to: `d:\GG\mobile-wallet`
4. Click **Finish**
5. Right-click on project → **Run As → Java Application**
6. Select **App** as the main class

---

## Troubleshooting

### Problem 1: "Access denied for user 'root'@'localhost'"

**Solution:**
- Your password in `AppConfig.java` doesn't match your MySQL password
- Update `DB_PASSWORD` in `AppConfig.java` with the correct password

### Problem 2: "Communications link failure"

**Solution:**
- MySQL Server is not running
- Start MySQL:
  1. Press **Win + R**
  2. Type `services.msc` and press Enter
  3. Find **MySQL80** in the list
  4. Right-click → **Start**

### Problem 3: "Unknown database 'mobile_wallet_db'"

**Solution:**
- The database wasn't created
- Go back to **Step 3** and execute the SQL script again

### Problem 4: "No suitable driver found"

**Solution:**
- Maven dependencies not downloaded
- Run: `mvn clean install -U` to force update

### Problem 5: Can't connect to MySQL Workbench

**Solution:**
- Check if MySQL is running (see Problem 2)
- Verify the port is 3306:
  1. In MySQL Workbench, click on the connection
  2. Click **Configure**
  3. Check **Port** is set to `3306`

---

## Connection Details Summary

Here's what you need to know:

| Setting | Value |
|---------|-------|
| **Host** | localhost |
| **Port** | 3306 |
| **Database** | mobile_wallet_db |
| **Username** | root |
| **Password** | (what you set during installation) |

These values are configured in:
- **File:** `src/main/java/com/mobilewallet/config/AppConfig.java`
- **Lines:** 11-13

---

## Test Login Credentials

Once everything is connected, use these to test:

| User | Phone | PIN | Balance |
|------|-------|-----|---------|
| John Doe | 01712345678 | 1234 | ৳5,000 |
| Jane Smith | 01812345678 | 1234 | ৳3,000 |
| Agent One | 01912345678 | 1234 | ৳50,000 |

---

## Quick Checklist

- [ ] MySQL Server installed
- [ ] MySQL Workbench installed
- [ ] Database created (execute `database_schema.sql`)
- [ ] Verified 3 tables exist in `mobile_wallet_db`
- [ ] Updated password in `AppConfig.java`
- [ ] Maven installed (for building)
- [ ] Application runs without errors

---

## Need Help?

If you encounter any issues:

1. **Check MySQL is running:**
   - Open Services (Win + R → `services.msc`)
   - Look for MySQL80 → should say "Running"

2. **Verify database exists:**
   - Open MySQL Workbench
   - Refresh Schemas
   - Look for `mobile_wallet_db`

3. **Check console output:**
   - When you run the app, check the console
   - Look for "Database connected successfully!"
   - If you see errors, read the error message carefully

---

**You're all set!** 🎉

Once you complete these steps, your Java application will be connected to MySQL and ready to use.
