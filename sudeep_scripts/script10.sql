-- what are the milk teas available
-- Connect to server and run: \i 'sudeep_scripts/script10.sql'
SELECT item_name FROM Menu WHERE category = 'Milk Tea' AND availability = 'TRUE';