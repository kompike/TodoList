package com.javaclasses.todolist.model.service.impl;

import com.javaclasses.todolist.model.dto.AddedTaskDTO;
import com.javaclasses.todolist.model.dto.TaskDTO;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.service.TaskCreationException;
import com.javaclasses.todolist.model.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Implementation of {@link TaskService} interface
 */
public class TaskServiceImpl implements TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static TaskServiceImpl taskService;

    private TaskServiceImpl() {
    }

    public static TaskServiceImpl getInstance() {
        if (taskService == null) {
            taskService = new TaskServiceImpl();
        }

        return taskService;
    }

    @Override
    public TaskId add(AddedTaskDTO taskDTO) throws TaskCreationException {
        return null;
    }

    @Override
    public TaskDTO findById(TaskId taskId) {
        return null;
    }

    @Override
    public Collection<TaskDTO> findAllUserTasks(UserId userId) {
        return null;
    }

    @Override
    public void update(TaskId taskId) {

    }

    @Override
    public void delete(TaskId taskId) {

    }
}
