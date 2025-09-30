--what is the cheapest item on the menu
SELECT item_name FROM Menu WHERE price = (SELECT MIN(price) FROM Menu);