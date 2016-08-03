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
                    "User with given email already exists", ex.getMessage());

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
                    "Invalid email format", ex.getMessage());
        }
    }

    @Test
    public void prohibitRegistrationOfUserWithDifferentPasswords() {

        try {
            userService.register(new RegistrationDTO(email, password, "pass"));
            fail("User with different passwords was registered.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for not equal passwords.",
                    "Passwords must be equal", ex.getMessage());
        }
    }

    @Test
    public void prohibitRegistrationOfUserWithEmptyFields() {

        try {
            userService.register(new RegistrationDTO("", password, password));
            fail("User with empty email was registered.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for empty fields during registration.",
                    "All fields must be filled", ex.getMessage());
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
                    "User with given email already exists", ex.getMessage());

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
                    "Incorrect email/password", ex.getMessage());
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
                    "Incorrect email/password", ex.getMessage());

            userService.delete(userId);
        }
    }
}
