package org.example.applestores.repository;

import org.example.applestores.model.Orders;
import org.example.applestores.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByOrderByIdDesc();
}
