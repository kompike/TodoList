package com.javaclasses.todolist.model.entity;

import com.javaclasses.todolist.model.entity.tinytype.TaskDescription;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;

import java.time.LocalDateTime;

/**
 * Task entity implementation
 */
public class Task implements Entity<TaskId> {

    private TaskId taskId;
    private TaskDescription description;
    private LocalDateTime creationDate;
    private boolean isActive;
    private UserId owner;

    public Task(TaskDescription description, UserId owner) {
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.isActive = false;
        this.owner = owner;
    }

    @Override
    public TaskId getId() {
        return taskId;
    }

    @Override
    public void setId(TaskId id) {
        this.taskId = id;
    }

    public TaskDescription getDescription() {
        return description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public UserId getOwner() {
        return owner;
    }

    public void setStatus(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (isActive != task.isActive) return false;
        if (!taskId.equals(task.taskId)) return false;
        if (!description.equals(task.description)) return false;
        if (!creationDate.equals(task.creationDate)) return false;
        return owner.equals(task.owner);

    }

    @Override
    public int hashCode() {
        return taskId.hashCode();
    }
}
