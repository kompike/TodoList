package com.javaclasses.todolist.model.entity.tinytype;

/**
 * Tiny type for entity's unique identifier
 */
public class EntityId {

    private final long id;

    /*package*/ EntityId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityId entityId = (EntityId) o;

        return id == entityId.id;

    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
