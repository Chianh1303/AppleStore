package org.example.applestores.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private String customerName;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    private double totalAmount;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    // Ghi chú đơn hàng (không bắt buộc)
    @Column(columnDefinition = "TEXT")
    private String note;

    // Phương thức thanh toán: COD, BANK, etc.
    @Column(nullable = false)
    private String paymentMethod;

    // Getters and setters

    public Order() {
    }

    public Order(Long id, LocalDateTime createdAt, String customerName, User user, List<OrderItem> items, double totalAmount, String status, String address, String phone, String note, String paymentMethod) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.user = user;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.address = address;
        this.phone = phone;
        this.note = note;
        this.paymentMethod = paymentMethod;
    }

    public Order(Long id, LocalDateTime createdAt, String customerName, User user, List<OrderItem> items, double totalAmount, String status, String address, String phone) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.user = user;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.address = address;
        this.phone = phone;
    }

    public Order(Long id, LocalDateTime createdAt, String customerName, User user, List<OrderItem> items, double totalAmount, String status) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.user = user;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Order(Long id, LocalDateTime createdAt, String customerName, User user, List<OrderItem> items, double totalAmount) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.user = user;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public Order(Long id, LocalDateTime createdAt, String customerName, User user, List<OrderItem> items) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.user = user;
        this.items = items;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setOrderDate(LocalDateTime now) {
        this.createdAt = now;
    }

    public void setTotalAmount(double sum) {
//        this.items.forEach(item -> item.setPrice(item.getProduct().getPrice() *
        this.totalAmount = sum;
    }



    public double getTotalAmount() {
        return totalAmount;
    }
}

