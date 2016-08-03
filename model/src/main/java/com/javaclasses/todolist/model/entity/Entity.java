package com.javaclasses.todolist.model.entity;

import com.javaclasses.todolist.model.entity.tinytype.EntityId;

/**
 * Abstract entity interface
 */
public interface Entity<TypeId extends EntityId> {

    TypeId getId();

    void setId(TypeId id);
}
