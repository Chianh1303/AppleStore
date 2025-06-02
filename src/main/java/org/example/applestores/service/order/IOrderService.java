package org.example.applestores.service.order;

import org.example.applestores.model.CartItem;
import org.example.applestores.model.User;

import java.util.List;

public interface IOrderService<T> {
    void save(T t);
    List<T> findAll();
    T findById(Long id);
    void checkout(User user, List<CartItem> cartItems);

}

