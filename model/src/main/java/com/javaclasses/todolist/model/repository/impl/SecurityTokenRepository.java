package com.javaclasses.todolist.model.repository.impl;

import com.javaclasses.todolist.model.entity.SecurityToken;
import com.javaclasses.todolist.model.entity.tinytype.SecurityTokenId;
import com.javaclasses.todolist.model.repository.InMemoryRepository;

import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link InMemoryRepository} implementation for authenticated users
 */
public class SecurityTokenRepository
        extends InMemoryRepository<SecurityTokenId, SecurityToken> {

    private static SecurityTokenRepository tokenRepository;

    private AtomicLong idCounter = new AtomicLong(1);

    private SecurityTokenRepository() {
    }

    public static SecurityTokenRepository getInstance() {
        if (tokenRepository == null) {
            tokenRepository = new SecurityTokenRepository();
        }

        return tokenRepository;
    }

    @Override
    protected SecurityTokenId generateId() {
        return new SecurityTokenId(idCounter.getAndIncrement());
    }
}
