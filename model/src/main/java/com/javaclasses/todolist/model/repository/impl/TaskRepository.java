package com.javaclasses.todolist.model.repository.impl;

import com.javaclasses.todolist.model.entity.Task;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.repository.InMemoryRepository;

import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link InMemoryRepository} implementation for user entity
 */
public class TaskRepository extends InMemoryRepository<TaskId, Task> {

    private static TaskRepository taskRepository;

    private AtomicLong idCounter = new AtomicLong(1);

    private TaskRepository() {
    }

    public static TaskRepository getInstance() {
        if (taskRepository == null) {
            taskRepository = new TaskRepository();
        }

        return taskRepository;
    }

    @Override
    protected TaskId generateId() {
        return new TaskId(idCounter.getAndIncrement());
    }
}
