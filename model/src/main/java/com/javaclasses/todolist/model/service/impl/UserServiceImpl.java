package com.javaclasses.todolist.model.service.impl;

import com.javaclasses.todolist.model.dto.LoginDTO;
import com.javaclasses.todolist.model.dto.RegistrationDTO;
import com.javaclasses.todolist.model.dto.SecurityTokenDTO;
import com.javaclasses.todolist.model.dto.UserDTO;
import com.javaclasses.todolist.model.entity.tinytype.Email;
import com.javaclasses.todolist.model.entity.tinytype.SecurityTokenId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.service.UserAuthenticationException;
import com.javaclasses.todolist.model.service.UserRegistrationException;
import com.javaclasses.todolist.model.service.UserService;

import java.util.Collection;

/**
 * Implementation of {@link UserService} interface
 */
public class UserServiceImpl implements UserService {

    private static UserServiceImpl userService;

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }

        return userService;
    }

    @Override
    public UserId register(RegistrationDTO registrationDTO) throws UserRegistrationException {
        return null;
    }

    @Override
    public SecurityTokenDTO login(LoginDTO loginDTO) throws UserAuthenticationException {
        return null;
    }

    @Override
    public UserDTO findByEmail(Email email) {
        return null;
    }

    @Override
    public UserDTO findById(UserId userId) {
        return null;
    }

    @Override
    public UserDTO findByToken(SecurityTokenId tokenId) {
        return null;
    }

    @Override
    public Collection<UserDTO> findAll() {
        return null;
    }

    @Override
    public void delete(UserId userId) {

    }
}
