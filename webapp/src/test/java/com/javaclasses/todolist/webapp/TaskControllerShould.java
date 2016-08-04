package com.javaclasses.todolist.webapp;

import org.apache.http.HttpEntity;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static com.javaclasses.todolist.webapp.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TaskControllerShould {

    private static String tokenId;

    @BeforeClass
    public static void registerAndLoginNewUser() throws IOException {

        final String email = "kompike@user.com";
        final String password = "NewPassword";

        registerUser(email, password, password);

        final HttpEntity httpEntity = loginUser(email, password);

        tokenId = getParameterFromResponse(httpEntity, "tokenId");
    }

    @Test
    public void allowUserToAddNewTask() throws IOException {

        final String description = "New task for today";

        final HttpEntity httpEntity = addNewTask(tokenId, description);

        final String responseContent = getResponseContent(httpEntity);

        assertTrue("Created task does not contain 'message' field.",
                responseContent.contains("message"));
        assertTrue("Created task does not contain 'userTasks' field.",
                responseContent.contains("userTasks"));

    }

    @Test
    public void prohibitUserToAddTaskWithEmptyDescription() throws IOException {

        final String description = "   ";

        final HttpEntity httpEntity = addNewTask(tokenId, description);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Task with empty description was registered.",
                "{\"errorMessage\":\"Task description cannot be empty\"}", responseContent);

    }

    @Test
    public void allowUserToCompleteTask() throws IOException {

        final String description = "New task for tomorrow";

        final HttpEntity taskCreationEntity = addNewTask(tokenId, description);
        final String taskId = getParameterFromResponse(taskCreationEntity, "taskId");

        final HttpEntity httpEntity = completeTask(tokenId, taskId);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Task was not completed.",
                "{\"message\":\"Task successfully completed\"}", responseContent);

    }

    @Test
    public void allowUserToReopenTask() throws IOException {

        final String description = "Old task for today";

        addNewTask(tokenId, description);
        final HttpEntity taskCreationEntity = addNewTask(tokenId, description);
        final String taskId = getParameterFromResponse(taskCreationEntity, "taskId");

        final HttpEntity httpEntity = reopenTask(tokenId, taskId);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Task was not reopened.",
                "{\"message\":\"Task successfully reopened\"}", responseContent);

    }

    @Test
    public void allowUserToDeleteTask() throws IOException {

        final String description = "some task for me";

        addNewTask(tokenId, description);
        final HttpEntity taskCreationEntity = addNewTask(tokenId, description);
        final String taskId = getParameterFromResponse(taskCreationEntity, "taskId");

        final HttpEntity httpEntity = deleteTask(tokenId, taskId);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Task was not deleted.",
                "{\"message\":\"Task successfully deleted\"}", responseContent);

    }
}
