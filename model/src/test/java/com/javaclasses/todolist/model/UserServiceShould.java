package com.javaclasses.todolist.model;

import com.javaclasses.todolist.model.dto.LoginDTO;
import com.javaclasses.todolist.model.dto.RegistrationDTO;
import com.javaclasses.todolist.model.dto.SecurityTokenDTO;
import com.javaclasses.todolist.model.dto.UserDTO;
import com.javaclasses.todolist.model.entity.tinytype.Email;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.service.TaskService;
import com.javaclasses.todolist.model.service.UserAuthenticationException;
import com.javaclasses.todolist.model.service.UserRegistrationException;
import com.javaclasses.todolist.model.service.UserService;
import com.javaclasses.todolist.model.service.impl.TaskServiceImpl;
import com.javaclasses.todolist.model.service.impl.UserServiceImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.javaclasses.todolist.model.service.ErrorMessage.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private final TaskService taskService = TaskServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance(taskService);

    private final String email = "user@test.com";
    private final String password = "password";

    @Test
    public void allowToCreateNewUser()
            throws UserRegistrationException {

        final UserId userId = userService.register(new RegistrationDTO(email, password, password));
        final UserDTO userDTO = userService.findById(userId);

        assertEquals("Actual email of registered user does not equal expected.",
                email, userDTO.getEmail());

        userService.delete(userId);
    }

    @Test
    public void prohibitRegistrationOfAlreadyExistingUser()
            throws UserRegistrationException {

        final UserId userId = userService.register(new RegistrationDTO(email, password, password));
        final UserDTO userDTO = userService.findById(userId);

        assertEquals("Actual email of registered user does not equal expected.",
                email, userDTO.getEmail());

        try {
            userService.register(new RegistrationDTO(email, password, password));
            fail("Already existing user was registered.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for already existing user.",
                    USER_ALREADY_EXISTS.toString(), ex.getMessage());

            userService.delete(userId);
        }
    }

    @Test
    public void prohibitRegistrationOfUserWithInvalidEmail() {
        try {
            userService.register(new RegistrationDTO("user", password, password));
            fail("User with invalid email was registered.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for invalid email.",
                    INVALID_EMAIL_FORMAT.toString(), ex.getMessage());
        }
    }

    @Test
    public void prohibitRegistrationOfUserWithDifferentPasswords() {

        try {
            userService.register(new RegistrationDTO(email, password, "pass"));
            fail("User with different passwords was registered.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for not equal passwords.",
                    PASSWORDS_DOES_NOT_MATCH.toString(), ex.getMessage());
        }
    }

    @Test
    public void prohibitRegistrationOfUserWithEmptyFields() {

        try {
            userService.register(new RegistrationDTO("", password, password));
            fail("User with empty email was registered.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for empty fields during registration.",
                    ALL_FIELDS_MUST_BE_FILLED.toString(), ex.getMessage());
        }
    }

    @Test
    public void trimNicknameWhileRegisteringNewUser() throws UserRegistrationException {
        final String email = "me@mail.com";

        final UserId userId = userService.register(new RegistrationDTO(email, password, password));
        final UserDTO userDTO = userService.findByEmail(new Email(email));

        assertEquals("Actual email of registered user does not equal expected.",
                email, userDTO.getEmail());

        try {
            userService.register(
                    new RegistrationDTO("   me@mail.com   ", password, password));
            fail("Email was not trimmed.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for already existing user.",
                    USER_ALREADY_EXISTS.toString(), ex.getMessage());

            userService.delete(userId);
        }
    }

    @Test
    public void allowRegisteredUserToLogin()
            throws UserRegistrationException, UserAuthenticationException {

        final UserId userId =
                userService.register(new RegistrationDTO(email, password, password));
        final UserDTO userDTO = userService.findById(userId);

        assertEquals("Actual email of registered user does not equal expected.",
                email, userDTO.getEmail());

        final SecurityTokenDTO tokenDTO = userService.login(new LoginDTO(email, password));
        final UserDTO loggedUser = userService.findByToken(tokenDTO.getTokenId());

        assertEquals("Actual email of logged user does not equal expected.",
                email, loggedUser.getEmail());

        userService.delete(userId);
    }

    @Test
    public void prohibitLoginOfNotRegisteredUser() {

        try {
            userService.login(new LoginDTO(email, password));
            fail("Not registered user logged in.");
        } catch (UserAuthenticationException ex) {
            assertEquals("Wrong message for not registered user.",
                    INCORRECT_CREDENTIALS.toString(), ex.getMessage());
        }
    }

    @Test
    public void prohibitLoginOfUserWithIncorrectPassword()
            throws UserRegistrationException {

        final UserId userId =
                userService.register(new RegistrationDTO(email, password, password));
        final UserDTO userDTO = userService.findByEmail(new Email(email));

        assertEquals("Actual email of registered user does not equal expected.",
                email, userDTO.getEmail());

        try {
            userService.login(new LoginDTO(email, "pass"));
            fail("User with incorrect password logged in.");
        } catch (UserAuthenticationException ex) {
            assertEquals("Wrong message for incorrect password.",
                    INCORRECT_CREDENTIALS.toString(), ex.getMessage());

            userService.delete(userId);
        }
    }

    @Test
    public void workCorrectlyInMultipleThreads() throws Exception {

        final int threadPoolSize = 100;

        final CountDownLatch startLatch =
                new CountDownLatch(threadPoolSize);

        final ExecutorService executorService =
                Executors.newFixedThreadPool(threadPoolSize);

        final Set<UserId> uniqueUserIds = new HashSet<>();

        final Set<UserDTO> loggedUsers = new HashSet<>();

        final List<Future<UserDTO>> futureList = new ArrayList<>();

        for (int i = 0; i < threadPoolSize; i++) {

            final int currentIndex = i;

            final Future<UserDTO> future = executorService.submit(() -> {
                startLatch.countDown();
                startLatch.await();

                final String email = "User_" + currentIndex + "@user.com";
                final String password = "password_" + currentIndex;

                final UserId userId = userService.register(new RegistrationDTO(email, password, password));
                final UserDTO userDTO = userService.findById(userId);

                uniqueUserIds.add(userDTO.getUserId());

                assertEquals("Actual email of registered user does not equal expected.",
                        email, userDTO.getEmail());

                final SecurityTokenDTO tokenDTO = userService.login(new LoginDTO(email, password));
                final UserDTO loggedUserDTO = userService.findByToken(tokenDTO.getTokenId());

                assertEquals("Actual email of logged user does not equal expected.",
                        email, loggedUserDTO.getEmail());

                loggedUsers.add(loggedUserDTO);

                return userDTO;
            });

            futureList.add(future);
        }

        for (Future future: futureList) {

            future.get();
        }

        assertEquals("Users number must be " + threadPoolSize, threadPoolSize,
                userService.findAll().size());

        assertEquals("Logged users number must be " + threadPoolSize, threadPoolSize,
                loggedUsers.size());

        assertEquals("Ids are not unique", threadPoolSize,
                uniqueUserIds.size());

        for (UserDTO userDTO : userService.findAll()) {
            userService.delete(userDTO.getUserId());
        }
    }

    @Test
    public void failWhileRegisteringExistingUserInMultipleThreads() throws Exception {

        final int threadPoolSize = 99;

        final CountDownLatch startLatch =
                new CountDownLatch(threadPoolSize);

        final ExecutorService executorService =
                Executors.newFixedThreadPool(threadPoolSize);

        final Set<UserId> uniqueUserIds = new HashSet<>();

        final List<Future<UserDTO>> futureList = new ArrayList<>();

        for (int i = 0; i < threadPoolSize; i++) {

            final int currentIndex = i;

            final Future<UserDTO> future = executorService.submit(() -> {
                startLatch.countDown();
                startLatch.await();

                UserDTO userDTO = null;

                if (currentIndex == threadPoolSize / 2) {

                    final String email = "User_" + 0 + "@user.com";
                    final String password = "password_" + 0;

                    try {
                        userService.register(new RegistrationDTO(email, password, password));
                        fail("UserRegistrationException was not thrown: " + currentIndex);
                    } catch (UserRegistrationException ex) {
                        assertEquals("Wrong message for already existing user.",
                                USER_ALREADY_EXISTS.toString(), ex.getMessage());
                    }
                } else {

                    final String email = "User_" + currentIndex + "@user.com";
                    final String password = "password_" + currentIndex;

                    final UserId userId = userService.register(new RegistrationDTO(email, password, password));
                    userDTO = userService.findById(userId);

                    uniqueUserIds.add(userDTO.getUserId());

                    assertEquals("Actual email of registered user does not equal expected.",
                            email, userDTO.getEmail());
                }

                return userDTO;
            });

            futureList.add(future);
        }

        for (Future future: futureList) {

            future.get();
        }

        assertEquals("Users number must be " + (threadPoolSize - 1), threadPoolSize - 1,
                userService.findAll().size());

        assertEquals("Ids are not unique", threadPoolSize - 1,
                uniqueUserIds.size());

        for (UserDTO userDTO : userService.findAll()) {
            userService.delete(userDTO.getUserId());
        }
    }
}
