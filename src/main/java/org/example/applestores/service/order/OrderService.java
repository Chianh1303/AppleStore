package org.example.applestores.service.order;

import org.example.applestores.model.Order;
import org.example.applestores.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrderService<Order> {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAllByOrderByIdDesc(); // giống với productService
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


}

