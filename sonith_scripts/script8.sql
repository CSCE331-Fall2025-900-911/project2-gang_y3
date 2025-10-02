-- how much boba is in stock
-- Connect to server and run: \i 'sonith_scripts/script8.sql'
SELECT quantity_in_stock, unit FROM Inventory WHERE item_name = 'Regular Boba';
