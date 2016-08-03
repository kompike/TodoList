package com.javaclasses.todolist.model.dto;

import com.javaclasses.todolist.model.entity.tinytype.UserId;

/**
 * Data transfer object for added task
 */
public class AddedTaskDTO {

    private final String description;
    private final UserId owner;

    public AddedTaskDTO(String description, UserId owner) {
        this.description = description;
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public UserId getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddedTaskDTO that = (AddedTaskDTO) o;

        if (!description.equals(that.description)) return false;
        return owner.equals(that.owner);

    }

    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + owner.hashCode();
        return result;
    }
}
