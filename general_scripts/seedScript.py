import csv
import random
import json
from datetime import datetime, timedelta

start_dt = datetime(2024, 1, 1)
end_dt = datetime(2024, 9, 30) 
sales_goal = 1000000
july4th = datetime(2024, 7, 4)
open_hour = 8
close_hour = 20

drink_menu = {
    1: {"name": "Classic Milk Tea", "price": 4.5, "category": "Milk Tea"},
    2: {"name": "Taro Milk Tea", "price": 4.75, "category": "Milk Tea"},
    3: {"name": "Thai Milk Tea", "price": 4.75, "category": "Milk Tea"},
    4: {"name": "Strawberry Fruit Tea", "price": 4.25, "category": "Fruit Tea"},
    5: {"name": "Mango Fruit Tea", "price": 4.25, "category": "Fruit Tea"},
    6: {"name": "Lychee Fruit Tea", "price": 4.25, "category": "Fruit Tea"},
    7: {"name": "Matcha Latte", "price": 5.25, "category": "Specialty Drinks"},
    8: {"name": "Brown Sugar Boba", "price": 5.5, "category": "Specialty Drinks"},
    9: {"name": "Strawberry Smoothie", "price": 5.75, "category": "Smoothies"},
    10: {"name": "Mango Smoothie", "price": 5.75, "category": "Smoothies"},
    11: {"name": "Lychee Smoothie", "price": 5.75, "category": "Smoothies"},
    12: {"name": "Egg Puff", "price": 4.0, "category": "Snacks/Desserts"},
    13: {"name": "Taiyaki", "price": 4.25, "category": "Snacks/Desserts"},
    14: {"name": "Popcorn Chicken", "price": 5.5, "category": "Snacks/Desserts"},
    15: {"name": "Honey Green Tea", "price": 4.25, "category": "Fruit Tea"},
    16: {"name": "Wintermelon Milk Tea", "price": 4.75, "category": "Milk Tea"}
}

customers = list(range(1, 101))

payment_opts = ["cash", "card"]

statuses = ["fulfilled", "pending", "canceled"]

ice_options = ["low", "medium", "high"]
sweetness = ["low", "medium", "high"]

def get_random_time(day, start_hr=8, end_hr=20):
    hr = random.randint(start_hr, end_hr - 1)
    mins = random.randint(0, 59)
    secs = random.randint(0, 59)
    return datetime.combine(day.date(), datetime.min.time().replace(hour=hr, minute=mins, second=secs))

def calc_orders_per_day(day, peak_day):
    daily_avg = 400
    
    if day.date() == peak_day.date():
        return daily_avg * 2
    
    if day.weekday() in [4, 5, 6]:
        daily_avg = int(daily_avg * 1.3)
    
    if day.month in [6, 7, 8]:
        daily_avg = int(daily_avg * 1.2)
    
    rand_factor = random.uniform(0.9, 1.1)
    return int(daily_avg * rand_factor)

def create_order_items():
    item_count = random.choices([1, 2, 3, 4], weights=[60, 25, 10, 5])[0]
    
    order_items = {}
    price_total = 0
    
    bestsellers = [1, 2, 4, 5, 8, 12, 13]
    
    for _ in range(item_count):
        if random.random() < 0.7:
            menu_id = random.choice(bestsellers)
        else:
            menu_id = random.choice(list(drink_menu.keys()))
        
        qty = random.choices([1, 2, 3], weights=[80, 15, 5])[0]
        
        if menu_id in order_items:
            order_items[menu_id] += qty
        else:
            order_items[menu_id] = qty
        
        price_total += drink_menu[menu_id]["price"] * qty
    
    return order_items, round(price_total, 2)

def add_customizations(order_items):
    custom_opts = {}
    
    for menu_id in order_items:
        if drink_menu[menu_id]["category"] in ["Milk Tea", "Fruit Tea", "Specialty Drinks", "Smoothies"]:
            ice_level = random.choice(ice_options)
            sugar_level = random.choice(sweetness)
            custom_opts[menu_id] = {"ice": ice_level, "sugar": sugar_level}
    
    return custom_opts

def stringify_items(order_items):
    return json.dumps(order_items, separators=(',', ':'))

def stringify_customs(custom_opts):
    if not custom_opts:
        return "{}"
    return json.dumps(custom_opts, separators=(',', ':'))

def build_orders():
    all_orders = []
    order_num = 1
    running_total = 0
    
    curr_day = start_dt
    while running_total < sales_goal:
        if curr_day > end_dt:
            curr_day = start_dt
            
        orders_today = calc_orders_per_day(curr_day, july4th)
        
        for _ in range(orders_today):
            if running_total >= sales_goal:
                break
                
            when = get_random_time(curr_day)
            
            order_items, total_cost = create_order_items()
            
            custom_opts = add_customizations(order_items)
            
            cust_id = random.choice(customers)
            pay_method = random.choices(payment_opts, weights=[40, 60])[0]
            status = random.choices(statuses, weights=[85, 10, 5])[0]
            
            new_order = {
                "order_id": order_num,
                "order_date": curr_day.strftime("%-m/%-d/%y"),
                "order_time": when.strftime("%H:%M:%S"),
                "total_amount": total_cost,
                "payment_method": pay_method,
                "order_status": status,
                "customer_id": cust_id,
                "item_link": stringify_items(order_items),
                "custom_id": stringify_customs(custom_opts)
            }
            
            all_orders.append(new_order)
            if status == "fulfilled":
                running_total += total_cost
            order_num += 1
        
        curr_day += timedelta(days=1)
    
    return all_orders

def save_to_csv(all_orders, filename="generated_orders.csv"):
    cols = ["order_id", "order_date", "order_time", "total_amount", 
            "payment_method", "order_status", "customer_id", "item_link", "custom_id"]
    
    with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=cols)
        writer.writeheader()
        writer.writerows(all_orders)

def main():
    all_orders = build_orders()    
    save_to_csv(all_orders)
if __name__ == "__main__":
    main()