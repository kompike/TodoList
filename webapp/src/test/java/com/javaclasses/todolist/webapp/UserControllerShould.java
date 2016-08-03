package com.javaclasses.todolist.webapp;

import com.javaclasses.todolist.model.service.UserRegistrationException;
import org.apache.http.HttpEntity;
import org.junit.Test;

import java.io.IOException;

import static com.javaclasses.todolist.webapp.TestUtils.getResponseContent;
import static com.javaclasses.todolist.webapp.TestUtils.registerUser;
import static org.junit.Assert.assertEquals;

public class UserControllerShould {

    @Test
    public void allowNewUserToRegister() throws IOException {

        final String email = "User@user.com";
        final String password = "password";

        final HttpEntity httpEntity = registerUser(email, password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Response content does not equal expected JSON.",
                "{\"message\":\"User successfully registered\"}", responseContent);
    }

    @Test
    public void prohibitRegistrationOfAlreadyExistingUser() throws IOException {

        final String email = "ExistingUser@user.com";
        final String password = "ExistingPassword";

        registerUser(email, password, password);
        final HttpEntity httpEntity = registerUser(email, password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Already existing user was registered.",
                "{\"errorMessage\":\"User with given email already exists\"}", responseContent);
    }

    @Test
    public void prohibitRegistrationOfUserWithIncorrectEmail() throws IOException {

        final String email = "User1.com";
        final String password = "passWithoutGaps";

        final HttpEntity httpEntity = registerUser(email, password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with incorrect email was registered.",
                "{\"errorMessage\":\"Invalid email format\"}", responseContent);
    }

    @Test
    public void prohibitRegistrationOfUserWithDifferentPasswords() throws IOException {

        final String email = "UserWithDifferentPasswords@user.com";
        final String password = "firstPassword";
        final String confirmPassword = "secondPassword";

        final HttpEntity httpEntity = registerUser(email, password, confirmPassword);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with different passwords was registered.",
                "{\"errorMessage\":\"Passwords must be equal\"}", responseContent);
    }

    @Test
    public void prohibitRegistrationOfUserWithEmptyFields() throws IOException {

        final String email = "";
        final String password = "pass";

        final HttpEntity httpEntity = registerUser(email, password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with empty email was registered.",
                "{\"errorMessage\":\"All fields must be filled\"}", responseContent);
    }

    @Test
    public void trimNicknameWhileRegisteringNewUser()
            throws UserRegistrationException, IOException {

        final String email = "UserWithWhitespaces@user.com";
        final String password = "password";

        registerUser(email, password, password);

        final HttpEntity httpEntity = registerUser("  UserWithWhitespaces@user.com  ", password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Already existing user was registered.",
                "{\"errorMessage\":\"User with given email already exists\"}", responseContent);
    }
}
