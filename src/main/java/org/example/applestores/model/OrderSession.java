package org.example.applestores.model;

import java.time.LocalDateTime;
import java.util.List;

public class OrderSession {
    private List<CartItem> items;
    private long totalAmount;
    private LocalDateTime orderTime;

    // Constructors
    public OrderSession(List<CartItem> items, long totalAmount, LocalDateTime orderTime) {
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderTime = orderTime;
    }

    public OrderSession() {
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "OrderSession{" +
                "items=" + items +
                ", totalAmount=" + totalAmount +
                ", orderTime=" + orderTime +
                '}';
    }
}

