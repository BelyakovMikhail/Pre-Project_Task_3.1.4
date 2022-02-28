package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;


public interface UserService {
    Iterable<User> findAll();
    void save(User user);
    void delete(User user);
    User findById(Long id);
    User getUserByEmail (String email);
    void updateUser(User user);
}