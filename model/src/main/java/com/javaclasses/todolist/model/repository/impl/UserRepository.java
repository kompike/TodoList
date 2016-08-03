package com.javaclasses.todolist.model.repository.impl;

import com.javaclasses.todolist.model.entity.User;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.repository.InMemoryRepository;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link InMemoryRepository} implementation for user entity
 */
public class UserRepository extends InMemoryRepository<UserId, User> {

    private static UserRepository userRepository;

    private AtomicLong idCounter = new AtomicLong(1);

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }

        return userRepository;
    }

    public User findByEmail(String email) {
        final Collection<User> users = findAll();
        User user = null;

        for (User currentUser : users) {
            if (currentUser.getEmail().getEmail().equals(email)) {
                user = currentUser;
                break;
            }
        }

        return user;
    }

    @Override
    protected UserId generateId() {
        return new UserId(idCounter.getAndIncrement());
    }
}
