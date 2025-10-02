-- pseudocode: select top 10 sums of order total grouped by day in descending order by order total
-- about: given a specific day, what was the sum of the top 10 order totals?
-- example: "30 August has $12345 of top sales"

-- Connect to server and run: \i 'sudeep_scripts/special3.sql'

SELECT order_date::date AS date, SUM(total_amount) AS total
FROM Orders
GROUP BY date
ORDER BY total DESC
LIMIT 10;