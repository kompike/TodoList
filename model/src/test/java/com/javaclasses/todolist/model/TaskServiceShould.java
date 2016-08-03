package com.javaclasses.todolist.model;

import com.javaclasses.todolist.model.dto.AddedTaskDTO;
import com.javaclasses.todolist.model.dto.TaskDTO;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.service.TaskCreationException;
import com.javaclasses.todolist.model.service.TaskService;
import com.javaclasses.todolist.model.service.impl.TaskServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TaskServiceShould {

    private final TaskService taskService = TaskServiceImpl.getInstance();

    @Test
    public void allowToCreateNewTask() throws TaskCreationException {

        final String taskDescription = "New task";

        final TaskId taskId =
                taskService.add(new AddedTaskDTO(taskDescription, new UserId(1)));
        final TaskDTO task = taskService.findById(taskId);

        assertEquals("Actual task description does not equal expected.",
                taskDescription, task.getDescription());

        taskService.delete(taskId);
    }

    @Test
    public void prohibitToCreateTaskWithEmptyDescription() {

        try {
            taskService.add(new AddedTaskDTO("", new UserId(1)));
            fail("Task with empty description was created.");
        } catch (TaskCreationException ex) {
            assertEquals("Wrong message for creation task with empty message.",
                    "Task description cannot be empty", ex.getMessage());
        }
    }

    @Test
    public void allowUserToUpdateTask() throws TaskCreationException {

        final String taskDescription = "New task";

        final TaskId taskId =
                taskService.add(new AddedTaskDTO(taskDescription, new UserId(2)));
        final TaskDTO task = taskService.findById(taskId);

        assertEquals("Actual task description does not equal expected.",
                taskDescription, task.getDescription());

        taskService.update(taskId);
        final TaskDTO updatedTask = taskService.findById(taskId);

        assertEquals("Task status was not changed.",
                false, updatedTask.isActive());

        taskService.update(taskId);
        final TaskDTO updatedTwiceTask = taskService.findById(taskId);

        assertEquals("Task status was not changed.",
                true, updatedTwiceTask.isActive());

        taskService.delete(taskId);
    }
}
