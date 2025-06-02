package org.example.applestores.service.user;

import org.example.applestores.model.User;

import java.util.List;

public interface IUserService<T> {
    List<T> findAll();
    User findByUsername(String username);
    User login(String username, String password);
    void save(T t);
    void deleteUserById(Long id);
    User findById(Long id);
}
