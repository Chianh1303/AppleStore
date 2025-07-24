package org.example.applestores.service.order;

import org.example.applestores.model.Order;
import org.example.applestores.model.User;
import org.example.applestores.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class OrderService implements IOrderService<Order> {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public void save(Order order) {
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            Throwable root = e instanceof InvocationTargetException ? ((InvocationTargetException)e).getTargetException() : e;
            root.printStackTrace(); // log kỹ
            throw e;
        }

    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAllByOrderByIdDesc(); // giống với productService
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }


}

