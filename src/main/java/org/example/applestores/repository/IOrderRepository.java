package org.example.applestores.repository;

import org.example.applestores.model.Order;
import org.example.applestores.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderByIdDesc();
    List<Order> findByUser (User user);
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.id = :id")
    Order findByIdWithItems(@Param("id") Long id);

}
