package org.example.applestores.service.order;

import org.example.applestores.model.CartItem;
import org.example.applestores.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderService<T> {
    void save(T t);
    List<T> findAll();
    T findById(Long id);

}

