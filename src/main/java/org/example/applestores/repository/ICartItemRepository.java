package org.example.applestores.repository;

import org.example.applestores.model.CartItem;
import org.example.applestores.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    void deleteByUser(User user);
    void deleteByUserAndProductId(User user, Long productId);
}
