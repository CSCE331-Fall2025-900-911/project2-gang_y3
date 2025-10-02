--what is the cheapest item on the menu
-- Connect to server and run: \i 'sudeep_scripts/script11.sql'
SELECT item_name FROM Menu WHERE price = (SELECT MIN(price) FROM Menu);