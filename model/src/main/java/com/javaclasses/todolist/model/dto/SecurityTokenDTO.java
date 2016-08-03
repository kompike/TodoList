package com.javaclasses.todolist.model.dto;

import com.javaclasses.todolist.model.entity.tinytype.SecurityTokenId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;

/**
 * Data transfer object for security token entity
 */
public class SecurityTokenDTO {

    private final SecurityTokenId tokenId;
    private final UserId userId;

    public SecurityTokenDTO(SecurityTokenId tokenId, UserId userId) {
        this.tokenId = tokenId;
        this.userId = userId;
    }

    public SecurityTokenId getTokenId() {
        return tokenId;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecurityTokenDTO that = (SecurityTokenDTO) o;

        if (!tokenId.equals(that.tokenId)) return false;
        return userId.equals(that.userId);

    }

    @Override
    public int hashCode() {
        return tokenId.hashCode();
    }
}
