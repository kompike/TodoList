package com.javaclasses.todolist.model.service.impl;

import com.javaclasses.todolist.model.dto.AddedTaskDTO;
import com.javaclasses.todolist.model.dto.TaskDTO;
import com.javaclasses.todolist.model.entity.Task;
import com.javaclasses.todolist.model.entity.tinytype.TaskDescription;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.repository.impl.TaskRepository;
import com.javaclasses.todolist.model.service.TaskCreationException;
import com.javaclasses.todolist.model.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.javaclasses.todolist.model.service.ErrorMessage.TASK_DESCRIPTION_CANNOT_BE_EMPTY;

/**
 * Implementation of {@link TaskService} interface
 */
public class TaskServiceImpl implements TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static TaskServiceImpl taskService;

    private final TaskRepository taskRepository =
            TaskRepository.getInstance();

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

        if (log.isInfoEnabled()) {
            log.info("Start adding new task...");
        }

        final String description = taskDTO.getDescription().trim();
        final UserId owner = taskDTO.getOwner();

        checkNotNull(description, "Description cannot be null");
        checkNotNull(owner, "Owner cannot be null");

        if (description.isEmpty()) {

            if (log.isWarnEnabled()) {
                log.warn(TASK_DESCRIPTION_CANNOT_BE_EMPTY.toString());
            }

            throw new TaskCreationException(TASK_DESCRIPTION_CANNOT_BE_EMPTY);
        }

        final Task task = new Task(new TaskDescription(description), owner);

        try {
            return taskRepository.add(task);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Task successfully added.");
            }
        }
    }

    @Override
    public TaskDTO findById(TaskId taskId) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for task with id: " + taskId.getId());
        }

        final Task task = taskRepository.findById(taskId);

        try {
            return createTaskDTOFromTask(task);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Task successfully found.");
            }
        }
    }

    @Override
    public Collection<TaskDTO> findAllUserTasks(UserId userId) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for all tasks of user with id: " + userId.getId());
        }

        final Collection<Task> tasks = taskRepository.findAll();

        final Collection<TaskDTO> taskDTOList = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getOwner().equals(userId)) {
                taskDTOList.add(createTaskDTOFromTask(task));
            }
        }

        try {
            return taskDTOList;
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Found " + taskDTOList.size() + " tasks.");
            }
        }
    }

    @Override
    public Collection<TaskDTO> findAll() {

        if (log.isInfoEnabled()) {
            log.info("Start looking for all tasks...");
        }

        final Collection<Task> tasks = taskRepository.findAll();

        final Collection<TaskDTO> taskDTOs = new ArrayList<>();

        for (Task task : tasks) {
            taskDTOs.add(createTaskDTOFromTask(task));
        }

        try {
            return taskDTOs;
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Found " + taskDTOs.size() + " tasks.");
            }
        }
    }

    @Override
    public void update(TaskId taskId) {

        if (log.isInfoEnabled()) {
            log.info("Start completing task with id: " + taskId.getId());
        }

        final Task task = taskRepository.findById(taskId);
        final boolean taskCompletionStatus = task.isCompleted();
        task.setCompletionStatus(!taskCompletionStatus);

        if (log.isInfoEnabled()) {
            log.info("Task successfully completed.");
        }

    }


    @Override
    public void delete(TaskId taskId) {

        if (log.isInfoEnabled()) {
            log.info("Start deleting task with id: " + taskId.getId());
        }

        taskRepository.delete(taskId);

        if (log.isInfoEnabled()) {
            log.info("Task successfully deleted.");
        }
    }

    private static TaskDTO createTaskDTOFromTask(Task task) {
        return new TaskDTO(task.getId(), task.getDescription().getDescription(),
                task.getCreationDate(), task.isCompleted(), task.getOwner());
    }
}
