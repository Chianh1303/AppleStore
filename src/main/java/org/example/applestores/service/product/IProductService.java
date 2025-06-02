package org.example.applestores.service.product;

import org.example.applestores.model.Product;

import java.util.List;

public interface IProductService<T> {
    List<T> findAll();
    T findById(Long id);
    void save(T t);
    void delete(Long id);
    List<Product> searchByName(String keyword);

    List<Product> getFeaturedProducts();

    List<Product> getNormalProducts();
}
