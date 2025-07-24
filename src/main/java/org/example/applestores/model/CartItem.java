package org.example.applestores.model;

import javax.persistence.*;

public class CartItem {

    private Long id;

    private Product product;
    private User user;

    private int quantity;

    public CartItem() {
    }

    public CartItem(Product product, User user, int quantity) {
        this.product = product;
        this.user = user;
        this.quantity = quantity;
    }


    // Getter & Setter


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", product=" + product +
                ", user=" + user +
                ", quantity=" + quantity +
                '}';
    }


}
