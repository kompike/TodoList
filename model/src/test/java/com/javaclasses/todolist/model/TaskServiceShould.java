package com.javaclasses.todolist.model;

import com.javaclasses.todolist.model.dto.AddedTaskDTO;
import com.javaclasses.todolist.model.dto.TaskDTO;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.service.TaskCreationException;
import com.javaclasses.todolist.model.service.TaskService;
import com.javaclasses.todolist.model.service.impl.TaskServiceImpl;
import org.junit.Test;

import java.util.Collection;

import static com.javaclasses.todolist.model.service.ErrorMessage.TASK_DESCRIPTION_CANNOT_BE_EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
                    TASK_DESCRIPTION_CANNOT_BE_EMPTY.toString(), ex.getMessage());
        }
    }

    @Test
    public void allowUserToCompleteTask() throws TaskCreationException {

        final String taskDescription = "New task";

        final TaskId taskId =
                taskService.add(new AddedTaskDTO(taskDescription, new UserId(2)));
        final TaskDTO task = taskService.findById(taskId);

        assertEquals("Actual task description does not equal expected.",
                taskDescription, task.getDescription());

        taskService.update(taskId);
        final TaskDTO completedTask = taskService.findById(taskId);

        assertEquals("Task status was not changed.",
                true, completedTask.isActive());

        taskService.delete(taskId);
    }

    @Test
    public void allowUserToReopenTask() throws TaskCreationException {

        final String taskDescription = "New task";

        final TaskId taskId =
                taskService.add(new AddedTaskDTO(taskDescription, new UserId(2)));
        final TaskDTO task = taskService.findById(taskId);

        assertEquals("Actual task description does not equal expected.",
                taskDescription, task.getDescription());

        taskService.update(taskId);
        final TaskDTO completedTask = taskService.findById(taskId);

        assertEquals("Task status was not changed.",
                true, completedTask.isActive());

        taskService.update(taskId);
        final TaskDTO reopenedTask = taskService.findById(taskId);

        assertEquals("Task status was not changed.",
                false, reopenedTask.isActive());

        taskService.delete(taskId);
    }

    @Test
    public void allowUserToDeleteTask() throws TaskCreationException {

        final String taskDescription = "Task to delete";

        final TaskId taskId =
                taskService.add(new AddedTaskDTO(taskDescription, new UserId(2)));
        final TaskDTO task = taskService.findById(taskId);

        assertEquals("Actual task description does not equal expected.",
                taskDescription, task.getDescription());

        taskService.delete(taskId);

        final Collection<TaskDTO> allTasks = taskService.findAll();

        assertTrue("Task was not deleted.",
                allTasks.isEmpty());
    }
}
