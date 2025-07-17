package org.example.applestores.model;

import javax.persistence.*;

    @Entity
    @Table(name = "order_items")
    public class OrderItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private int quantity;
        private double price;

        @ManyToOne
        @JoinColumn(name = "product_id")
        private Product product;

        @ManyToOne
        @JoinColumn(name = "order_id")
        private Order order;

        // Getters and setters

        public OrderItem() {
        }

        public OrderItem(Long id, int quantity, double price, Product product, Order order) {
            this.id = id;
            this.quantity = quantity;
            this.price = price;
            this.product = product;
            this.order = order;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        @Override
        public String toString() {
            return "OrderItem{" +
                    "id=" + id +
                    ", quantity=" + quantity +
                    ", price=" + price +
                    ", product=" + product +
                    ", order=" + order +
                    '}';
        }
    }




