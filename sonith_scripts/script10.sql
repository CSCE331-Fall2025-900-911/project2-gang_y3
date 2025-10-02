-- What is the total revenue of all the sales
SELECT SUM(total_amount) AS total_sales FROM orders WHERE order_status = 'fulfilled';
