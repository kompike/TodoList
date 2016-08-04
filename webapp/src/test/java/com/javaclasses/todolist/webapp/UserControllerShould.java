package com.javaclasses.todolist.webapp;

import com.javaclasses.todolist.model.service.UserRegistrationException;
import org.apache.http.HttpEntity;
import org.junit.Test;

import java.io.IOException;

import static com.javaclasses.todolist.model.service.ErrorMessage.*;
import static com.javaclasses.todolist.webapp.TestUtils.getResponseContent;
import static com.javaclasses.todolist.webapp.TestUtils.loginUser;
import static com.javaclasses.todolist.webapp.TestUtils.registerUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
                "{\"errorMessage\":\"" + USER_ALREADY_EXISTS.toString() + "\"}", responseContent);
    }

    @Test
    public void prohibitRegistrationOfUserWithIncorrectEmail() throws IOException {

        final String email = "User1.com";
        final String password = "passWithoutGaps";

        final HttpEntity httpEntity = registerUser(email, password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with incorrect email was registered.",
                "{\"errorMessage\":\"" + INVALID_EMAIL_FORMAT.toString() + "\"}", responseContent);
    }

    @Test
    public void prohibitRegistrationOfUserWithDifferentPasswords() throws IOException {

        final String email = "UserWithDifferentPasswords@user.com";
        final String password = "firstPassword";
        final String confirmPassword = "secondPassword";

        final HttpEntity httpEntity = registerUser(email, password, confirmPassword);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with different passwords was registered.",
                "{\"errorMessage\":\"" + PASSWORDS_DOES_NOT_MATCH.toString() + "\"}", responseContent);
    }

    @Test
    public void prohibitRegistrationOfUserWithEmptyFields() throws IOException {

        final String email = "";
        final String password = "pass";

        final HttpEntity httpEntity = registerUser(email, password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with empty email was registered.",
                "{\"errorMessage\":\"" + ALL_FIELDS_MUST_BE_FILLED.toString() + "\"}", responseContent);
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
                "{\"errorMessage\":\"" + USER_ALREADY_EXISTS.toString() + "\"}", responseContent);
    }

    @Test
    public void allowExistingUserToLogin() throws IOException {

        final String email = "NewUser@user.com";
        final String password = "NewPassword";

        registerUser(email, password, password);

        final HttpEntity httpEntity = loginUser(email, password);

        final String responseContent = getResponseContent(httpEntity);

        assertTrue("Result must contain tokenId field.",
                responseContent.contains("tokenId"));
        assertTrue("Result must contain email field with \"" + email + "\" value.",
                responseContent.contains(email));
    }

    @Test
    public void prohibitLoginOfNotRegisteredUser() throws IOException {

        final String email = "Frank@user.com";
        final String password = "FrankPass";

        final HttpEntity httpEntity = loginUser(email, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Not registered user logged in.",
                "{\"errorMessage\":\"" + INCORRECT_CREDENTIALS.toString() + "\"}", responseContent);
    }

    @Test
    public void checkPasswordCorrectnessDuringLogin() throws IOException {

        final String email = "Misha@user.com";
        final String password = "MishaPass";

        registerUser(email, password, password);

        final HttpEntity httpEntity = loginUser(email, "password");

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with incorrect password logged in.",
                "{\"errorMessage\":\"" + INCORRECT_CREDENTIALS.toString() + "\"}", responseContent);
    }
}
