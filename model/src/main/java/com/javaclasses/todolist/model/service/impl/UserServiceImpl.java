package com.javaclasses.todolist.model.service.impl;

import com.javaclasses.todolist.model.dto.LoginDTO;
import com.javaclasses.todolist.model.dto.RegistrationDTO;
import com.javaclasses.todolist.model.dto.SecurityTokenDTO;
import com.javaclasses.todolist.model.dto.UserDTO;
import com.javaclasses.todolist.model.entity.SecurityToken;
import com.javaclasses.todolist.model.entity.User;
import com.javaclasses.todolist.model.entity.tinytype.Email;
import com.javaclasses.todolist.model.entity.tinytype.Password;
import com.javaclasses.todolist.model.entity.tinytype.SecurityTokenId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.repository.impl.SecurityTokenRepository;
import com.javaclasses.todolist.model.repository.impl.UserRepository;
import com.javaclasses.todolist.model.service.UserAuthenticationException;
import com.javaclasses.todolist.model.service.UserRegistrationException;
import com.javaclasses.todolist.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of {@link UserService} interface
 */
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String EMAIL_PATTERN =
            "^([A-Za-z0-9+_.-]+@[A-Za-z]+(\\.[A-Za-z]+)*\\.[A-Za-z]{2,})$";

    private static UserServiceImpl userService;

    private final UserRepository userRepository =
            UserRepository.getInstance();
    private final SecurityTokenRepository tokenRepository =
            SecurityTokenRepository.getInstance();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }

        return userService;
    }

    @Override
    public UserId register(RegistrationDTO registrationDTO)
            throws UserRegistrationException {

        if (log.isInfoEnabled()) {
            log.info("Start registering new user...");
        }

        final String email = registrationDTO.getEmail().trim();
        final String password = registrationDTO.getPassword();
        final String confirmPassword = registrationDTO.getConfirmPassword();

        checkNotNull(email, "Email cannot be null");
        checkNotNull(password, "Password cannot be null");
        checkNotNull(confirmPassword, "Confirmed password cannot be null");

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {

            if (log.isWarnEnabled()) {
                log.warn("All fields must be filled");
            }

            throw new UserRegistrationException("All fields must be filled");
        }

        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {

            if (log.isWarnEnabled()) {
                log.warn("Invalid email format");
            }

            throw new UserRegistrationException("Invalid email format");
        }

        if (userRepository.findByEmail(email) != null) {

            if (log.isWarnEnabled()) {
                log.warn("User with given email already exists");
            }

            throw new UserRegistrationException("User with given email already exists");
        }

        if (!password.equals(confirmPassword)){

            if (log.isWarnEnabled()) {
                log.warn("Passwords must be equal");
            }

            throw new UserRegistrationException("Passwords must be equal");
        }

        final User user = new User(new Email(email), new Password(password));

        try {
            return userRepository.add(user);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully registered.");
            }
        }
    }

    @Override
    public SecurityTokenDTO login(LoginDTO loginDTO)
            throws UserAuthenticationException {

        if (log.isInfoEnabled()) {
            log.info("Start login user...");
        }

        final String email = loginDTO.getEmail();
        final String password = loginDTO.getPassword();

        checkNotNull(email, "Email cannot be null");
        checkNotNull(password, "Password cannot be null");

        final User user = userRepository.findByEmail(email);

        if (user == null) {

            if (log.isWarnEnabled()) {
                log.warn("Incorrect email/password");
            }

            throw new UserAuthenticationException("Incorrect email/password");
        }
        if (!user.getPassword().getPassword().equals(password)) {

            if (log.isWarnEnabled()) {
                log.warn("Incorrect email/password");
            }

            throw new UserAuthenticationException("Incorrect email/password");
        }

        final SecurityToken token = new SecurityToken(user.getId());
        final SecurityTokenId tokenId = tokenRepository.add(token);
        final SecurityToken tokenById = tokenRepository.findById(tokenId);

        final SecurityTokenDTO tokenDTO =
                new SecurityTokenDTO(tokenById.getId(), tokenById.getUserId());

        try {
            return tokenDTO;
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully logged in.");
            }
        }
    }

    @Override
    public UserDTO findByEmail(Email email) {

        final String userEmail = email.getEmail();

        if (log.isInfoEnabled()) {
            log.info("Start looking for user with email: " + userEmail);
        }

        final User user = userRepository.findByEmail(userEmail);

        try {
            return createUserDTOFromUser(user);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully found.");
            }
        }
    }

    @Override
    public UserDTO findById(UserId userId) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for user with id: " + userId.getId());
        }

        final User user = userRepository.findById(userId);

        try {
            return createUserDTOFromUser(user);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully found.");
            }
        }
    }

    @Override
    public UserDTO findByToken(SecurityTokenId tokenId) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for user by security token...");
        }

        final SecurityToken token = tokenRepository.findById(tokenId);

        if (token == null) {
            return null;
        }

        final UserId userId = token.getUserId();

        final User user = userRepository.findById(userId);

        try {
            return createUserDTOFromUser(user);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully found.");
            }
        }
    }

    @Override
    public Collection<UserDTO> findAll() {

        if (log.isInfoEnabled()) {
            log.info("Start looking for all registered users...");
        }

        final Collection<User> users = userRepository.findAll();

        final Collection<UserDTO> userDTOList = new ArrayList<>();

        for (User user : users) {
            userDTOList.add(createUserDTOFromUser(user));
        }

        try {
            return userDTOList;
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Found " + userDTOList.size() + " users.");
            }
        }
    }

    @Override
    public void delete(UserId userId) {

        if (log.isInfoEnabled()) {
            log.info("Start deleting user with id: " + userId.getId());
        }

        userRepository.delete(userId);

        if (log.isInfoEnabled()) {
            log.info("User successfully deleted.");
        }
    }

    private static UserDTO createUserDTOFromUser(User user) {

        return new UserDTO(user.getId(), user.getEmail().getEmail());
    }
}
