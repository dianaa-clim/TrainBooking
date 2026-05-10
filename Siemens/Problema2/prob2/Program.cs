using System;
using System.Collections.Generic;

namespace SieMarket
{
    public class OrderItem
    {
        public string ProductName
        {
            get;
        }
        public int Quantity
        {
            get;
        }
        public double PriceAtPurchase
        {
            get;
        }
        public OrderItem(string productName, int quantity, double priceAtPurchase)
        {
            if (string.IsNullOrEmpty(productName))
            {
                throw new ArgumentException("Product name cannot be null or empty.");
            }
            if (quantity <= 0)
            {
                throw new ArgumentException("Quantity must be greater than zero.");
            }
            if (priceAtPurchase < 0)
            {
                throw new ArgumentException("Price at purchase cannot be negative.");
            }
            ProductName = productName;
            Quantity = quantity;
            PriceAtPurchase = priceAtPurchase;
        }
        public double totalPrice()
        {
            return Quantity * PriceAtPurchase;
        }

    }

    public class Order
    {
        public string OrderId
        {
            get;
        }
        public string CustomerName
        {
            get;
        }
        public DateTime OrderDate
        {
            get;
        }
        private List<OrderItem> orderItems = new();
        public Order(string orderId, string customerName, DateTime orderDate)
        {
            if (string.IsNullOrEmpty(orderId))
            {
                throw new ArgumentException("Order ID cannot be null or empty.");
            }
            if (string.IsNullOrEmpty(customerName))
            {
                throw new ArgumentException("Customer name cannot be null or empty.");
            }
            OrderId = orderId;
            CustomerName = customerName;
            OrderDate = orderDate;
        }
        public void AddItem(OrderItem item)
        {
            if (item is null)
            {
                throw new ArgumentException("Order item cannot be null.");
            }
            orderItems.Add(item);
        }
        public double TotalOrderPriceBeforeDiscount()
        {
            double total = 0;
            foreach (var item in orderItems)
            {
                total += item.totalPrice();
            }
            return total;
        }
        public double TotalOrderPriceAfterDiscount()
        {
            double totalBeforeDiscount = TotalOrderPriceBeforeDiscount();
            if (totalBeforeDiscount > 500)
            {
                return totalBeforeDiscount * 0.9;
            }
            return totalBeforeDiscount;
        }
        public List<OrderItem> GetOrderItems()
        {
            return new List<OrderItem>(orderItems);
        }
    }

    public class OrdersAnalytics
    {
        public string CustomerWhoSpentTheMost(List<Order> order)
        {
            if (order is null || order.Count == 0)
            {
                throw new ArgumentException("Order list cannot be null or empty.");
            }
            var customerSpending = new Dictionary<string, double>();
            foreach (var ord in order)
            {
                double totalPrice = ord.TotalOrderPriceAfterDiscount();
                if (customerSpending.ContainsKey(ord.CustomerName))
                {
                    customerSpending[ord.CustomerName] += totalPrice;
                }
                else
                {
                    customerSpending[ord.CustomerName] = totalPrice;
                }
            }
            string topCustomer = null;
            double maxSpending = 0;
            foreach (var kvp in customerSpending)
            {
                if (kvp.Value > maxSpending)
                {
                    maxSpending = kvp.Value;
                    topCustomer = kvp.Key;
                }
            }
            return topCustomer;
        }

        public List<KeyValuePair<string, int>> PopularProducts(List<Order> orders)
        {
            if (orders is null || orders.Count == 0)
            {
                throw new ArgumentException("Order list cannot be null or empty.");
            }
            Dictionary<string, int> productPopularity = new Dictionary<string, int>();
            foreach (var order in orders)
            {
                foreach (var item in order.GetOrderItems())
                {
                    if (productPopularity.ContainsKey(item.ProductName))
                    {
                        productPopularity[item.ProductName] += item.Quantity;
                    }
                    else
                    {
                        productPopularity[item.ProductName] = item.Quantity;
                    }
                }
            }
            List<KeyValuePair<string, int>> sortedProducts = new List<KeyValuePair<string, int>>();
            foreach (var kvp in productPopularity)
            {
                sortedProducts.Add(kvp);
            }
            sortedProducts.Sort(delegate (KeyValuePair<string, int> a, KeyValuePair<string, int> b)
            {
                return b.Value.CompareTo(a.Value);
            });
            return sortedProducts;
        }
    }
    public class Demo
    {
        public static void Main(string[] args)
        {
            Order order1 = new Order("ORD123", "John Doe", DateTime.Now);
            order1.AddItem(new OrderItem("Laptop", 1, 599));
            order1.AddItem(new OrderItem("Mouse", 2, 25));
            Order order2 = new Order("ORD124", "Jane Smith", DateTime.Now);
            order2.AddItem(new OrderItem("Smartphone", 1, 799));
            Order order3 = new Order("ORD125", "John Doe", DateTime.Now);
            order3.AddItem(new OrderItem("Smartphone", 1, 299));
            Console.WriteLine($"Total price before discount: {order1.TotalOrderPriceBeforeDiscount():C}");
            Console.WriteLine($"Total price after discount: {order1.TotalOrderPriceAfterDiscount():C}");
            OrdersAnalytics analytics = new OrdersAnalytics();
            List<Order> orders = new List<Order> { order1, order2, order3 };
            string topCustomer = analytics.CustomerWhoSpentTheMost(orders);
            Console.WriteLine($"Customer who spent the most: {topCustomer}");
            List<KeyValuePair<string, int>> popularItems = analytics.PopularProducts(orders);
            Console.WriteLine("Popular products total sold:");
            foreach (var item in popularItems)
            {
                Console.WriteLine($"{item.Key}: {item.Value}");
            }
        }
    }
}