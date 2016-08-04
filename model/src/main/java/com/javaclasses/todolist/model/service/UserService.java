package com.javaclasses.todolist.model.service;

import com.javaclasses.todolist.model.dto.LoginDTO;
import com.javaclasses.todolist.model.dto.RegistrationDTO;
import com.javaclasses.todolist.model.dto.SecurityTokenDTO;
import com.javaclasses.todolist.model.dto.UserDTO;
import com.javaclasses.todolist.model.entity.tinytype.Email;
import com.javaclasses.todolist.model.entity.tinytype.SecurityTokenId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;

import java.util.Collection;

/**
 * Basic interface for user management
 */
public interface UserService {

    /**
     * Registers new user
     * @param registrationDTO DTO with registration information
     * @return Id of registered user
     * @throws UserRegistrationException In case of error during registration
     */
    UserId register(RegistrationDTO registrationDTO)
            throws UserRegistrationException;

    /**
     * Authorizes user into the system
     * @param loginDTO DTO with login information
     * @return Security token DTO for current user
     * @throws UserAuthenticationException In case of error during login
     */
    SecurityTokenDTO login(LoginDTO loginDTO)
            throws UserAuthenticationException;

    /**
     * Searches for user by email
     * @param email Email of user to be found
     * @return DTO with user information
     */
    UserDTO findByEmail(Email email);

    /**
     * Searches for user by id
     * @param userId Id of user to be found
     * @return DTO with user information
     */
    UserDTO findById(UserId userId);

    /**
     * Searches for user by security token id
     * @param tokenId Security token id of user to be found
     * @return DTO with user information
     */
    UserDTO findByToken(SecurityTokenId tokenId);

    /**
     * Gets all registered users
     * @return Collection of DTOs with user information
     */
    Collection<UserDTO> findAll();

    /**
     * Logout user from service
     * @param tokenId Security token id of user to be logged out
     */
    void logout(SecurityTokenId tokenId);

    /**
     * Delete user from repository by user id
     * @param userId Id of user to be deleted
     */
    void delete(UserId userId);
}
