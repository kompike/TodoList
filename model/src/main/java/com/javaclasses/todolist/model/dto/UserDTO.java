package com.javaclasses.todolist.model.dto;

import com.javaclasses.todolist.model.entity.tinytype.UserId;

/**
 * Data transfer object for user entity
 */
public class UserDTO {

    private final UserId userId;
    private final String email;

    public UserDTO(UserId userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (!userId.equals(userDTO.userId)) return false;
        return email.equals(userDTO.email);

    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
