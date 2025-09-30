-- Connect to server and run: \i 'updateServer.sql'

-- Drop all existing tables
DROP TABLE IF EXISTS Orders CASCADE;
DROP TABLE IF EXISTS Menu CASCADE;
DROP TABLE IF EXISTS Customer CASCADE;
DROP TABLE IF EXISTS Inventory CASCADE;
DROP TABLE IF EXISTS Authentication CASCADE;

-- Create tables
CREATE TABLE Authentication (
    user_id INTEGER PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);
CREATE TABLE Customer (
    customer_id INTEGER PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15) UNIQUE NOT NULL
);
CREATE TABLE Inventory (
    item_id INTEGER PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    quantity_in_stock INTEGER NOT NULL DEFAULT 0,
    unit VARCHAR(20) NOT NULL,
    red_threshold INTEGER NOT NULL DEFAULT 5,
    yellow_threshold INTEGER NOT NULL DEFAULT 15
);
CREATE TABLE Menu (
    menu_item_id INTEGER PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    availability BOOLEAN NOT NULL DEFAULT TRUE,
    inventory_link TEXT
);
CREATE TABLE Orders (
    order_id INTEGER PRIMARY KEY,
    order_date DATE NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    order_status VARCHAR(20) NOT NULL,
    customer_id INTEGER REFERENCES Customer(customer_id),
    item_link TEXT,
    custom_id TEXT
);

-- Copy data from CSV files into tables
\COPY Authentication FROM 'tables/Authentication.csv' WITH CSV HEADER;
\COPY Customer FROM 'tables/Customer.csv' WITH CSV HEADER;
\COPY Inventory FROM 'tables/Inventory.csv' WITH CSV HEADER;
\COPY Menu FROM 'tables/Menu.csv' WITH CSV HEADER;
\COPY Orders FROM 'tables/Orders.csv' WITH CSV HEADER;

-- Verify data insertions
SELECT 'Authentication' as table_name, COUNT(*) as row_count FROM Authentication
UNION ALL
SELECT 'Customer' as table_name, COUNT(*) as row_count FROM Customer
UNION ALL
SELECT 'Inventory' as table_name, COUNT(*) as row_count FROM Inventory
UNION ALL
SELECT 'Menu' as table_name, COUNT(*) as row_count FROM Menu
UNION ALL
SELECT 'Orders' as table_name, COUNT(*) as row_count FROM Orders;

-- Display test data from each table
SELECT 'Authentication Sample:' as info;
SELECT * FROM Authentication LIMIT 3;
SELECT 'Customer Sample:' as info;
SELECT * FROM Customer LIMIT 3;
SELECT 'Inventory Sample:' as info;
SELECT * FROM Inventory LIMIT 3;
SELECT 'Menu Sample:' as info;
SELECT * FROM Menu LIMIT 3;
SELECT 'Orders Sample:' as info;
SELECT * FROM Orders LIMIT 3;