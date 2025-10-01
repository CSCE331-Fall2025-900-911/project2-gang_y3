/* pseudocode: select count of orders, sum of order total grouped by hour
about: given a specific hour of the day, how many orders were placed and what was the total sum of the orders?
example: e.g., "12pm has 12345 orders totaling $86753" */

SELECT
    EXTRACT(HOUR FROM order_time) AS hour_of_day, 

    COUNT(order_id) AS number_of_orders,

    SUM(total_amount) AS total_sales
FROM
    Orders 
GROUP BY
    hour_of_day 
ORDER BY
    hour_of_day ASC;
