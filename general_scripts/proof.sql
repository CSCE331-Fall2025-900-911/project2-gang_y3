SELECT COUNT(*) AS total_orders
FROM Orders;

SELECT 
    order_date AS peak_sales_date,
    COUNT(*) AS orders_count
FROM Orders
GROUP BY order_date
ORDER BY orders_count DESC
LIMIT 1;

SELECT SUM(total_amount) AS total_fulfilled_revenue
FROM Orders
WHERE order_status = 'fulfilled';

SELECT 
    (SELECT COUNT(*) FROM Orders) AS total_orders,
    (SELECT order_date FROM Orders 
     GROUP BY order_date 
     ORDER BY COUNT(*) DESC 
     LIMIT 1) AS peak_sales_date,
    (SELECT COUNT(*) FROM Orders 
     GROUP BY order_date 
     ORDER BY COUNT(*) DESC 
     LIMIT 1) AS peak_day_orders,
    (SELECT COALESCE(SUM(total_amount), 0) FROM Orders 
     WHERE order_status = 'fulfilled') AS total_fulfilled_revenue;
