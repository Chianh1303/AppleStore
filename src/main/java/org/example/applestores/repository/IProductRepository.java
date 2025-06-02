package org.example.applestores.repository;

import org.example.applestores.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findAllByOrderByIdDesc();
    List<Product> findByIsFeaturedTrue();

    List<Product> findByIsFeaturedFalse();
}
