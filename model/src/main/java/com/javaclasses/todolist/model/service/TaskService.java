package com.javaclasses.todolist.model.service;


import com.javaclasses.todolist.model.dto.AddedTaskDTO;
import com.javaclasses.todolist.model.dto.TaskDTO;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;

import java.util.Collection;

/**
 * Basic interface for user management
 */
public interface TaskService {

    /**
     * Adds new task to repository
     * @param taskDTO DTO with given task information
     * @return Id of added task
     * @throws TaskCreationException In case of error during adding new task
     */
    TaskId add(AddedTaskDTO taskDTO)
            throws TaskCreationException;

    /**
     * Searches for task in repository by given id
     * @param taskId Id of task to be found
     * @return DTO with task information
     */
    TaskDTO findById(TaskId taskId);

    /**
     * Gets all user's tasks
     * @param userId User's id
     * @return Collection of given user tasks
     */
    Collection<TaskDTO> findAllUserTasks(UserId userId);

    /**
     * Updates task in repository
     * @param taskId Id of task to be updated
     */
    void update(TaskId taskId);

    /**
     * Delete task from repository
     * @param taskId Id of task to be deleted
     */
    void delete(TaskId taskId);
}
