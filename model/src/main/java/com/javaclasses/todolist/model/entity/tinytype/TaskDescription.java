package com.javaclasses.todolist.model.entity.tinytype;

/**
 * Tiny type for task description
 */
public class TaskDescription {

    private final String description;

    public TaskDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskDescription that = (TaskDescription) o;

        return description.equals(that.description);

    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    @Override
    public String toString() {
        return description;
    }
}
