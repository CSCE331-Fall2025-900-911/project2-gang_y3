-- Connect to server and run: \i 'joshua_scripts/special1.sql'

SELECT
    EXTRACT(
        WEEK
        FROM order_date
    ) AS number,
    DATE_TRUNC ('week', order_date)::date AS date,
    COUNT(*) AS count
FROM Orders
GROUP BY
    number, date
ORDER BY date;