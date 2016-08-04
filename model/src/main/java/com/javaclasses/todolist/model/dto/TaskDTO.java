package com.javaclasses.todolist.model.dto;

import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;

import java.time.LocalDateTime;

/**
 * Data transfer object for task entity
 */
public class TaskDTO {

    private TaskId taskId;
    private String description;
    private LocalDateTime creationDate;
    private boolean isActive;
    private UserId owner;

    public TaskDTO(TaskId taskId, String description, LocalDateTime creationDate, boolean isActive, UserId owner) {
        this.taskId = taskId;
        this.description = description;
        this.creationDate = creationDate;
        this.isActive = isActive;
        this.owner = owner;
    }

    public TaskId getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskDTO taskDTO = (TaskDTO) o;

        if (isActive != taskDTO.isActive) return false;
        if (!taskId.equals(taskDTO.taskId)) return false;
        if (!description.equals(taskDTO.description)) return false;
        if (!creationDate.equals(taskDTO.creationDate)) return false;
        return owner.equals(taskDTO.owner);

    }

    @Override
    public int hashCode() {
        return taskId.hashCode();
    }
}
