# Login Issue - Troubleshooting Guide

## The Problem
You're getting a `NullPointerException` which means the database connection failed. The application cannot connect to MySQL.

## Solutions (Try in Order)

### Solution 1: Check if MySQL Server is Running ⭐ MOST COMMON

1. Press **Windows + R**
2. Type: `services.msc` and press Enter
3. Scroll down and find **MySQL80** (or MySQL)
4. Check the **Status** column:
   - If it says **"Running"** → Go to Solution 2
   - If it's blank or says **"Stopped"** → Right-click → **Start**
5. Try running your app again

---

### Solution 2: Verify Database Exists

1. Open **MySQL Workbench**
2. Connect to your local MySQL instance
3. Look at the left sidebar under **"Schemas"**
4. Click the **🔄 Refresh** button
5. Do you see **`mobile_wallet_db`**?
   - **YES** → Go to Solution 3
   - **NO** → Follow steps below:

#### Create the Database:
1. In MySQL Workbench, click **File → Open SQL Script**
2. Navigate to: `d:\GG\mobile-wallet\database_schema.sql`
3. Click **Open**
4. Click the **⚡ Execute** button (lightning bolt icon)
5. Check output - should say "Database setup completed successfully!"
6. Refresh Schemas - you should now see `mobile_wallet_db`

---

### Solution 3: Check MySQL Password

Your MySQL password might not be "root". Let's check:

1. Open MySQL Workbench
2. Try to connect - it will ask for a password
3. What password works? (Remember it!)
4. Open: `d:\GG\mobile-wallet\src\main\java\com\mobilewallet\config\AppConfig.java`
5. Change line 13 to match your actual password:

**Current:**
```java
public static final String DB_PASSWORD = "root";
```

**Change to your actual password:**
```java
public static final String DB_PASSWORD = "your_actual_password";
```

For example, if your password is `admin123`:
```java
public static final String DB_PASSWORD = "admin123";
```

6. Save the file
7. Rebuild and run the app

---

### Solution 4: Check MySQL Port

1. In MySQL Workbench, click on your connection
2. Click **Configure** (or right-click → Edit Connection)
3. Check the **Port** field - it should be **3306**
4. If it's different (like 3307), update `AppConfig.java` line 11:

```java
public static final String DB_URL = "jdbc:mysql://localhost:3307/mobile_wallet_db";
```
(Change 3306 to your actual port)

---

### Solution 5: Rebuild the Project

Sometimes Maven dependencies don't load properly:

```bash
cd d:\GG\mobile-wallet
mvn clean install -U
mvn exec:java
```

The `-U` flag forces Maven to update all dependencies.

---

## Quick Test: Can You Connect to MySQL?

Run this test in MySQL Workbench:

```sql
-- Test 1: Check if database exists
SHOW DATABASES LIKE 'mobile_wallet_db';

-- Test 2: Check if tables exist
USE mobile_wallet_db;
SHOW TABLES;

-- Test 3: Check if users exist
SELECT * FROM users;
```

**Expected Results:**
- Test 1: Should show `mobile_wallet_db`
- Test 2: Should show 3 tables (users, transactions, transaction_types)
- Test 3: Should show 3 users (John Doe, Jane Smith, Agent One)

If any test fails, the database isn't set up correctly.

---

## Still Not Working?

### Check Console Output

When you run the app, look at the console output. You should see:
```
Database connected successfully!
```

If you see an error instead, copy the error message and check:

**"Access denied for user 'root'@'localhost'"**
→ Wrong password (Solution 3)

**"Unknown database 'mobile_wallet_db'"**
→ Database not created (Solution 2)

**"Communications link failure"**
→ MySQL not running (Solution 1)

**"No suitable driver found"**
→ Maven dependencies issue (Solution 5)

---

## What's Your MySQL Password?

If you forgot your MySQL password:

### Option A: Reset MySQL Password
1. Stop MySQL service
2. Follow MySQL password reset guide
3. Update `AppConfig.java` with new password

### Option B: Use Default
During installation, if you didn't change it, try these common defaults:
- `root`
- `password`
- `admin`
- (blank/empty - use `""`)

---

## Need to Start Fresh?

If nothing works, you can reinstall MySQL:

1. Uninstall MySQL from Control Panel
2. Delete folder: `C:\ProgramData\MySQL`
3. Reinstall MySQL from: https://dev.mysql.com/downloads/installer/
4. During installation, set password to: `root` (simple and matches the code)
5. Run the database_schema.sql script
6. Run your app

---

## Checklist

Before running the app, verify:

- [ ] MySQL Server is **Running** (check services.msc)
- [ ] Database `mobile_wallet_db` **exists** (check MySQL Workbench)
- [ ] Password in `AppConfig.java` **matches** your MySQL password
- [ ] You can **connect** to MySQL Workbench successfully
- [ ] The 3 tables exist in the database
- [ ] Maven dependencies are downloaded

---

**Most likely issue:** MySQL Server is not running OR wrong password in AppConfig.java

Try Solution 1 first! 🚀
