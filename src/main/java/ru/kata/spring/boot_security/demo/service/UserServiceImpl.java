package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager em;

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
    @Transactional
    public void updateUser(User user) {
        em.merge(user);
    }


}