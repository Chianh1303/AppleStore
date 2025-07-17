package org.example.applestores.repository;

import org.example.applestores.model.Order;
import org.example.applestores.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderByIdDesc();
    List<Order> findByUser (User user);
}
