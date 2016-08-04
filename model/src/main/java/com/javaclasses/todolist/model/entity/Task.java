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
    private boolean isCompleted;
    private UserId owner;

    public Task(TaskDescription description, UserId owner) {
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.isCompleted = false;
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

    public boolean isCompleted() {
        return isCompleted;
    }

    public UserId getOwner() {
        return owner;
    }

    public void setCompletionStatus(boolean status) {
        isCompleted = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (isCompleted != task.isCompleted) return false;
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
